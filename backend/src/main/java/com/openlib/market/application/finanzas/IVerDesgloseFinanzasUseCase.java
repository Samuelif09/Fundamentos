package com.openlib.market.application.finanzas;

import java.util.List;

public interface IVerDesgloseFinanzasUseCase {
    List<DesgloseFinancieroDto> obtenerDesglose(String idVendedor);
}
