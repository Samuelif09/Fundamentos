package com.openlib.market.application.finanzas;

public record GenerarFacturaRequestDto(
        String idPedido,
        String idVendedor,
        String identificacionTributariaVendedor,
        String razonSocialVendedor,
        String idComprador,
        String nombreComprador,
        String correoComprador,
        double subtotalPedido
) {}
