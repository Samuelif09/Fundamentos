package com.openlib.market.infrastructure.ventavendedor;

import com.openlib.market.domain.ventavendedor.IDetalleLibroGateway;
import com.openlib.market.infrastructure.catalogo.CatalogoJsonGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DetalleLibroJsonGateway implements IDetalleLibroGateway {

    private final CatalogoJsonGateway catalogoJsonGateway;

    public DetalleLibroJsonGateway(CatalogoJsonGateway catalogoJsonGateway) {
        this.catalogoJsonGateway = catalogoJsonGateway;
    }

    @Override
    public Optional<String> obtenerIdVendedorPorIsbn(String isbn) {
        return catalogoJsonGateway.listarTodos().stream()
                .filter(dto -> isbn.equals(dto.isbn()))
                .map(CatalogoJsonGateway.LibroDto::idVendedor)
                .findFirst();
    }
}
