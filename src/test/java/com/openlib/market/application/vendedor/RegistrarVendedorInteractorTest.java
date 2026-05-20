package com.openlib.market.application.vendedor;

import com.openlib.market.domain.registro.EmailDuplicadoException;
import com.openlib.market.domain.registro.IPasswordEncoderGateway;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegistrarVendedorInteractorTest {

    private IRegistroGateway registroGateway;
    private IVendedorGateway vendedorGateway;
    private IPasswordEncoderGateway passwordEncoder;
    private RegistrarVendedorInteractor interactor;

    @BeforeEach
    void setUp() {
        registroGateway = mock(IRegistroGateway.class);
        vendedorGateway = mock(IVendedorGateway.class);
        passwordEncoder = mock(IPasswordEncoderGateway.class);
        interactor = new RegistrarVendedorInteractor(registroGateway, vendedorGateway, passwordEncoder);
    }

    @Test
    void debeRegistrarVendedorExitosamente() {
        when(vendedorGateway.existePorIdentificacionTributaria(anyString())).thenReturn(false);
        when(registroGateway.existeEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-pass");

        RegistrarVendedorRequestDto request = new RegistrarVendedorRequestDto(
                "Mi Tienda", "tienda@test.com", "Pass123!", "Mi Tienda S.A.", "NIT-123456"
        );

        interactor.registrar(request);

        verify(registroGateway, times(1)).guardar(any(Usuario.class));
        verify(vendedorGateway, times(1)).guardar(any(Vendedor.class));
    }

    @Test
    void debeFallarSiIdentificacionTributariaDuplicada() {
        when(vendedorGateway.existePorIdentificacionTributaria(anyString())).thenReturn(true);

        RegistrarVendedorRequestDto request = new RegistrarVendedorRequestDto(
                "Mi Tienda", "tienda@test.com", "Pass123!", "Mi Tienda S.A.", "NIT-123456"
        );

        assertThrows(IllegalArgumentException.class, () -> interactor.registrar(request));
        
        verify(registroGateway, never()).guardar(any(Usuario.class));
        verify(vendedorGateway, never()).guardar(any(Vendedor.class));
    }

    @Test
    void debeFallarSiEmailDuplicado() {
        when(vendedorGateway.existePorIdentificacionTributaria(anyString())).thenReturn(false);
        when(registroGateway.existeEmail(any())).thenReturn(true);

        RegistrarVendedorRequestDto request = new RegistrarVendedorRequestDto(
                "Mi Tienda", "tienda@test.com", "Pass123!", "Mi Tienda S.A.", "NIT-123456"
        );

        assertThrows(EmailDuplicadoException.class, () -> interactor.registrar(request));
        
        verify(registroGateway, never()).guardar(any(Usuario.class));
        verify(vendedorGateway, never()).guardar(any(Vendedor.class));
    }
}
