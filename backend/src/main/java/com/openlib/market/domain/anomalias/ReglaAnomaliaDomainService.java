package com.openlib.market.domain.anomalias;

import java.util.Optional;

import org.springframework.stereotype.Service;

public class ReglaAnomaliaDomainService {
    
    public Optional<Alerta> evaluarRegla(ReglaAnomalia regla, double valorActual) {
        if (regla.evaluar(valorActual)) {
            return Optional.of(new Alerta(regla, valorActual));
        }
        return Optional.empty();
    }
}
