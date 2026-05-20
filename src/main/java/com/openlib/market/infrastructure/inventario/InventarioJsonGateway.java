package com.openlib.market.infrastructure.inventario;

import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.infrastructure.catalogo.CatalogoJsonGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventarioJsonGateway implements IInventarioGateway {

    private final CatalogoJsonGateway catalogoJsonGateway;

    public InventarioJsonGateway(CatalogoJsonGateway catalogoJsonGateway) {
        this.catalogoJsonGateway = catalogoJsonGateway;
    }

    @Override
    public List<LibroCatalogo> listarPorVendedorId(String idVendedor) {
        return catalogoJsonGateway.listarTodos().stream()
                .filter(l -> idVendedor.equals(l.idVendedor()))
                .map(l -> new LibroCatalogo(l.isbn(), l.titulo(), l.precio(), l.urlPortada()))
                .collect(Collectors.toList());
    }
}
