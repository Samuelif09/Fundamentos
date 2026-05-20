package com.openlib.market.application.registro;

import com.openlib.market.domain.registro.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrarRegistroInteractorTest {

    private IRegistroGateway registroGateway;
    private IPasswordEncoderGateway passwordEncoder;
    private RegistrarRegistroInteractor interactor;

    @BeforeEach
    void setUp() {
        registroGateway = mock(IRegistroGateway.class);
        passwordEncoder = mock(IPasswordEncoderGateway.class);
        interactor = new RegistrarRegistroInteractor(registroGateway, passwordEncoder);
    }

    @Test
    void debeRegistrarUsuarioYEncriptarPassword() {
        RegistroRequestDto request = new RegistroRequestDto("Juan", "juan@test.com", "Secret123!");
        when(registroGateway.existeEmail(any(Email.class))).thenReturn(false);
        when(passwordEncoder.encode("Secret123!")).thenReturn("hashed-secret");

        interactor.registrar(request);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(registroGateway).guardar(captor.capture());
        
        Usuario guardado = captor.getValue();
        assertEquals("hashed-secret", guardado.getPassword().getValor());
    }

    @Test
    void debeLanzarExcepcionSiEmailEstaDuplicado() {
        RegistroRequestDto request = new RegistroRequestDto("Juan", "juan@test.com", "Secret123!");
        when(registroGateway.existeEmail(any(Email.class))).thenReturn(true);

        assertThrows(EmailDuplicadoException.class, () -> interactor.registrar(request));
        verify(registroGateway, never()).guardar(any());
    }
}
