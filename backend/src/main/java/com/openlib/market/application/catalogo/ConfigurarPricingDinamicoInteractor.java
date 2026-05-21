package com.openlib.market.application.catalogo;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.catalogo.IReglaPricingGateway;
import com.openlib.market.domain.catalogo.PrecioMaximo;
import com.openlib.market.domain.catalogo.PrecioMinimo;
import com.openlib.market.domain.catalogo.ReglaPricing;
import com.openlib.market.domain.detalle.Isbn;

@Service
public class ConfigurarPricingDinamicoInteractor implements IConfigurarPricingDinamicoUseCase {

    private final IReglaPricingGateway reglaPricingGateway;

    public ConfigurarPricingDinamicoInteractor(IReglaPricingGateway reglaPricingGateway) {
        this.reglaPricingGateway = reglaPricingGateway;
    }

    @Override
    public void configurar(ConfigurarPricingRequestDto request) {
        ReglaPricing regla = new ReglaPricing(
                new Isbn(request.isbn()),
                request.idVendedor(),
                new PrecioMinimo(request.precioMinimo()),
                new PrecioMaximo(request.precioMaximo()),
                request.estrategia()
        );

        reglaPricingGateway.guardar(regla);
    }
}
