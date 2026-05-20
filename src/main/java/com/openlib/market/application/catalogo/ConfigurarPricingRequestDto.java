package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.EstrategiaCompetencia;

public record ConfigurarPricingRequestDto(
        String isbn,
        String idVendedor,
        double precioMinimo,
        double precioMaximo,
        EstrategiaCompetencia estrategia
) {}
