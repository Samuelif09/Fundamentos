package com.openlib.market.application.curaduria;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.curaduria.MotivoRechazo;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.notificacion.INotificacionGateway;

@Service
public class RechazarCuraduriaContenidoInteractor implements IRechazarCuraduriaContenidoUseCase {

    private final ICuraduriaGateway curaduriaGateway;
    private final INotificacionGateway notificacionGateway;

    public RechazarCuraduriaContenidoInteractor(ICuraduriaGateway curaduriaGateway, INotificacionGateway notificacionGateway) {
        this.curaduriaGateway = curaduriaGateway;
        this.notificacionGateway = notificacionGateway;
    }

    @Override
    public void rechazarLibro(String isbn, String motivoStr) {
        MotivoRechazo motivo = new MotivoRechazo(motivoStr);
        Libro libro = curaduriaGateway.obtenerPorIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        Libro rechazado = libro.rechazar(motivo);
        curaduriaGateway.actualizar(rechazado);

        notificacionGateway.notificarRechazoLibro(rechazado.getIdVendedor(), rechazado.getTitulo(), motivo.getRazon());
    }
}
