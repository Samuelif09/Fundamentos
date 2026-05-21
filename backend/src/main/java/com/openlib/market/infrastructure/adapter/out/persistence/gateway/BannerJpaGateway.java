package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.marketing.*;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.BannerPromocionalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.BannerPromocionalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class BannerJpaGateway implements IBannerGateway {

    private final BannerPromocionalRepository repository;

    public BannerJpaGateway(BannerPromocionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardar(BannerPromocional banner) {
        repository.save(toEntity(banner));
    }

    @Override
    public void actualizar(BannerPromocional banner) {
        BannerPromocionalEntity entity = repository.findById(banner.getId())
                .orElseThrow(() -> new IllegalArgumentException("Banner no encontrado: " + banner.getId()));
        entity.setEstado(banner.getEstado().name());
        repository.save(entity);
    }

    @Override
    public BannerPromocional obtenerPorId(String id) {
        return repository.findById(id).map(this::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Banner no encontrado: " + id));
    }

    @Override
    public List<BannerPromocional> listarTodos() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public List<BannerPromocional> listarVigentes(LocalDateTime ahora) {
        return repository.findVigentes(ahora).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private BannerPromocionalEntity toEntity(BannerPromocional b) {
        return new BannerPromocionalEntity(b.getId(), b.getTitulo(), b.getUrlImagen(), b.getUrlDestino(),
                b.getPeriodo().getFechaInicio(), b.getPeriodo().getFechaFin(), b.getEstado().name());
    }

    private BannerPromocional toDomain(BannerPromocionalEntity e) {
        return new BannerPromocional(e.getId(), e.getTitulo(), e.getUrlImagen(), e.getUrlDestino(),
                new PeriodoCampana(e.getFechaInicio(), e.getFechaFin()),
                EstadoCampana.valueOf(e.getEstado()));
    }
}
