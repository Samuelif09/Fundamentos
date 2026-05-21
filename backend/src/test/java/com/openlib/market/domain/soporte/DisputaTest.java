package com.openlib.market.domain.soporte;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DisputaTest {

    @Test
    void debeIniciarMediacionExitosamente() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "Producto defectuoso");
        assertEquals(EstadoDisputa.ABIERTA, disputa.getEstado());
        
        disputa.iniciarMediacion();
        assertEquals(EstadoDisputa.EN_MEDIACION, disputa.getEstado());
    }

    @Test
    void debeRechazarResolucionSinMediacion() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "Producto defectuoso");
        
        assertThrows(TransicionEstadoInvalidaException.class, () -> 
            disputa.resolver(Resolucion.FAVOR_COMPRADOR)
        );
    }

    @Test
    void debeResolverDisputaYGenerarEventoSiEsAFavorDelComprador() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "Producto defectuoso");
        disputa.iniciarMediacion();
        
        ReembolsoSolicitadoPorDisputaEvent evento = disputa.resolver(Resolucion.FAVOR_COMPRADOR);
        
        assertEquals(EstadoDisputa.RESUELTA, disputa.getEstado());
        assertEquals(Resolucion.FAVOR_COMPRADOR, disputa.getResolucion());
        assertNotNull(evento);
        assertEquals("ped1", evento.idPedido());
        assertEquals(disputa.getId(), evento.idDisputa());
    }

    @Test
    void debeResolverDisputaSinEventoSiNoEsAFavorDelComprador() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "Producto defectuoso");
        disputa.iniciarMediacion();
        
        ReembolsoSolicitadoPorDisputaEvent evento = disputa.resolver(Resolucion.FAVOR_VENDEDOR);
        
        assertEquals(EstadoDisputa.RESUELTA, disputa.getEstado());
        assertEquals(Resolucion.FAVOR_VENDEDOR, disputa.getResolucion());
        assertNull(evento);
    }
}
