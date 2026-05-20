package com.openlib.market.domain.curaduria;

import java.time.LocalDateTime;
import java.util.UUID;

public class RevisionAutomatica {
    private final String id;
    private final String idElemento; // Puede ser IdLibro o IdResena
    private final ScoreToxicidad score;
    private final Veredicto veredicto;
    private final LocalDateTime fechaRevision;

    public RevisionAutomatica(String idElemento, ScoreToxicidad score) {
        if (idElemento == null || idElemento.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del elemento es obligatorio");
        }
        
        this.id = UUID.randomUUID().toString();
        this.idElemento = idElemento;
        this.score = score;
        this.fechaRevision = LocalDateTime.now();
        
        // Reglas de negocio del dominio
        if (score.valor() > 0.8) {
            this.veredicto = Veredicto.RECHAZADO;
        } else if (score.valor() >= 0.4) {
            this.veredicto = Veredicto.SOSPECHOSO;
        } else {
            this.veredicto = Veredicto.APROBADO;
        }
    }

    public String getId() { return id; }
    public String getIdElemento() { return idElemento; }
    public ScoreToxicidad getScore() { return score; }
    public Veredicto getVeredicto() { return veredicto; }
    public LocalDateTime getFechaRevision() { return fechaRevision; }
}
