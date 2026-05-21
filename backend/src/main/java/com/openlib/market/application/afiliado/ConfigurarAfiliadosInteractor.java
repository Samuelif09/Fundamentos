package com.openlib.market.application.afiliado;

import com.openlib.market.domain.afiliado.*;

import java.util.Optional;

public class ConfigurarAfiliadosInteractor implements IConfigurarAfiliadosUseCase {

    private final IAfiliadoGateway afiliadoGateway;

    public ConfigurarAfiliadosInteractor(IAfiliadoGateway afiliadoGateway) {
        this.afiliadoGateway = afiliadoGateway;
    }

    @Override
    public String configurarYGenerarEnlace(ConfigurarAfiliadosRequestDto request) {
        Optional<ProgramaAfiliado> programaOpt = afiliadoGateway.obtenerProgramaPorVendedor(request.idVendedor());
        ProgramaAfiliado programa;

        if (programaOpt.isPresent()) {
            programa = programaOpt.get();
        } else {
            programa = new ProgramaAfiliado(request.idVendedor(), new PorcentajeComisionAfiliado(request.comision()));
            afiliadoGateway.guardarPrograma(programa);
        }

        EnlaceAfiliado enlace = new EnlaceAfiliado(request.idAfiliado(), request.idVendedor(), new CodigoRastreo());
        afiliadoGateway.guardarEnlace(enlace);

        return enlace.generarUrl();
    }
}
