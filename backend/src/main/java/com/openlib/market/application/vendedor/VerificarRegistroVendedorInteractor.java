package com.openlib.market.application.vendedor;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.vendedor.INotificacionAdminGateway;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;

import java.util.Optional;

@Service
public class VerificarRegistroVendedorInteractor implements IVerificarRegistroVendedorUseCase {

    private final IVendedorGateway vendedorGateway;
    private final IAlmacenamientoGateway almacenamientoGateway;
    private final INotificacionAdminGateway notificacionAdminGateway;

    public VerificarRegistroVendedorInteractor(
            IVendedorGateway vendedorGateway,
            IAlmacenamientoGateway almacenamientoGateway,
            INotificacionAdminGateway notificacionAdminGateway) {
        this.vendedorGateway = vendedorGateway;
        this.almacenamientoGateway = almacenamientoGateway;
        this.notificacionAdminGateway = notificacionAdminGateway;
    }

    @Override
    public void solicitarVerificacion(String idVendedor, byte[] documentoContenido, String mimeType, String nombreOriginal) {
        Optional<Vendedor> vendedorOpt = vendedorGateway.obtenerPorId(idVendedor);
        if (vendedorOpt.isEmpty()) {
            throw new IllegalArgumentException("Vendedor no encontrado");
        }

        Vendedor vendedor = vendedorOpt.get();
        vendedor.solicitarVerificacion();

        // 3MB limit for identity document
        if (documentoContenido == null || documentoContenido.length == 0) {
            throw new IllegalArgumentException("El documento no puede estar vacío");
        }
        if (documentoContenido.length > 3 * 1024 * 1024) {
            throw new IllegalArgumentException("El documento supera el límite de 3MB");
        }

        ArchivoImagen archivo = new ArchivoImagen(documentoContenido, mimeType, nombreOriginal);
        almacenamientoGateway.guardar(archivo, "verificacion_" + idVendedor);

        vendedorGateway.actualizar(vendedor);
        notificacionAdminGateway.notificarVerificacionPendiente(idVendedor);
    }
}
