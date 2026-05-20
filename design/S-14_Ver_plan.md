# Diseño Técnico e Implementación: S-14 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-14**: 
> *"Como vendedor, quiero ver gráficas de ventas por período para identificar tendencias y picos"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SerieTiempoVentas`.
* **Value Objects**: `IdVendedor`, `IntervaloTiempo` (DIARIO, SEMANAL, MENSUAL), `PuntoDatos` (Fecha + Valor).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerGraficasVentasUseCase`.
* **Output Port (Gateway)**: `IPedidoGateway` / `IMetricasGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El Dominio recibe un volumen alto de transacciones crudas y se encarga de empaquetarlas en puntos cartesianos `(X=Fecha, Y=Monto)` acordes al intervalo de agrupación seleccionado.

### B. Domain Driven Design (DDD)
* `SerieTiempoVentas` debe rellenar los "huecos" (días sin ventas) con `Y=0` para asegurar que las gráficas del frontend no se rompan y mantengan una escala de tiempo continua.

### C. Principios SOLID
* **SRP**: Especializado en transformaciones matemáticas/temporales de datos para gráficos, no para balances contables (S-07).

### D. Test-Driven Development (TDD)
* Mockear una venta el Lunes y otra el Miércoles. Testear que el interactor, bajo intervalo "DIARIO", genera la serie Lunes(X), Martes(0), Miércoles(X).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Traer pedidos del JSON, filtrar, parsear las fechas usando `java.time` y agrupar en un mapa de Java.
* **Entrega 2**: Mover la agrupación a la base de datos usando `GROUP BY DATE_TRUNC('day', fecha)` para mejorar el rendimiento con miles de ventas.
* **API Web**: GET `/api/v1/vendedores/{sellerId}/metricas/ventas?intervalo=DIARIO`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el VO `IntervaloTiempo` y el agrupador de dominio `SerieTiempoVentas`.
- [ ] 2. Implementar la lógica de relleno de días nulos (ceros).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `VerGraficasVentasInteractor`.
- [ ] 4. (TDD) Escribir pruebas unitarias de las transformaciones temporales y rellenos de vacíos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Extraer y agrupar la información desde los archivos locales.
- [ ] 6. Controlador REST devolviendo el Array de DTOs listo para Chart.js o similar en el Frontend.
