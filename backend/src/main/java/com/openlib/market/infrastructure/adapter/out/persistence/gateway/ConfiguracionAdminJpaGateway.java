package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.dashboard.ConfiguracionDashboard;
import com.openlib.market.domain.dashboard.IConfiguracionAdminGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ConfiguracionDashboardMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ConfiguracionDashboardRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class ConfiguracionAdminJpaGateway implements IConfiguracionAdminGateway {

    private final ConfiguracionDashboardRepository repository;
    private final ConfiguracionDashboardMapper mapper;

    public ConfiguracionAdminJpaGateway(ConfiguracionDashboardRepository repository, ConfiguracionDashboardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ConfiguracionDashboard> buscarPorAdminId(String idAdmin) {
        return repository.findById(idAdmin).map(mapper::toDomain);
    }

    @Override
    public void guardar(ConfiguracionDashboard configuracion) {
        repository.save(mapper.toEntity(configuracion));
        repository.flush();
    }
}
