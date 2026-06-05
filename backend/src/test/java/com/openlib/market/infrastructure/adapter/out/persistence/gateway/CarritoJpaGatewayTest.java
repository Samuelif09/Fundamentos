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

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class CarritoJpaGatewayTest {

    private CarritoJpaGateway carritoJpaGateway;

    @Autowired
    private CarritoRepository carritoRepository;

    @BeforeEach
    public void setUp() {
        com.openlib.market.domain.carrito.ILibroGateway libroGateway = Mockito.mock(com.openlib.market.domain.carrito.ILibroGateway.class);
        when(libroGateway.obtenerPorIsbn("isbn-A")).thenReturn(Optional.of(new LibroSnapshot("isbn-A", 29.99)));
        when(libroGateway.obtenerPorIsbn("isbn-B")).thenReturn(Optional.of(new LibroSnapshot("isbn-B", 14.99)));
        when(libroGateway.obtenerPorIsbn("isbn-C")).thenReturn(Optional.of(new LibroSnapshot("isbn-C", 9.99)));
        when(libroGateway.obtenerPorIsbn("isbn-D")).thenReturn(Optional.of(new LibroSnapshot("isbn-D", 19.99)));

        carritoJpaGateway = new CarritoJpaGateway(carritoRepository, libroGateway);
    }

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
        
        double total = recuperado.get().getTotal();
        assertTrue(total > 0, "El total debe ser mayor a 0");
        assertEquals(74.97, total, 0.01);
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
