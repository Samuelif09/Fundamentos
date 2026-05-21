package com.openlib.market.domain.historial;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialNavegacion {
    private final String idUsuario;
    private final List<ItemNavegacion> items;
    private static final int LIMITE_HISTORIAL = 50;

    public HistorialNavegacion(String idUsuario, List<ItemNavegacion> itemsExistentes) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }
        this.idUsuario = idUsuario;
        this.items = itemsExistentes != null ? new ArrayList<>(itemsExistentes) : new ArrayList<>();
        ordenarYLimitar();
    }

    public void registrarVista(String idLibro, LocalDateTime fechaVista) {
        // Si el libro ya estaba en el historial, actualiza la fecha
        items.stream()
                .filter(item -> item.getIdLibro().equals(idLibro))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.actualizarFechaVista(fechaVista),
                        () -> items.add(new ItemNavegacion(idLibro, fechaVista))
                );

        ordenarYLimitar();
    }

    private void ordenarYLimitar() {
        // Ordenar descendente por fecha de vista (más recientes primero)
        items.sort((a, b) -> b.getFechaVista().compareTo(a.getFechaVista()));

        // Mantener solo los últimos LIMITE_HISTORIAL
        if (items.size() > LIMITE_HISTORIAL) {
            items.subList(LIMITE_HISTORIAL, items.size()).clear();
        }
    }

    public String getIdUsuario() { return idUsuario; }
    public List<ItemNavegacion> getItems() { return Collections.unmodifiableList(items); }
}
