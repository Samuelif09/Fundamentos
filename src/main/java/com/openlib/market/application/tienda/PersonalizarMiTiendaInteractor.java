package com.openlib.market.application.tienda;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;

public class PersonalizarMiTiendaInteractor implements IPersonalizarMiTiendaUseCase {

    private static final long MAX_BANNER_BYTES = 3 * 1024 * 1024; // 3MB para banner

    private final IAlmacenamientoGateway almacenamientoGateway;
    private final ITiendaVendedorGateway tiendaGateway;

    public PersonalizarMiTiendaInteractor(IAlmacenamientoGateway almacenamientoGateway, ITiendaVendedorGateway tiendaGateway) {
        this.almacenamientoGateway = almacenamientoGateway;
        this.tiendaGateway = tiendaGateway;
    }

    @Override
    public void subirBanner(String idVendedor, byte[] contenido, String mimeType, String nombreOriginal) {
        if (contenido == null || contenido.length == 0) {
            throw new IllegalArgumentException("El archivo de banner no puede estar vacío");
        }
        if (contenido.length > MAX_BANNER_BYTES) {
            throw new IllegalArgumentException("El banner supera el límite de 3MB");
        }

        // Reutilizamos el VO ArchivoImagen para validar MIME type permitido
        ArchivoImagen archivoImagen = new ArchivoImagen(contenido, mimeType, nombreOriginal);
        String urlBanner = almacenamientoGateway.guardar(archivoImagen, "banner_" + idVendedor);

        tiendaGateway.actualizarBanner(idVendedor, urlBanner);
    }
}
