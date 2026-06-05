package com.openlib.market.application.wishlist;

import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import com.openlib.market.domain.wishlist.ListaDeseos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WishlistInteractorsTest {

    private IListaDeseosGateway wishlistGateway;
    private AgregarAWishlistInteractor agregarInteractor;
    private RemoverDeWishlistInteractor removerInteractor;
    private VerWishlistInteractor verInteractor;

    @BeforeEach
    void setUp() {
        wishlistGateway = mock(IListaDeseosGateway.class);
        agregarInteractor = new AgregarAWishlistInteractor(wishlistGateway);
        removerInteractor = new RemoverDeWishlistInteractor(wishlistGateway);
        verInteractor = new VerWishlistInteractor(wishlistGateway);
    }

    @Test
    void debeAgregarIsbnNuevoyEvitarDuplicados() {
        String idUsuario = "u1";
        ListaDeseos listaExistente = new ListaDeseos(idUsuario);
        listaExistente.agregarItem("isbn1");

        when(wishlistGateway.obtenerPorUsuario(idUsuario)).thenReturn(Optional.of(listaExistente));

        // Act: add existing
        agregarInteractor.ejecutar(idUsuario, "isbn1");
        // Act: add new
        agregarInteractor.ejecutar(idUsuario, "isbn2");

        ArgumentCaptor<ListaDeseos> captor = ArgumentCaptor.forClass(ListaDeseos.class);
        verify(wishlistGateway, times(2)).guardar(captor.capture());

        ListaDeseos saved = captor.getValue();
        assertEquals(2, saved.getIsbns().size(), "Deberia haber solo 2 isbns sin duplicados");
        assertTrue(saved.getIsbns().contains("isbn1"));
        assertTrue(saved.getIsbns().contains("isbn2"));
    }

    @Test
    void debeRemoverIsbn() {
        String idUsuario = "u1";
        ListaDeseos listaExistente = new ListaDeseos(idUsuario);
        listaExistente.agregarItem("isbn1");
        listaExistente.agregarItem("isbn2");

        when(wishlistGateway.obtenerPorUsuario(idUsuario)).thenReturn(Optional.of(listaExistente));

        removerInteractor.ejecutar(idUsuario, "isbn1");

        ArgumentCaptor<ListaDeseos> captor = ArgumentCaptor.forClass(ListaDeseos.class);
        verify(wishlistGateway).guardar(captor.capture());

        ListaDeseos saved = captor.getValue();
        assertEquals(1, saved.getIsbns().size());
        assertTrue(saved.getIsbns().contains("isbn2"));
    }

    @Test
    void debeDevolverWishlist() {
        String idUsuario = "u1";
        ListaDeseos listaExistente = new ListaDeseos(idUsuario);
        listaExistente.agregarItem("isbn1");

        when(wishlistGateway.obtenerPorUsuario(idUsuario)).thenReturn(Optional.of(listaExistente));

        WishlistResponseDto response = verInteractor.ejecutar(idUsuario);

        assertEquals("u1", response.getIdUsuario());
        assertEquals(1, response.getIsbns().size());
        assertTrue(response.getIsbns().contains("isbn1"));
    }
}
