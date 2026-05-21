package com.openlib.market.application.inventario;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VerInventarioInteractor implements IVerInventarioUseCase {

    private final IInventarioGateway inventarioGateway;

    public VerInventarioInteractor(IInventarioGateway inventarioGateway) {
        this.inventarioGateway = inventarioGateway;
    }

    @Override
    public List<LibroInventarioDto> listarPorVendedor(String idVendedor) {
        if (idVendedor == null || idVendedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del vendedor es obligatorio");
        }

        List<LibroCatalogo> libros = inventarioGateway.listarPorVendedorId(idVendedor);

        return libros.stream()
                .map(l -> new LibroInventarioDto(l.isbn(), l.titulo(), l.precio(), null, idVendedor))
                .collect(Collectors.toList());
    }
}
