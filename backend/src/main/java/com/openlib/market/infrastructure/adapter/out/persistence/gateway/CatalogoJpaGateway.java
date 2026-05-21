package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.catalogo.CriterioBusqueda;
import com.openlib.market.domain.catalogo.CriterioSimilitud;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.catalogo.PaginaDominio;
import com.openlib.market.domain.catalogo.Paginacion;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ContenidoDigitalMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class CatalogoJpaGateway implements ICatalogoGateway {

    private final ContenidoDigitalRepository repository;
    // Asumiremos que el mapper tiene un toCatalogoDomain(ContenidoDigitalEntity)
    // Pero LibroMapper lo tenía. Lo pasaremos a ContenidoDigitalMapper.
    // O podemos mapearlo manualmente aquí por simplicidad.
    // Implementaremos toCatalogoDomain en ContenidoDigitalMapper.

    public CatalogoJpaGateway(ContenidoDigitalRepository repository) {
        this.repository = repository;
    }

    @Override
    public PaginaDominio<LibroCatalogo> listarPaginado(Paginacion paginacion) {
        Pageable pageable = PageRequest.of(paginacion.getPaginaActual(), paginacion.getTamanoPagina());
        Page<ContenidoDigitalEntity> page = repository.findAll(pageable);
        
        List<LibroCatalogo> contenido = page.getContent().stream()
                .map(this::toCatalogoDomain)
                .collect(Collectors.toList());

        return new PaginaDominio<>(contenido, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    @Override
    public List<LibroCatalogo> buscarPorFiltros(CriterioBusqueda criterio) {
        Specification<ContenidoDigitalEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (criterio.tieneTitulo()) {
                predicates.add(cb.like(cb.lower(root.get("titulo")), "%" + criterio.getTitulo().toLowerCase() + "%"));
            }
            // Asumimos que autor no existe en ContenidoDigitalEntity por ahora, pero lo simulamos si existiera
            // o lo ignoramos si no está en el modelo (ContenidoDigitalEntity no tiene 'autor' en nuestro MVP, 
            // así que ignoraremos autor o usaremos idVendedor como aproximación).
            
            if (criterio.tieneCategoria()) {
                predicates.add(cb.equal(cb.lower(root.get("categoria")), criterio.getCategoria().toLowerCase()));
            }
            
            if (criterio.tieneRangoPrecio()) {
                predicates.add(cb.between(root.get("precio"), 
                    criterio.getRangoPrecio().getMin(), 
                    criterio.getRangoPrecio().getMax()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(spec).stream()
                .map(this::toCatalogoDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroCatalogo> buscarRelacionados(CriterioSimilitud criterio) {
        // Implementación básica, retorna todos para este ejemplo de MVP, o podemos buscar por misma categoría.
        // Simularemos buscando por la misma categoría excluyendo el libro original si tuviéramos su ID.
        return repository.findAll().stream()
                .limit(5)
                .map(this::toCatalogoDomain)
                .collect(Collectors.toList());
    }
    
    private LibroCatalogo toCatalogoDomain(ContenidoDigitalEntity entity) {
        return new LibroCatalogo(
                entity.getIsbn(),
                entity.getTitulo(),
                entity.getPrecio(),
                entity.getUrlPortada()
        );
    }
}
