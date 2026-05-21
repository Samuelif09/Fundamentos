package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.detalle.*;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.AudiolibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.CursoVirtualEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import org.springframework.stereotype.Component;

@Component
public class ContenidoDigitalMapper {

    public ContenidoDigital toDomain(ContenidoDigitalEntity entity) {
        Isbn isbn = new Isbn(entity.getIsbn());
        Precio precio = new Precio(entity.getPrecio());
        EstadoLibro estado = EstadoLibro.valueOf(entity.getEstado() != null ? entity.getEstado() : "ACTIVO");

        if (entity instanceof AudiolibroEntity) {
            AudiolibroEntity audio = (AudiolibroEntity) entity;
            return new Audiolibro(isbn, audio.getTitulo(), audio.getSinopsis(), precio, audio.getUrlPortada(), audio.getCategoria(), audio.getIdVendedor(), estado, audio.getUrlVistaPrevia(), new DuracionEnMinutos(audio.getDuracionMinutos() != null ? audio.getDuracionMinutos() : 0));
        } else if (entity instanceof CursoVirtualEntity) {
            CursoVirtualEntity curso = (CursoVirtualEntity) entity;
            return new CursoVirtual(isbn, curso.getTitulo(), curso.getSinopsis(), precio, curso.getUrlPortada(), curso.getCategoria(), curso.getIdVendedor(), estado, curso.getUrlVistaPrevia(), new DuracionEnMinutos(curso.getDuracionEstimadaMinutos() != null ? curso.getDuracionEstimadaMinutos() : 0));
        } else if (entity instanceof LibroEntity) {
            LibroEntity libro = (LibroEntity) entity;
            return new Libro(isbn, libro.getTitulo(), libro.getSinopsis(), precio, libro.getUrlPortada(), libro.getCategoria(), libro.getIdVendedor(), estado, libro.getUrlVistaPrevia());
        }

        throw new IllegalArgumentException("Tipo de entidad no soportado: " + entity.getClass().getName());
    }

    public ContenidoDigitalEntity toEntity(ContenidoDigital domain) {
        if (domain instanceof Audiolibro) {
            Audiolibro audio = (Audiolibro) domain;
            return new AudiolibroEntity(audio.getId().getValor(), audio.getTitulo(), audio.getSinopsis(), audio.getPrecio().getValor(), audio.getUrlPortada(), audio.getCategoria(), audio.getIdVendedor(), audio.getEstado().name(), audio.getUrlVistaPrevia(), 0, audio.getDuracion().getValor());
        } else if (domain instanceof CursoVirtual) {
            CursoVirtual curso = (CursoVirtual) domain;
            return new CursoVirtualEntity(curso.getId().getValor(), curso.getTitulo(), curso.getSinopsis(), curso.getPrecio().getValor(), curso.getUrlPortada(), curso.getCategoria(), curso.getIdVendedor(), curso.getEstado().name(), curso.getUrlVistaPrevia(), 0, curso.getDuracionEstimada().getValor());
        } else if (domain instanceof Libro) {
            Libro libro = (Libro) domain;
            return new LibroEntity(libro.getId().getValor(), libro.getTitulo(), libro.getSinopsis(), libro.getPrecio().getValor(), libro.getUrlPortada(), libro.getCategoria(), libro.getIdVendedor(), libro.getEstado().name(), libro.getUrlVistaPrevia(), 0);
        }

        throw new IllegalArgumentException("Tipo de dominio no soportado: " + domain.getClass().getName());
    }
}
