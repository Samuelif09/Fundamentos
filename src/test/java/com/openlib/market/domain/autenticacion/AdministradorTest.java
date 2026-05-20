package com.openlib.market.domain.autenticacion;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdministradorTest {

    @Test
    void debeAsignarNuevoRol() {
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN);
        admin.asignarRol(new RolAdmin(NombreRolAdmin.MODERADOR, List.of("LEER_FORO")));

        assertEquals(1, admin.getRoles().size());
        assertEquals(NombreRolAdmin.MODERADOR, admin.getRoles().get(0).getNombre());
    }

    @Test
    void debeRemoverRolSiNoEsUltimoSuperAdmin() {
        RolAdmin superAdmin = new RolAdmin(NombreRolAdmin.SUPERADMIN, List.of());
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN, List.of(superAdmin));

        admin.removerRol(NombreRolAdmin.SUPERADMIN, 2); // Hay 2 super admins en total
        
        assertTrue(admin.getRoles().isEmpty());
    }

    @Test
    void debeLanzarExcepcionSiEsUltimoSuperAdmin() {
        RolAdmin superAdmin = new RolAdmin(NombreRolAdmin.SUPERADMIN, List.of());
        Administrador admin = new Administrador("a1", new Email("admin@test.com"), "hash", Rol.ROLE_ADMIN, List.of(superAdmin));

        assertThrows(ValidacionJerarquiaException.class, () -> 
            admin.removerRol(NombreRolAdmin.SUPERADMIN, 1) // Es el único en el sistema
        );
    }
}
