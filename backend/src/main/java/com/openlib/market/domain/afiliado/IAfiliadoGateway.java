package com.openlib.market.domain.afiliado;

import java.util.Optional;

public interface IAfiliadoGateway {
    void guardarPrograma(ProgramaAfiliado programa);
    void guardarEnlace(EnlaceAfiliado enlace);
    Optional<ProgramaAfiliado> obtenerProgramaPorVendedor(String idVendedor);
}
