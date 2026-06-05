package com.openlib.market.application.inventario;

import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.shared.ReglaNegocioInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class AbastecerInventarioInteractorTest {

    private IInventarioGateway inventarioGateway;
    private AbastecerInventarioInteractor interactor;

    @BeforeEach
    void setUp() {
        inventarioGateway = mock(IInventarioGateway.class);
        interactor = new AbastecerInventarioInteractor(inventarioGateway);
    }

    @Test
    void debeLanzarExcepcionSiCantidadEsCeroONegativa() {
        assertThrows(ReglaNegocioInvalidaException.class, () -> interactor.ejecutar("123", 0));
        assertThrows(ReglaNegocioInvalidaException.class, () -> interactor.ejecutar("123", -5));

        verify(inventarioGateway, never()).agregarStock(anyString(), anyInt());
    }

    @Test
    void debeAbastecerInventarioExitosamente() {
        assertDoesNotThrow(() -> interactor.ejecutar("123", 10));

        verify(inventarioGateway, times(1)).agregarStock("123", 10);
    }
}
