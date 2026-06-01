package com.openlib.market.infrastructure.curaduria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.application.curaduria.IRechazarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.IRevisarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.LibroParaRevisionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuraduriaAdminController.class)
class CuraduriaAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private IRevisarCuraduriaContenidoUseCase revisarUseCase;

    @MockitoBean
    private IRechazarCuraduriaContenidoUseCase rechazarUseCase;

    @Test
    void debeListarLibrosPendientes() throws Exception {
        LibroParaRevisionDto dto = new LibroParaRevisionDto("isbn1", "Tit", "Sin", 10.0, "url", "vend", "VendName", "123");
        when(revisarUseCase.listarLibrosPendientes(0, 20)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/admin/curaduria-admin/libros-pendientes")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("isbn1"))
                .andExpect(jsonPath("$[0].nombreVendedor").value("VendName"));
    }

    @Test
    void debeRechazarLibroCorrectamente() throws Exception {
        CuraduriaAdminController.RechazoRequest request = new CuraduriaAdminController.RechazoRequest("Contenido plagiado");

        mockMvc.perform(post("/api/v1/admin/curaduria-admin/libros/isbn1/rechazar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Libro rechazado correctamente"));

        verify(rechazarUseCase).rechazarLibro("isbn1", "Contenido plagiado");
    }

    @Test
    void debeRetornarBadRequestSiMotivoEsInvalido() throws Exception {
        doThrow(new IllegalArgumentException("El motivo de rechazo debe tener al menos 10 caracteres."))
                .when(rechazarUseCase).rechazarLibro("isbn1", "corto");

        CuraduriaAdminController.RechazoRequest request = new CuraduriaAdminController.RechazoRequest("corto");

        mockMvc.perform(post("/api/v1/admin/curaduria-admin/libros/isbn1/rechazar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El motivo de rechazo debe tener al menos 10 caracteres."));
    }
}
