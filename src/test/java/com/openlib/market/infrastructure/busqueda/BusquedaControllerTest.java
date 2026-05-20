package com.openlib.market.infrastructure.busqueda;

import com.openlib.market.application.busqueda.IBuscarBusquedaUseCase;
import com.openlib.market.application.busqueda.LibroBuscadoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusquedaController.class)
class BusquedaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private IBuscarBusquedaUseCase useCase;

        @Test
        void debeRetornarResultadosExitososConStatus200() throws Exception {
                when(useCase.buscarPorPalabrasClave("spring"))
                                .thenReturn(List.of(new LibroBuscadoDto("1", "Spring Boot", "Craig")));

                mockMvc.perform(get("/api/v1/catalogo/buscar").param("q", "spring"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].titulo").value("Spring Boot"));
        }

        @Test
        void debeRetornarBadRequestSiPalabraEsInvalida() throws Exception {
                when(useCase.buscarPorPalabrasClave("a"))
                                .thenThrow(new IllegalArgumentException(
                                                "La palabra clave debe tener al menos 3 caracteres."));

                mockMvc.perform(get("/api/v1/catalogo/buscar").param("q", "a"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("La palabra clave debe tener al menos 3 caracteres."));
        }
}
