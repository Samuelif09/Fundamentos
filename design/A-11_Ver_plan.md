# Diseño Técnico e Implementación: A-11 - Dashboard métricas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-11**: 
> *"Como admin, quiero ver gráficas de ventas por período para análisis de tendencias de negocio"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SerieGraficaVentas` (A nivel plataforma).
* **Value Objects**: `IntervaloTiempo` (DIARIO, SEMANAL, MENSUAL), `FiltroPeriodo`, `PuntoDatos`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerDashboardMetricasUseCase`.
* **Output Port (Gateway)**: `ITransaccionGateway` / `IPedidoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Muy similar a S-14 (Gráficas de vendedor), pero sin el filtro de `IdVendedor`. El cálculo es global y representa los ingresos totales de OpenLib.

### B. Domain Driven Design (DDD)
* Retornar un objeto de lectura `SerieGraficaVentas` con la serie temporal normalizada (sin saltos de días nulos), para que el front-end pueda renderizar la gráfica sin manipulación extra.

### C. Principios SOLID
* **Reusabilidad**: Podemos reutilizar el motor de transformación temporal que hicimos en el dominio para S-14, pasándole esta vez el conjunto global de ventas.

### D. Test-Driven Development (TDD)
* Comprobar que los puntos generados corresponden al acumulado de múltiples vendedores en un mismo día, validando que la suma total es correcta.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer todos los pedidos globales desde `pedidos.json`, parsear, agrupar por fechas globales y sumar el monto retenido por la plataforma + ingresos de vendedores.
* **Entrega 2**: Delegar la suma y el agrupamiento a la BD con funciones avanzadas (ej. `GROUP BY DATE(fecha)`).
* **API Web**: GET `/api/v1/admin/dashboard/graficas/ventas?intervalo=MENSUAL&anio=2026`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender o reutilizar la lógica de `SerieTiempoVentas` para ámbito global.
- [ ] 2. Validar que agrupa todas las transacciones de todos los vendedores.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `VerDashboardMetricasInteractor`.
- [ ] 4. (TDD) Pruebas unitarias de agregación global.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar Gateway JSON devolviendo todos los pedidos sin filtro de vendedor.
- [ ] 6. Endpoint protegido del administrador.
