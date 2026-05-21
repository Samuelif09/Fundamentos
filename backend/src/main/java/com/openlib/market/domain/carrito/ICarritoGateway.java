package com.openlib.market.domain.carrito;

import java.util.Optional;

public interface ICarritoGateway {
    Optional<CarritoCompras> obtenerPorSesionId(SesionId sesionId);
    Optional<CarritoCompras> obtenerPorUsuario(IdUsuario idUsuario);
    void guardar(CarritoCompras carrito);
}
