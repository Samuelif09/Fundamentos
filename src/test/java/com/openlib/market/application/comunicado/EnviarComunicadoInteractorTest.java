package com.openlib.market.application.comunicado;

import com.openlib.market.domain.comunicado.ComunicadoMasivo;
import com.openlib.market.domain.comunicado.INotificacionGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnviarComunicadoInteractorTest {

    private IUsuarioGateway usuarioGateway;
    private INotificacionGateway notificacionGateway;
    private EnviarComunicadoInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioGateway.class);
        notificacionGateway = mock(INotificacionGateway.class);
        interactor = new EnviarComunicadoInteractor(usuarioGateway, notificacionGateway);
    }

    @Test
    void debeFiltrarSoloVendedoresYEnviar() {
        Usuario comprador = new Usuario("u1", "C", new com.openlib.market.domain.registro.Email("c@test.com"), new com.openlib.market.domain.registro.Password("Pass123!"), RolUsuario.COMPRADOR);
        Usuario vendedor = new Usuario("u2", "V", new com.openlib.market.domain.registro.Email("v@test.com"), new com.openlib.market.domain.registro.Password("Pass123!"), RolUsuario.VENDEDOR);

        when(usuarioGateway.listarTodos()).thenReturn(List.of(comprador, vendedor));

        ComunicadoDto dto = interactor.enviar("Test", "Cuerpo", "SOLO_VENDEDORES");

        assertEquals(1, dto.getCantidadDestinatarios());
        verify(notificacionGateway).enviarComunicadoMasivo(any(ComunicadoMasivo.class), eq(List.of("v@test.com")));
    }
    
    @Test
    void noDebeLlamarAlGatewaySiNoHayDestinatarios() {
        Usuario comprador = new Usuario("u1", "C", new com.openlib.market.domain.registro.Email("c@test.com"), new com.openlib.market.domain.registro.Password("Pass123!"), RolUsuario.COMPRADOR);
        when(usuarioGateway.listarTodos()).thenReturn(List.of(comprador));

        ComunicadoDto dto = interactor.enviar("Test", "Cuerpo", "SOLO_VENDEDORES");

        assertEquals(0, dto.getCantidadDestinatarios());
        verify(notificacionGateway, never()).enviarComunicadoMasivo(any(), any());
    }
}
