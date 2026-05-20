package com.openlib.market.application.curaduria;

import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.vendedor.IVendedorGateway;

import java.util.List;
import java.util.stream.Collectors;

public class RevisarCuraduriaContenidoInteractor implements IRevisarCuraduriaContenidoUseCase {

    private final ICuraduriaGateway curaduriaGateway;
    private final IVendedorGateway vendedorGateway;

    public RevisarCuraduriaContenidoInteractor(ICuraduriaGateway curaduriaGateway, IVendedorGateway vendedorGateway) {
        this.curaduriaGateway = curaduriaGateway;
        this.vendedorGateway = vendedorGateway;
    }

    @Override
    public List<LibroParaRevisionDto> listarLibrosPendientes(int page, int size) {
        List<Libro> pendientes = curaduriaGateway.listarPorEstado(EstadoLibro.EN_REVISION, page, size);

        return pendientes.stream().map(libro -> {
            String nombreVendedor = "Desconocido";
            String ruc = "N/A";
            var vendedorOpt = vendedorGateway.obtenerPorId(libro.getIdVendedor());
            if (vendedorOpt.isPresent()) {
                nombreVendedor = vendedorOpt.get().getRazonSocial().getValor();
                ruc = vendedorOpt.get().getIdentificacionTributaria().getValor();
            }
            return new LibroParaRevisionDto(
                    libro.getIsbn().getValor(),
                    libro.getTitulo(),
                    libro.getSinopsis(),
                    libro.getPrecio().getValor(),
                    libro.getUrlPortada(),
                    libro.getIdVendedor(),
                    nombreVendedor,
                    ruc
            );
        }).collect(Collectors.toList());
    }
}
