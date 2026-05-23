package com.openlib.market.application.publicacion;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;

import java.util.Optional;

public class PublicarLibroInteractor implements IPublicarLibroUseCase {

    private final ILibroPublicacionGateway libroGateway;
    private final IUsuarioGateway usuarioGateway;
    private final IVendedorGateway vendedorGateway;

    public PublicarLibroInteractor(ILibroPublicacionGateway libroGateway,
                                   IUsuarioGateway usuarioGateway,
                                   IVendedorGateway vendedorGateway) {
        this.libroGateway = libroGateway;
        this.usuarioGateway = usuarioGateway;
        this.vendedorGateway = vendedorGateway;
    }

    @Override
    public void publicar(PublicarLibroRequestDto request) {
        // 1. Validar que el vendedor existe
        Optional<Vendedor> vendedorOpt = vendedorGateway.obtenerPorId(request.getIdVendedor());
        if (vendedorOpt.isEmpty()) {
            throw new IllegalArgumentException("El vendedor no existe.");
        }

        // 2. Validar que el usuario asociado al vendedor tiene rol VENDEDOR
        String idUsuario = vendedorOpt.get().getIdUsuario();
        Optional<Usuario> usuarioOpt = usuarioGateway.buscarPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("El usuario asociado al vendedor no existe.");
        }
        
        Usuario usuario = usuarioOpt.get();
        if (usuario.getRol() != RolUsuario.VENDEDOR) {
            throw new IllegalArgumentException("Solo los vendedores pueden publicar libros.");
        }

        // 3. Crear y validar Value Objects
        Isbn isbn = new Isbn(request.getIsbn());
        Precio precio = new Precio(request.getPrecio());

        // 4. Crear Entidad Libro
        Libro libro = new Libro(
                isbn,
                request.getTitulo(),
                request.getSinopsis(),
                precio,
                request.getUrlPortada(),
                request.getCategoria(),
                request.getIdVendedor()
        );

            // 5. Persistir
        libroGateway.guardar(libro);
    }
}
