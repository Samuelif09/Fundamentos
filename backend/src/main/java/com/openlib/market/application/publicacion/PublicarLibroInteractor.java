package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;

import java.util.Optional;

public class PublicarLibroInteractor implements IPublicarLibroUseCase {

    private final ILibroPublicacionGateway libroGateway;
    private final IUsuarioGateway usuarioGateway;

    public PublicarLibroInteractor(ILibroPublicacionGateway libroGateway, IUsuarioGateway usuarioGateway) {
        this.libroGateway = libroGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public void publicar(PublicarLibroRequestDto request) {
        // 1. Validar que el vendedor existe y tiene rol VENDEDOR
        Optional<Usuario> usuarioOpt = usuarioGateway.buscarPorId(request.getIdVendedor());
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("El vendedor no existe.");
        }
        
        Usuario usuario = usuarioOpt.get();
        if (usuario.getRol() != RolUsuario.VENDEDOR) {
            throw new IllegalArgumentException("Solo los vendedores pueden publicar libros.");
        }

        // 2. Crear y validar Value Objects
        Isbn isbn = new Isbn(request.getIsbn());
        Precio precio = new Precio(request.getPrecio());

        // 3. Crear Entidad Libro
        Libro libro = new Libro(
                isbn,
                request.getTitulo(),
                request.getSinopsis(),
                precio,
                request.getUrlPortada(),
                request.getCategoria(),
                request.getIdVendedor()
        );

        // 4. Persistir
        libroGateway.guardar(libro);
    }
}
