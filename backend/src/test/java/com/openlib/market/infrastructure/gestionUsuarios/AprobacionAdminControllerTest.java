package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.application.gestionUsuarios.IAprobarGestionUsuariosUseCase;
import com.openlib.market.domain.vendedor.SolicitudInvalidaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(AprobacionAdminController.class)
class AprobacionAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAprobarGestionUsuariosUseCase aprobarUseCase;

    @Test
    void debeRetornarOkCuandoSeApruebaConExito() throws Exception {
        mockMvc.perform(post("/api/v1/admin/vendedores/seller1/aprobar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vendedor aprobado correctamente"));

        verify(aprobarUseCase).aprobarVendedor("seller1");
    }

    @Test
    void debeRetornarBadRequestSiVendedorNoExiste() throws Exception {
        doThrow(new IllegalArgumentException("Vendedor no encontrado"))
                .when(aprobarUseCase).aprobarVendedor("seller2");

        mockMvc.perform(post("/api/v1/admin/vendedores/seller2/aprobar"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Vendedor no encontrado"));
    }

    @Test
    void debeRetornarConflictSiVendedorNoEstaEnRevision() throws Exception {
        doThrow(new SolicitudInvalidaException("El vendedor no está en revisión."))
                .when(aprobarUseCase).aprobarVendedor("seller3");

        mockMvc.perform(post("/api/v1/admin/vendedores/seller3/aprobar"))
                .andExpect(status().isConflict())
                .andExpect(content().string("El vendedor no está en revisión."));
    }
}
