package com.openlib.market.application.autenticacion;

import com.openlib.market.domain.autenticacion.Administrador;
import com.openlib.market.domain.autenticacion.IAdminGateway;
import com.openlib.market.domain.autenticacion.NombreRolAdmin;
import com.openlib.market.domain.autenticacion.RolAdmin;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GestionarRolesInteractor implements IGestionarRolesUseCase {

    private final IAdminGateway adminGateway;

    public GestionarRolesInteractor(IAdminGateway adminGateway) {
        this.adminGateway = adminGateway;
    }

    @Override
    public void asignarRol(String adminId, String nombreRolStr, List<String> permisos) {
        Administrador admin = adminGateway.buscarPorId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));

        NombreRolAdmin nombreRol = NombreRolAdmin.valueOf(nombreRolStr.toUpperCase());
        admin.asignarRol(new RolAdmin(nombreRol, permisos));

        adminGateway.guardar(admin);
    }

    @Override
    public void removerRol(String adminId, String nombreRolStr) {
        Administrador admin = adminGateway.buscarPorId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));

        NombreRolAdmin nombreRol = NombreRolAdmin.valueOf(nombreRolStr.toUpperCase());
        int totalSuperAdmins = adminGateway.contarSuperAdmins();

        admin.removerRol(nombreRol, totalSuperAdmins);

        adminGateway.guardar(admin);
    }
}
