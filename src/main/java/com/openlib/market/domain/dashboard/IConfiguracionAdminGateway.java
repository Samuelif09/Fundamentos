package com.openlib.market.domain.dashboard;

import java.util.Optional;

public interface IConfiguracionAdminGateway {
    Optional<ConfiguracionDashboard> buscarPorAdminId(String idAdmin);
    void guardar(ConfiguracionDashboard configuracion);
}
