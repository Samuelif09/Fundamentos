package com.openlib.market.domain.finanzas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SerieTiempoVentas {

    public List<PuntoDatos> agrupar(List<TransaccionFinanciera> transacciones, IntervaloTiempo intervalo) {
        if (transacciones == null || transacciones.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDate minFecha = transacciones.stream().map(TransaccionFinanciera::getFecha).min(LocalDate::compareTo).orElse(LocalDate.now());
        LocalDate maxFecha = transacciones.stream().map(TransaccionFinanciera::getFecha).max(LocalDate::compareTo).orElse(LocalDate.now());

        Map<String, Double> agrupado = transacciones.stream()
                .collect(Collectors.groupingBy(
                        t -> formatearFecha(t.getFecha(), intervalo),
                        Collectors.summingDouble(TransaccionFinanciera::getSubtotal)
                ));

        List<PuntoDatos> resultado = new ArrayList<>();
        LocalDate actual = minFecha;

        while (!actual.isAfter(maxFecha)) {
            String clave = formatearFecha(actual, intervalo);
            if (resultado.isEmpty() || !resultado.get(resultado.size() - 1).getFecha().equals(clave)) {
                double valor = agrupado.getOrDefault(clave, 0.0);
                resultado.add(new PuntoDatos(clave, valor));
            }
            actual = avanzar(actual, intervalo);
        }

        return resultado;
    }

    private String formatearFecha(LocalDate fecha, IntervaloTiempo intervalo) {
        return switch (intervalo) {
            case DIARIO -> fecha.format(DateTimeFormatter.ISO_LOCAL_DATE);
            case SEMANAL -> {
                // Simplificación para el MVP: agrupar por Lunes de la semana
                LocalDate inicioSemana = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
                yield inicioSemana.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            case MENSUAL -> fecha.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        };
    }

    private LocalDate avanzar(LocalDate fecha, IntervaloTiempo intervalo) {
        return switch (intervalo) {
            case DIARIO -> fecha.plusDays(1);
            case SEMANAL -> fecha.plusWeeks(1);
            case MENSUAL -> fecha.plusMonths(1);
        };
    }
}
