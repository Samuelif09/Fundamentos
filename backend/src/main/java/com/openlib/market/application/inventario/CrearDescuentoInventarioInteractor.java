package com.openlib.market.application.inventario;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.inventario.IPromocionGateway;
import com.openlib.market.domain.inventario.PeriodoPromocion;
import com.openlib.market.domain.inventario.PorcentajeDescuento;
import com.openlib.market.domain.inventario.PromocionLibro;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CrearDescuentoInventarioInteractor implements ICrearDescuentoInventarioUseCase {

    private final ILibroPublicacionGateway libroGateway;
    private final IPromocionGateway promocionGateway;

    public CrearDescuentoInventarioInteractor(ILibroPublicacionGateway libroGateway, IPromocionGateway promocionGateway) {
        this.libroGateway = libroGateway;
        this.promocionGateway = promocionGateway;
    }

    @Override
    public void crearDescuento(String idVendedor, String isbn, int porcentaje, LocalDate fechaInicio, LocalDate fechaFin) {
        Optional<Libro> libroOpt = libroGateway.obtenerPorIsbn(isbn);
        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("Libro no encontrado");
        }

        if (!libroOpt.get().getIdVendedor().equals(idVendedor)) {
            throw new IllegalStateException("El vendedor no tiene permisos para crear promociones de este libro");
        }

        PeriodoPromocion nuevoPeriodo = new PeriodoPromocion(fechaInicio, fechaFin);
        PorcentajeDescuento nuevoDescuento = new PorcentajeDescuento(porcentaje);

        List<PromocionLibro> promocionesExistentes = promocionGateway.obtenerPorIsbn(isbn);
        for (PromocionLibro promo : promocionesExistentes) {
            if (nuevoPeriodo.seSolapaCon(promo.getPeriodo())) {
                throw new IllegalStateException("El periodo de promoción se solapa con una promoción existente");
            }
        }

        PromocionLibro nuevaPromocion = new PromocionLibro(isbn, nuevoDescuento, nuevoPeriodo);
        promocionGateway.guardar(nuevaPromocion);
    }
}
