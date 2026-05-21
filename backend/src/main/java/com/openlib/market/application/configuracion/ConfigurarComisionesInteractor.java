package com.openlib.market.application.configuracion;

import com.openlib.market.domain.configuracion.IConfiguracionComisionGateway;
import com.openlib.market.domain.configuracion.ReglaComision;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigurarComisionesInteractor implements IConfigurarComisionesUseCase {

    private final IConfiguracionComisionGateway comisionGateway;

    public ConfigurarComisionesInteractor(IConfiguracionComisionGateway comisionGateway) {
        this.comisionGateway = comisionGateway;
    }

    @Override
    public void configurarComision(String idCategoria, double porcentaje) {
        ReglaComision regla = new ReglaComision(idCategoria, porcentaje);
        comisionGateway.guardarRegla(regla);
    }

    @Override
    public ComisionDto obtenerComisionParaCategoria(String idCategoria) {
        ReglaComision regla = comisionGateway.obtenerRegla(idCategoria);
        if (regla == null) {
            regla = comisionGateway.obtenerRegla("GLOBAL");
        }
        
        if (regla == null) {
            throw new IllegalStateException("No existe regla de comisión GLOBAL configurada");
        }

        return new ComisionDto(regla.getIdCategoria(), regla.getPorcentajeComision());
    }

    @Override
    public List<ComisionDto> listarComisiones() {
        return comisionGateway.listarTodas().stream()
                .map(r -> new ComisionDto(r.getIdCategoria(), r.getPorcentajeComision()))
                .collect(Collectors.toList());
    }
}
