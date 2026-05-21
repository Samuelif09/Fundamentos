package com.openlib.market.application.cupon;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.IdUsuario;
import com.openlib.market.domain.cupon.CodigoCupon;
import com.openlib.market.domain.cupon.CuponDescuento;
import com.openlib.market.domain.cupon.CuponNoEncontradoException;
import com.openlib.market.domain.cupon.ICuponGateway;

import java.time.LocalDate;

@Service
public class AplicarCuponInteractor implements IAplicarCuponUseCase {

    private final ICuponGateway cuponGateway;
    private final ICarritoGateway carritoGateway;

    public AplicarCuponInteractor(ICuponGateway cuponGateway, ICarritoGateway carritoGateway) {
        this.cuponGateway = cuponGateway;
        this.carritoGateway = carritoGateway;
    }

    @Override
    public AplicarCuponResponseDto aplicar(AplicarCuponRequestDto request) {
        CodigoCupon codigo = new CodigoCupon(request.getCodigoCupon());

        CuponDescuento cupon = cuponGateway.buscarPorCodigo(codigo)
                .orElseThrow(() -> new CuponNoEncontradoException(codigo.getValor()));

        // Valida que no esté expirado (lanza CuponExpiradoException si lo está)
        cupon.validar(LocalDate.now());

        CarritoCompras carrito = carritoGateway.obtenerPorUsuario(new IdUsuario(request.getUserId()))
                .orElseThrow(() -> new IllegalStateException("Carrito no encontrado para el usuario: " + request.getUserId()));

        double totalOriginal = carrito.getTotal();
        
        carrito.aplicarDescuento(cupon);
        carritoGateway.guardar(carrito);
        
        double totalConDescuento = carrito.getTotal();

        return new AplicarCuponResponseDto(totalOriginal, totalConDescuento, codigo.getValor());
    }
}
