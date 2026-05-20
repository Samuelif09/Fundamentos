package com.openlib.market.domain.configuracion;

import java.util.List;

public interface IConfiguracionComisionGateway {
    ReglaComision obtenerRegla(String idCategoria);
    void guardarRegla(ReglaComision regla);
    List<ReglaComision> listarTodas();
}
