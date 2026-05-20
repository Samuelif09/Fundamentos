package com.openlib.market.domain.historial;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistorialNavegacionTest {

    @Test
    void debeRegistrarNuevaVistaYOrdenarCorrectamente() {
        HistorialNavegacion historial = new HistorialNavegacion("u1", new ArrayList<>());
        
        historial.registrarVista("libro1", LocalDateTime.now().minusMinutes(10));
        historial.registrarVista("libro2", LocalDateTime.now().minusMinutes(5)); // Más reciente

        assertEquals(2, historial.getItems().size());
        assertEquals("libro2", historial.getItems().get(0).getIdLibro()); // libro2 primero
        assertEquals("libro1", historial.getItems().get(1).getIdLibro());
    }

    @Test
    void debeActualizarFechaSiLibroYaFueVisto() {
        HistorialNavegacion historial = new HistorialNavegacion("u1", new ArrayList<>());
        
        historial.registrarVista("libro1", LocalDateTime.now().minusDays(1));
        historial.registrarVista("libro2", LocalDateTime.now().minusHours(1));
        
        // El usuario vuelve a ver el libro1
        LocalDateTime nuevaFecha = LocalDateTime.now();
        historial.registrarVista("libro1", nuevaFecha);

        assertEquals(2, historial.getItems().size());
        assertEquals("libro1", historial.getItems().get(0).getIdLibro()); // libro1 ahora es el primero
        assertEquals(nuevaFecha, historial.getItems().get(0).getFechaVista());
    }

    @Test
    void debeMantenerElLimiteDeHistorial() {
        HistorialNavegacion historial = new HistorialNavegacion("u1", new ArrayList<>());
        
        // Agregamos 51 libros
        for (int i = 1; i <= 51; i++) {
            historial.registrarVista("libro" + i, LocalDateTime.now().plusSeconds(i));
        }

        // El límite es 50
        assertEquals(50, historial.getItems().size());
        // El más reciente fue "libro51"
        assertEquals("libro51", historial.getItems().get(0).getIdLibro());
        // El más antiguo ("libro1") debe haber sido eliminado
        assertFalse(historial.getItems().stream().anyMatch(i -> i.getIdLibro().equals("libro1")));
    }
}
