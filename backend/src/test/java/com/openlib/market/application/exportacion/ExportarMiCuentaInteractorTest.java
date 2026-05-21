package com.openlib.market.application.exportacion;

import com.openlib.market.domain.exportacion.DataExportadaUsuario;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportarMiCuentaInteractorTest {

    private IUsuarioGateway usuarioGateway;
    private IPedidoGateway pedidoGateway;
    private ExportarMiCuentaInteractor interactor;

    @BeforeEach
    void setUp() {
        usuarioGateway = mock(IUsuarioGateway.class);
        pedidoGateway = mock(IPedidoGateway.class);
        interactor = new ExportarMiCuentaInteractor(usuarioGateway, pedidoGateway);
    }

    @Test
    void debeExportarDatosCompletosSiUsuarioExiste() {
        Usuario usuario = mock(Usuario.class);
        when(usuarioGateway.buscarPorId("user-1")).thenReturn(Optional.of(usuario));
        when(pedidoGateway.listarPorUsuarioId("user-1", 0, 1000)).thenReturn(List.of(mock(Pedido.class)));

        DataExportadaUsuario data = interactor.exportar("user-1");

        assertNotNull(data);
        assertEquals(usuario, data.getPerfil());
        assertEquals(1, data.getPedidos().size());
        assertNotNull(data.getSuscripciones());
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExiste() {
        when(usuarioGateway.buscarPorId("user-desc")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> interactor.exportar("user-desc"));
    }
}
