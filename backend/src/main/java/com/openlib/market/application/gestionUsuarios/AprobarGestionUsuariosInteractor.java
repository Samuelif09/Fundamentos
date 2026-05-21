package com.openlib.market.application.gestionUsuarios;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.INotificacionAdminGateway;
import com.openlib.market.domain.vendedor.Vendedor;

import java.util.Optional;

@Service
public class AprobarGestionUsuariosInteractor implements IAprobarGestionUsuariosUseCase {

    private final IVendedorGateway vendedorGateway;
    private final INotificacionAdminGateway notificacionGateway;

    public AprobarGestionUsuariosInteractor(IVendedorGateway vendedorGateway, INotificacionAdminGateway notificacionGateway) {
        this.vendedorGateway = vendedorGateway;
        this.notificacionGateway = notificacionGateway;
    }

    @Override
    public void aprobarVendedor(String idVendedor) {
        Optional<Vendedor> vendedorOpt = vendedorGateway.obtenerPorId(idVendedor);
        if (vendedorOpt.isEmpty()) {
            throw new IllegalArgumentException("Vendedor no encontrado");
        }

        Vendedor vendedor = vendedorOpt.get();
        vendedor.aprobar();

        vendedorGateway.actualizar(vendedor);
        notificacionGateway.notificarVendedorAprobado(idVendedor);
    }
}
