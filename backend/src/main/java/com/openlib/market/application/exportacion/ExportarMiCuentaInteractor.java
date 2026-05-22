package com.openlib.market.application.exportacion;

import com.openlib.market.domain.exportacion.DataExportadaUsuario;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.suscripcion.SuscripcionAutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExportarMiCuentaInteractor implements IExportarMiCuentaUseCase {

    private final IUsuarioGateway usuarioGateway;
    private final IPedidoGateway pedidoGateway;
    // Para simplificar la demo de C-23, omitiremos ISuscripcionGateway en el constructor si no tiene metodo listar, 
    // o lo podemos simular. Asumiremos que el usuario obtiene sus pedidos por ahora.

    public ExportarMiCuentaInteractor(IUsuarioGateway usuarioGateway, IPedidoGateway pedidoGateway) {
        this.usuarioGateway = usuarioGateway;
        this.pedidoGateway = pedidoGateway;
    }

    @Override
    public DataExportadaUsuario exportar(String idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioGateway.buscarPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        List<Pedido> pedidos = pedidoGateway.listarPorUsuarioId(idUsuario, 0, 1000);
        List<SuscripcionAutor> suscripciones = new ArrayList<>(); // Mock o añadir gateway real
        
        return new DataExportadaUsuario(usuarioOpt.get(), pedidos, suscripciones);
    }
}
