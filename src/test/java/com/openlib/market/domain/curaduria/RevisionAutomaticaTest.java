package com.openlib.market.domain.curaduria;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RevisionAutomaticaTest {

    @Test
    void debeRechazarScoreAlto() {
        RevisionAutomatica revision = new RevisionAutomatica("elem1", new ScoreToxicidad(0.9));
        assertEquals(Veredicto.RECHAZADO, revision.getVeredicto());
    }

    @Test
    void debeMarcarSospechosoScoreMedio() {
        RevisionAutomatica revision = new RevisionAutomatica("elem1", new ScoreToxicidad(0.6));
        assertEquals(Veredicto.SOSPECHOSO, revision.getVeredicto());
    }

    @Test
    void debeAprobarScoreBajo() {
        RevisionAutomatica revision = new RevisionAutomatica("elem1", new ScoreToxicidad(0.2));
        assertEquals(Veredicto.APROBADO, revision.getVeredicto());
    }

    @Test
    void debeValidarRangoScore() {
        assertThrows(IllegalArgumentException.class, () -> new ScoreToxicidad(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new ScoreToxicidad(1.1));
    }
}
