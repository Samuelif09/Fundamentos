package com.openlib.market.infrastructure.biblioteca;

import com.openlib.market.domain.biblioteca.IBibliotecaGateway;
import com.openlib.market.domain.biblioteca.LicenciaAcceso;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BibliotecaJpaGateway implements IBibliotecaGateway {

    private final PedidoRepository pedidoRepository;
    private final ContenidoDigitalRepository contenidoRepository;

    private final com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository usuarioRepository;

    public BibliotecaJpaGateway(PedidoRepository pedidoRepository, ContenidoDigitalRepository contenidoRepository,
                                com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.contenidoRepository = contenidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean validarLicencia(LicenciaAcceso licencia) {
        List<String> isbns = pedidoRepository.findLibrosCompradosPorUsuario(licencia.getIdUsuario());
        return isbns.contains(licencia.getIdLibro());
    }

    @Override
    public List<LibroCatalogo> listarLibrosComprados(String idUsuario) {
        List<String> isbns = pedidoRepository.findLibrosCompradosPorUsuario(idUsuario);
        List<ContenidoDigitalEntity> libros = contenidoRepository.findAllById(isbns);
        return libros.stream().map(l -> {
            String autor = "Desconocido";
            if (l.getIdVendedor() != null) {
                autor = usuarioRepository.findById(l.getIdVendedor())
                        .map(com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity::getNombre)
                        .orElse("Desconocido");
            }
            return new LibroCatalogo(
                    l.getIsbn(),
                    l.getTitulo(),
                    autor,
                    l.getPrecio(),
                    l.getUrlPortada()
            );
        }).collect(Collectors.toList());
    }
}
