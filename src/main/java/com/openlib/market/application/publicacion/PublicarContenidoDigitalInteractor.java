package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.DigitalContentFactory;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Precio;

public class PublicarContenidoDigitalInteractor implements IPublicarContenidoDigitalUseCase {

    private final IContenidoDigitalGateway contenidoDigitalGateway;

    public PublicarContenidoDigitalInteractor(IContenidoDigitalGateway contenidoDigitalGateway) {
        this.contenidoDigitalGateway = contenidoDigitalGateway;
    }

    @Override
    public void publicar(PublicarContenidoRequestDto request) {
        ContenidoDigital contenido = DigitalContentFactory.crear(
                request.tipoFormato(),
                new Isbn(request.isbn()),
                request.titulo(),
                request.sinopsis(),
                new Precio(request.precio()),
                request.urlPortada(),
                request.categoria(),
                request.idVendedor(),
                request.duracionMinutos()
        );

        contenidoDigitalGateway.guardarContenido(contenido);
    }
}
