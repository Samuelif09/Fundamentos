package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarSoporteInteractorTest {

    private IDisputaGateway disputaGateway;
    private IDisputaEventPublisher eventPublisher;
    private GestionarSoporteInteractor interactor;

    @BeforeEach
    void setUp() {
        disputaGateway = mock(IDisputaGateway.class);
        eventPublisher = mock(IDisputaEventPublisher.class);
        interactor = new GestionarSoporteInteractor(disputaGateway, eventPublisher);
    }

    @Test
    void debeIniciarMediacion() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "motivo");
        when(disputaGateway.buscarPorId(disputa.getId())).thenReturn(Optional.of(disputa));

        DisputaDto dto = interactor.iniciarMediacion(disputa.getId());

        assertEquals(EstadoDisputa.EN_MEDIACION.name(), dto.getEstado());
        verify(disputaGateway).guardar(disputa);
    }

    @Test
    void debeResolverYPublicarEvento() {
        Disputa disputa = new Disputa("ped1", "comp1", "vend1", "motivo");
        disputa.iniciarMediacion();
        when(disputaGateway.buscarPorId(disputa.getId())).thenReturn(Optional.of(disputa));

        DisputaDto dto = interactor.resolverDisputa(disputa.getId(), "FAVOR_COMPRADOR");

        assertEquals(EstadoDisputa.RESUELTA.name(), dto.getEstado());
        assertEquals(Resolucion.FAVOR_COMPRADOR.name(), dto.getResolucion());
        
        verify(disputaGateway).guardar(disputa);
        verify(eventPublisher).publicar(any(ReembolsoSolicitadoPorDisputaEvent.class));
    }
}
