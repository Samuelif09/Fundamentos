package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionarRolesInteractorTest {

    private IAdminGateway adminGateway;
    private GestionarRolesInteractor interactor;

    @BeforeEach
    void setUp() {
        adminGateway = mock(IAdminGateway.class);
        interactor = new GestionarRolesInteractor(adminGateway);
    }

    @Test
    void debeAsignarRolCorrectamente() {
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN);
        when(adminGateway.buscarPorId("a1")).thenReturn(Optional.of(admin));

        interactor.asignarRol("a1", "MODERADOR", List.of("PERMISO_X"));

        assertEquals(1, admin.getRoles().size());
        assertEquals(NombreRolAdmin.MODERADOR, admin.getRoles().get(0).getNombre());
        verify(adminGateway).guardar(admin);
    }

    @Test
    void debeRemoverRolSiHayMasDeUnSuperAdmin() {
        RolAdmin superAdmin = new RolAdmin(NombreRolAdmin.SUPERADMIN, List.of());
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN, List.of(superAdmin));
        
        when(adminGateway.buscarPorId("a1")).thenReturn(Optional.of(admin));
        when(adminGateway.contarSuperAdmins()).thenReturn(2);

        interactor.removerRol("a1", "SUPERADMIN");

        assertTrue(admin.getRoles().isEmpty());
        verify(adminGateway).guardar(admin);
    }

    @Test
    void debeBloquearRemocionSiEsElUltimoSuperAdmin() {
        RolAdmin superAdmin = new RolAdmin(NombreRolAdmin.SUPERADMIN, List.of());
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN, List.of(superAdmin));
        
        when(adminGateway.buscarPorId("a1")).thenReturn(Optional.of(admin));
        when(adminGateway.contarSuperAdmins()).thenReturn(1);

        assertThrows(ValidacionJerarquiaException.class, () -> 
            interactor.removerRol("a1", "SUPERADMIN")
        );
        
        verify(adminGateway, never()).guardar(any());
    }
}
