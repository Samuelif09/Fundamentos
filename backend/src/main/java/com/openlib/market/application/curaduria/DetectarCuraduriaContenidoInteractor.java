package com.openlib.market.application.curaduria;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.curaduria.IInteligenciaArtificialGateway;
import com.openlib.market.domain.curaduria.RevisionAutomatica;
import com.openlib.market.domain.curaduria.ScoreToxicidad;

@Service
public class DetectarCuraduriaContenidoInteractor implements IDetectarCuraduriaContenidoUseCase {

    private final IInteligenciaArtificialGateway iaGateway;

    public DetectarCuraduriaContenidoInteractor(IInteligenciaArtificialGateway iaGateway) {
        this.iaGateway = iaGateway;
    }

    @Override
    public String evaluarContenido(String idElemento, String texto) {
        ScoreToxicidad score = iaGateway.analizarTexto(texto);
        RevisionAutomatica revision = new RevisionAutomatica(idElemento, score);
        
        // En una implementación real, aquí se actualizaría el estado del libro/reseña 
        // a INACTIVO si es RECHAZADO, o se enviaría a revisión humana si es SOSPECHOSO.
        // Para este MVP retornamos el Veredicto.
        
        return revision.getVeredicto().name();
    }
}
