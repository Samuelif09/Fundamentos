package com.openlib.market.application.marketing;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.marketing.BannerPromocional;
import com.openlib.market.domain.marketing.EstadoCampana;
import com.openlib.market.domain.marketing.IBannerGateway;
import com.openlib.market.domain.marketing.PeriodoCampana;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestionarBannersInteractor implements IGestionarBannersUseCase {

    private final IBannerGateway bannerGateway;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public GestionarBannersInteractor(IBannerGateway bannerGateway) {
        this.bannerGateway = bannerGateway;
    }

    @Override
    public BannerDto crearBanner(String titulo, String urlImagen, String urlDestino, String fechaInicio, String fechaFin) {
        PeriodoCampana periodo = new PeriodoCampana(
                LocalDateTime.parse(fechaInicio, formatter),
                LocalDateTime.parse(fechaFin, formatter)
        );

        BannerPromocional banner = new BannerPromocional(titulo, urlImagen, urlDestino, periodo);
        bannerGateway.guardar(banner);
        
        return mapToDto(banner);
    }

    @Override
    public void cambiarEstado(String id, String estadoStr) {
        BannerPromocional banner = bannerGateway.obtenerPorId(id);
        if (banner == null) throw new IllegalArgumentException("Banner no encontrado");

        EstadoCampana estado = EstadoCampana.valueOf(estadoStr.toUpperCase());
        banner.cambiarEstado(estado);
        bannerGateway.actualizar(banner);
    }

    @Override
    public List<BannerDto> listarBanners() {
        return bannerGateway.listarTodos().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BannerDto mapToDto(BannerPromocional banner) {
        return new BannerDto(
                banner.getId(),
                banner.getTitulo(),
                banner.getUrlImagen(),
                banner.getUrlDestino(),
                banner.getPeriodo().getFechaInicio().toString(),
                banner.getPeriodo().getFechaFin().toString(),
                banner.getEstado().name(),
                banner.estaVigente(LocalDateTime.now())
        );
    }
}
