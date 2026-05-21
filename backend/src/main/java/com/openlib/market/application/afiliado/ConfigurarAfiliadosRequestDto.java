package com.openlib.market.application.afiliado;

public record ConfigurarAfiliadosRequestDto(
        String idVendedor,
        String idAfiliado,
        double comision
) {}
