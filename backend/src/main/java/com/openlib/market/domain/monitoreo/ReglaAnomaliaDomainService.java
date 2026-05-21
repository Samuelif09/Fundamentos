package com.openlib.market.domain.monitoreo;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service("monitoreoReglaAnomaliaDomainService")
public class ReglaAnomaliaDomainService {
    
    public Optional<Alerta> evaluar(ReglaAnomalia regla, double valorActual) {
        if (!regla.isActiva()) {
            return Optional.empty();
        }

        if (valorActual > regla.getUmbral().valor()) {
            return Optional.of(new Alerta(regla.getId(), valorActual));
        }

        return Optional.empty();
    }
}
