package com.openlib.market.application.exportacion;

import com.openlib.market.domain.exportacion.DataExportadaUsuario;

public interface IExportarMiCuentaUseCase {
    DataExportadaUsuario exportar(String idUsuario);
}
