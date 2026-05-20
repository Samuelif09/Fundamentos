package com.openlib.market.infrastructure.biblioteca;

import com.openlib.market.domain.biblioteca.IBibliotecaGateway;
import com.openlib.market.domain.biblioteca.LicenciaAcceso;
import org.springframework.stereotype.Component;

@Component
public class BibliotecaDummyGateway implements IBibliotecaGateway {

    @Override
    public boolean validarLicencia(LicenciaAcceso licencia) {
        // Simulamos que el usuario tiene licencia si el idLibro termina en par, 
        // o para pruebas simples, decimos que siempre tiene licencia a menos que 
        // el idLibro sea "unauthorized"
        if ("unauthorized".equalsIgnoreCase(licencia.getIdLibro())) {
            return false;
        }
        return true;
    }
}
