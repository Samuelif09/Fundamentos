package com.openlib.market.infrastructure.cupon;

import com.openlib.market.domain.cupon.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CuponJsonGateway implements ICuponGateway {

    private final List<CuponDescuento> baseDatosEnMemoria;

    public CuponJsonGateway() {
        this.baseDatosEnMemoria = new ArrayList<>();
        // Seeders de cupones para pruebas
        baseDatosEnMemoria.add(new CuponDescuento(
                new CodigoCupon("BIENVENIDA20"),
                new DescuentoPorcentaje(20),
                LocalDate.now().plusMonths(1)
        ));
        
        baseDatosEnMemoria.add(new CuponDescuento(
                new CodigoCupon("DESCUENTO10"),
                new DescuentoMontoFijo(10.0),
                LocalDate.now().plusMonths(1)
        ));

        baseDatosEnMemoria.add(new CuponDescuento(
                new CodigoCupon("VENCIDO"),
                new DescuentoPorcentaje(50),
                LocalDate.now().minusDays(1)
        ));
    }

    @Override
    public Optional<CuponDescuento> buscarPorCodigo(CodigoCupon codigo) {
        return baseDatosEnMemoria.stream()
                .filter(cupon -> cupon.getCodigo().equals(codigo))
                .findFirst();
    }
}
