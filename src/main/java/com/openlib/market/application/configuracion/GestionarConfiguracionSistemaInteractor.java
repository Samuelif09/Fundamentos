package com.openlib.market.application.configuracion;

import com.openlib.market.domain.configuracion.ConfiguracionMetodoPago;
import com.openlib.market.domain.configuracion.IMetodoPagoConfigGateway;

import java.util.List;
import java.util.stream.Collectors;

public class GestionarConfiguracionSistemaInteractor implements IGestionarConfiguracionSistemaUseCase {

    private final IMetodoPagoConfigGateway configGateway;

    public GestionarConfiguracionSistemaInteractor(IMetodoPagoConfigGateway configGateway) {
        this.configGateway = configGateway;
    }

    @Override
    public void cambiarEstadoMetodoPago(String id, String estado) {
        ConfiguracionMetodoPago config = configGateway.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado"));

        if ("HABILITADO".equalsIgnoreCase(estado)) {
            config.habilitar();
        } else if ("DESHABILITADO".equalsIgnoreCase(estado)) {
            int activos = configGateway.contarMetodosHabilitados();
            config.deshabilitar(activos);
        } else {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }

        configGateway.actualizar(config);
    }

    @Override
    public List<MetodoPagoConfigDto> listarMetodosPago() {
        return configGateway.listarTodos().stream()
                .map(c -> new MetodoPagoConfigDto(c.getId(), c.getNombre().getValor(), c.getEstado().name()))
                .collect(Collectors.toList());
    }
}
