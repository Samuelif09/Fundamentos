package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.Cantidad;
import com.openlib.market.domain.carrito.LibroSnapshot;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.CarritoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class CarritoJpaGatewayTest {

    @Autowired
    private CarritoJpaGateway carritoJpaGateway;

    @Autowired
    private CarritoRepository carritoRepository;

    @Test
    public void testPersistenciaCarritoEItems() {
        CarritoCompras carrito = new CarritoCompras(new SesionId("sesion-carrito-1"));
        carrito.agregarItem(new LibroSnapshot("isbn-A", 29.99), new Cantidad(2));
        carrito.agregarItem(new LibroSnapshot("isbn-B", 14.99), new Cantidad(1));

        carritoJpaGateway.guardar(carrito);

        Optional<CarritoCompras> recuperado = carritoJpaGateway.obtenerPorSesionId(new SesionId("sesion-carrito-1"));

        assertTrue(recuperado.isPresent());
        assertEquals(2, recuperado.get().getItems().size());
        assertEquals(2, recuperado.get().getItems().stream()
                .filter(i -> i.getLibroIsbn().equals("isbn-A"))
                .findFirst().get().getCantidad().getValor());
    }

    @Test
    public void testActualizarCarritoSinDuplicados() {
        CarritoCompras carrito = new CarritoCompras(new SesionId("sesion-carrito-2"));
        carrito.agregarItem(new LibroSnapshot("isbn-C", 9.99), new Cantidad(1));
        carritoJpaGateway.guardar(carrito);

        long countAntes = carritoRepository.count();

        // Actualizar el mismo carrito - no debe crear duplicado
        carrito.agregarItem(new LibroSnapshot("isbn-D", 19.99), new Cantidad(3));
        carritoJpaGateway.guardar(carrito);

        assertEquals(countAntes, carritoRepository.count());

        Optional<CarritoCompras> recuperado = carritoJpaGateway.obtenerPorSesionId(new SesionId("sesion-carrito-2"));
        assertTrue(recuperado.isPresent());
        assertEquals(2, recuperado.get().getItems().size());
    }
}
