package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.Administrador;
import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.NombreRolAdmin;
import com.openlib.market.domain.autenticacion.Rol;
import com.openlib.market.domain.autenticacion.RolAdmin;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class AdminJpaGatewayTest {

    @Autowired
    private AdminJpaGateway adminJpaGateway;

    @Test
    public void testGuardarYBuscarAdmin() {
        Administrador admin = new Administrador("admin-123", new Email("admin@test.com"), "hash123", Rol.ROLE_ADMIN);
        admin.asignarRol(new RolAdmin(NombreRolAdmin.SUPERADMIN, java.util.List.of()));
        admin.asignarRol(new RolAdmin(NombreRolAdmin.MODERADOR, java.util.List.of()));
        
        adminJpaGateway.guardar(admin);

        Optional<Administrador> guardado = adminJpaGateway.buscarPorId("admin-123");
        
        assertTrue(guardado.isPresent());
        assertEquals("admin@test.com", guardado.get().getEmail().getDireccion());
        assertEquals(2, guardado.get().getRoles().size());
        assertTrue(guardado.get().esSuperAdmin());
    }

    @Test
    public void testContarSuperAdmins() {
        Administrador admin1 = new Administrador("super-1", new Email("super1@test.com"), "hash1", Rol.ROLE_ADMIN);
        admin1.asignarRol(new RolAdmin(NombreRolAdmin.SUPERADMIN, java.util.List.of()));
        adminJpaGateway.guardar(admin1);

        Administrador admin2 = new Administrador("mod-1", new Email("mod@test.com"), "hash2", Rol.ROLE_ADMIN);
        admin2.asignarRol(new RolAdmin(NombreRolAdmin.MODERADOR, java.util.List.of()));
        adminJpaGateway.guardar(admin2);

        Administrador admin3 = new Administrador("super-2", new Email("super2@test.com"), "hash3", Rol.ROLE_ADMIN);
        admin3.asignarRol(new RolAdmin(NombreRolAdmin.SUPERADMIN, java.util.List.of()));
        adminJpaGateway.guardar(admin3);

        int count = adminJpaGateway.contarSuperAdmins();
        assertEquals(2, count);
    }

    @Test
    public void testBuscarPorEmailAdmin() {
        Administrador admin = new Administrador("admin-456", new Email("buscar@test.com"), "hash", Rol.ROLE_ADMIN);
        adminJpaGateway.guardar(admin);

        Optional<Administrador> encontrado = adminJpaGateway.buscarPorEmail(new Email("buscar@test.com"));
        assertTrue(encontrado.isPresent());
        assertEquals("admin-456", encontrado.get().getId());
    }
}
