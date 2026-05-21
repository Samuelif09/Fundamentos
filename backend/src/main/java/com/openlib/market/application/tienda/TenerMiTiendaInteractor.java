package com.openlib.market.application.tienda;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;
import com.openlib.market.domain.tienda.PerfilTienda;
import com.openlib.market.domain.tienda.UrlAmigable;

import java.util.List;
import java.util.Optional;

@Service
public class TenerMiTiendaInteractor implements ITenerMiTiendaUseCase {

    private final ITiendaVendedorGateway tiendaVendedorGateway;
    private final IInventarioGateway inventarioGateway;

    public TenerMiTiendaInteractor(ITiendaVendedorGateway tiendaVendedorGateway, IInventarioGateway inventarioGateway) {
        this.tiendaVendedorGateway = tiendaVendedorGateway;
        this.inventarioGateway = inventarioGateway;
    }

    @Override
    public TiendaPublicaDto obtenerTienda(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw new IllegalArgumentException("El slug de la tienda es obligatorio");
        }

        Optional<ITiendaVendedorGateway.PerfilTiendaBase> perfilOpt = tiendaVendedorGateway.obtenerPerfilPorSlug(slug);
        if (perfilOpt.isEmpty()) {
            throw new IllegalArgumentException("Tienda no encontrada");
        }

        ITiendaVendedorGateway.PerfilTiendaBase base = perfilOpt.get();
        List<LibroCatalogo> catalogo = inventarioGateway.listarPorVendedorId(base.idVendedor());

        // Aseguramos que la regla de la URL se cumple reconstruyéndola (DDD)
        UrlAmigable urlAmigable = new UrlAmigable(base.nombreTienda());

        PerfilTienda perfilTienda = new PerfilTienda(
                base.idVendedor(),
                base.nombreTienda(),
                urlAmigable,
                catalogo
        );

        return new TiendaPublicaDto(
                perfilTienda.getIdVendedor(),
                perfilTienda.getNombreTienda(),
                perfilTienda.getUrlAmigable().getValor(),
                perfilTienda.getCatalogoPublico()
        );
    }
}
