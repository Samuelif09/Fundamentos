package com.openlib.market.application.notificacion;

import com.openlib.market.domain.listadeseos.IListaDeseosGateway;
import com.openlib.market.domain.notificacionrebaja.INotificacionGateway;
import com.openlib.market.domain.notificacionrebaja.LibroCambioPrecioEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class RecibirNotificacionPrecioInteractorTest {

    private IListaDeseosGateway listaDeseosGateway;
    private INotificacionGateway notificacionGateway;
    private RecibirNotificacionPrecioInteractor interactor;

    @BeforeEach
    void setUp() {
        listaDeseosGateway = mock(IListaDeseosGateway.class);
        notificacionGateway = mock(INotificacionGateway.class);
        interactor = new RecibirNotificacionPrecioInteractor(listaDeseosGateway, notificacionGateway);
    }

    @Test
    void debeNotificarSiPrecioBajaYUsuariosTienenLibroEnLista() {
        when(listaDeseosGateway.obtenerUsuariosInteresados("libro-1")).thenReturn(List.of("u1", "u2"));

        LibroCambioPrecioEvent evento = new LibroCambioPrecioEvent("libro-1", 100.0, 80.0);
        interactor.manejarCambioPrecio(evento);

        verify(notificacionGateway).enviarAlertaPrecio("u1", "libro-1", 80.0);
        verify(notificacionGateway).enviarAlertaPrecio("u2", "libro-1", 80.0);
    }

    @Test
    void noDebeNotificarSiPrecioSubeOSeMantiene() {
        LibroCambioPrecioEvent eventoSube = new LibroCambioPrecioEvent("libro-1", 100.0, 120.0);
        interactor.manejarCambioPrecio(eventoSube);

        LibroCambioPrecioEvent eventoIgual = new LibroCambioPrecioEvent("libro-1", 100.0, 100.0);
        interactor.manejarCambioPrecio(eventoIgual);

        verify(listaDeseosGateway, never()).obtenerUsuariosInteresados(anyString());
        verify(notificacionGateway, never()).enviarAlertaPrecio(anyString(), anyString(), anyDouble());
    }

    @Test
    void noDebeNotificarSiNadieTieneElLibroEnLista() {
        when(listaDeseosGateway.obtenerUsuariosInteresados("libro-2")).thenReturn(List.of());

        LibroCambioPrecioEvent evento = new LibroCambioPrecioEvent("libro-2", 100.0, 80.0);
        interactor.manejarCambioPrecio(evento);

        verify(notificacionGateway, never()).enviarAlertaPrecio(anyString(), anyString(), anyDouble());
    }
}
