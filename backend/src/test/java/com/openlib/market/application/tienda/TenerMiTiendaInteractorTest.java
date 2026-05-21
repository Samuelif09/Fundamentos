package com.openlib.market.application.tienda;

import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TenerMiTiendaInteractorTest {

    private ITiendaVendedorGateway tiendaGateway;
    private IInventarioGateway inventarioGateway;
    private TenerMiTiendaInteractor interactor;

    @BeforeEach
    void setUp() {
        tiendaGateway = mock(ITiendaVendedorGateway.class);
        inventarioGateway = mock(IInventarioGateway.class);
        interactor = new TenerMiTiendaInteractor(tiendaGateway, inventarioGateway);
    }

    @Test
    void debeRetornarPerfilTiendaCompleto() {
        when(tiendaGateway.obtenerPerfilPorSlug("mi-libreria")).thenReturn(
                Optional.of(new ITiendaVendedorGateway.PerfilTiendaBase("v1", "Mi Librería", "mi-libreria", null))
        );
        when(inventarioGateway.listarPorVendedorId("v1")).thenReturn(List.of(
                new LibroCatalogo("isbn-1", "Libro 1", 10.0, "url")
        ));

        TiendaPublicaDto dto = interactor.obtenerTienda("mi-libreria");

        assertEquals("v1", dto.getIdVendedor());
        assertEquals("Mi Librería", dto.getNombreTienda());
        assertEquals("mi-libreria", dto.getUrlAmigable());
        assertEquals(1, dto.getLibros().size());
        assertEquals("Libro 1", dto.getLibros().get(0).titulo());
    }

    @Test
    void debeLanzarExcepcionSiTiendaNoExiste() {
        when(tiendaGateway.obtenerPerfilPorSlug("inexistente")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> interactor.obtenerTienda("inexistente"));
        verify(inventarioGateway, never()).listarPorVendedorId(anyString());
    }

    @Test
    void debeTransformarNombreDeTiendaASlugValidoParaVerificarDominio() {
        // En este test probamos que el Domain Object UrlAmigable funciona y limpia la cadena
        when(tiendaGateway.obtenerPerfilPorSlug("libreria-epica")).thenReturn(
                Optional.of(new ITiendaVendedorGateway.PerfilTiendaBase("v1", "Librería Épica @2023!", "libreria-epica", null))
        );

        TiendaPublicaDto dto = interactor.obtenerTienda("libreria-epica");

        // El nombre original tiene tildes y caracteres especiales, el slug resultante debe ser limpio
        assertEquals("libreria-epica-2023", dto.getUrlAmigable());
    }
}
