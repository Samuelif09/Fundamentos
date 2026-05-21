package com.openlib.market.application.configuracion;

import java.util.List;

public interface IConfigurarComisionesUseCase {
    void configurarComision(String idCategoria, double porcentaje);
    ComisionDto obtenerComisionParaCategoria(String idCategoria);
    List<ComisionDto> listarComisiones();
}
