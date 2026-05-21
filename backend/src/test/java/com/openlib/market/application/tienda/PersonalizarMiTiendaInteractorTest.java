package com.openlib.market.application.tienda;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.almacenamiento.ArchivoInvalidoException;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonalizarMiTiendaInteractorTest {

    private IAlmacenamientoGateway almacenamientoGateway;
    private ITiendaVendedorGateway tiendaGateway;
    private PersonalizarMiTiendaInteractor interactor;

    @BeforeEach
    void setUp() {
        almacenamientoGateway = mock(IAlmacenamientoGateway.class);
        tiendaGateway = mock(ITiendaVendedorGateway.class);
        interactor = new PersonalizarMiTiendaInteractor(almacenamientoGateway, tiendaGateway);
    }

    @Test
    void debeSubirBannerExitosamente() {
        byte[] img = new byte[100];
        when(almacenamientoGateway.guardar(any(ArchivoImagen.class), eq("banner_seller-1")))
                .thenReturn("http://cdn/banners/banner_seller-1.jpg");

        interactor.subirBanner("seller-1", img, "image/jpeg", "banner.jpg");

        verify(tiendaGateway).actualizarBanner("seller-1", "http://cdn/banners/banner_seller-1.jpg");
    }

    @Test
    void debeRechazarArchivoVacio() {
        assertThrows(IllegalArgumentException.class, () ->
                interactor.subirBanner("seller-1", new byte[0], "image/jpeg", "banner.jpg")
        );
        verifyNoInteractions(almacenamientoGateway, tiendaGateway);
    }

    @Test
    void debeRechazarMimeInvalido() {
        byte[] exe = new byte[100];
        assertThrows(ArchivoInvalidoException.class, () ->
                interactor.subirBanner("seller-1", exe, "application/x-msdownload", "virus.exe")
        );
    }
}
