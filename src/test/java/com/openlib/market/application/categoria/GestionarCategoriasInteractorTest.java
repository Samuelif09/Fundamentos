package com.openlib.market.application.categoria;

import com.openlib.market.domain.categoria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarCategoriasInteractorTest {

    private ICategoriaGateway categoriaGateway;
    private GestionarCategoriasInteractor interactor;

    @BeforeEach
    void setUp() {
        categoriaGateway = mock(ICategoriaGateway.class);
        interactor = new GestionarCategoriasInteractor(categoriaGateway);
    }

    @Test
    void debeCrearCategoriaExitosamente() {
        when(categoriaGateway.existePorNombreNormalizado("ciencia ficción")).thenReturn(false);

        CategoriaCatalogo resultado = interactor.crearCategoria("Ciencia Ficción");

        assertNotNull(resultado.getId());
        assertEquals("Ciencia ficción", resultado.getNombre().getValor());
        verify(categoriaGateway).guardar(any(CategoriaCatalogo.class));
    }

    @Test
    void debeLanzarExcepcionSiCategoriaDuplicada() {
        when(categoriaGateway.existePorNombreNormalizado("fantasía")).thenReturn(true);

        assertThrows(CategoriaDuplicadaException.class,
                () -> interactor.crearCategoria(" fAntasÍa  "));
    }

    @Test
    void debeEditarNombreDeCategoria() {
        CategoriaCatalogo cat = new CategoriaCatalogo("cat1", new NombreCategoria("Terror"), EstadoCategoria.ACTIVA);
        when(categoriaGateway.obtenerPorId("cat1")).thenReturn(Optional.of(cat));
        when(categoriaGateway.existePorNombreNormalizado("suspenso")).thenReturn(false);

        CategoriaCatalogo editada = interactor.editarCategoria("cat1", "Suspenso");

        assertEquals("Suspenso", editada.getNombre().getValor());
        verify(categoriaGateway).actualizar(cat);
    }

    @Test
    void debeCambiarEstadoDeCategoria() {
        CategoriaCatalogo cat = new CategoriaCatalogo("cat1", new NombreCategoria("Terror"), EstadoCategoria.ACTIVA);
        when(categoriaGateway.obtenerPorId("cat1")).thenReturn(Optional.of(cat));

        interactor.cambiarEstado("cat1", "INACTIVA");

        assertEquals(EstadoCategoria.INACTIVA, cat.getEstado());
        verify(categoriaGateway).actualizar(cat);
    }
}
