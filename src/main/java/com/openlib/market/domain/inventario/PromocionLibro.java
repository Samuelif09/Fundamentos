package com.openlib.market.domain.inventario;

import java.util.UUID;

public class PromocionLibro {
    private final String id;
    private final String isbn;
    private final PorcentajeDescuento descuento;
    private final PeriodoPromocion periodo;

    public PromocionLibro(String isbn, PorcentajeDescuento descuento, PeriodoPromocion periodo) {
        this(UUID.randomUUID().toString(), isbn, descuento, periodo);
    }

    public PromocionLibro(String id, String isbn, PorcentajeDescuento descuento, PeriodoPromocion periodo) {
        if (isbn == null || isbn.trim().isEmpty() || descuento == null || periodo == null) {
            throw new IllegalArgumentException("Datos de la promoción incompletos");
        }
        this.id = id;
        this.isbn = isbn;
        this.descuento = descuento;
        this.periodo = periodo;
    }

    public String getId() { return id; }
    public String getIsbn() { return isbn; }
    public PorcentajeDescuento getDescuento() { return descuento; }
    public PeriodoPromocion getPeriodo() { return periodo; }
}
