package com.openlib.market.domain.cupon;

import java.util.Optional;

public interface ICuponGateway {
    Optional<CuponDescuento> buscarPorCodigo(CodigoCupon codigo);
}
