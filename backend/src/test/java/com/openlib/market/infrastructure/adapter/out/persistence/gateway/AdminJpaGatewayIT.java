package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.Administrador;
import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.NombreRolAdmin;
import com.openlib.market.domain.autenticacion.Rol;
import com.openlib.market.domain.autenticacion.RolAdmin;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.AdminMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AdminJpaGateway.class, AdminMapper.class})
@Transactional
class AdminJpaGatewayIT {

    @Autowired
    private AdminJpaGateway gateway;

    @Test
    void debeGuardarYRecuperarAdminConRoles() {
        Administrador admin = new Administrador("admin-1", new Email("admin@test.com"), "hash123", Rol.ROLE_ADMIN);
        admin.asignarRol(new RolAdmin(NombreRolAdmin.SUPERADMIN, List.of("ALL")));
        admin.asignarRol(new RolAdmin(NombreRolAdmin.FINANCIERO, List.of("READ_FINANCE")));

        gateway.guardar(admin);

        Optional<Administrador> recuperado = gateway.buscarPorEmail(new Email("admin@test.com"));
        assertTrue(recuperado.isPresent());
        assertEquals("admin-1", recuperado.get().getId());
        assertEquals(2, recuperado.get().getRoles().size());
        assertTrue(recuperado.get().esSuperAdmin());

        int count = gateway.contarSuperAdmins();
        assertEquals(1, count);
    }
}
