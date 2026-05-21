package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.TipoFormato;

public record PublicarContenidoRequestDto(
        String isbn,
        String titulo,
        String sinopsis,
        Double precio,
        String urlPortada,
        String categoria,
        String idVendedor,
        TipoFormato tipoFormato,
        Integer duracionMinutos // Requerido para audiolibros y cursos
) {}
