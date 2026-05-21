# Diseño Técnico e Implementación: S-12 - Gestión inventario

Este documento define el plan de implementación detallado para la Historia de Usuario **S-12**: 
> *"Como vendedor, quiero crear descuentos por tiempo limitado para promover ventas en períodos clave"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `PromocionLibro`.
* **Value Objects**: `IdLibro`, `PorcentajeDescuento` (1 a 99), `PeriodoPromocion` (Fecha Inicio y Fecha Fin).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ICrearDescuentoInventarioUseCase`.
* **Output Ports (Gateways)**: `IPromocionGateway`, `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La promoción existe como un agregado independiente y no sobreescribe el precio base del libro. El precio final se calcula al vuelo en el caso de uso de detalle del libro (C-04).

### B. Domain Driven Design (DDD)
* Validar que la `FechaInicio` no sea menor a la fecha actual y que la `FechaFin` sea posterior a `FechaInicio`.
* Validar que no existan promociones solapadas en tiempo para el mismo libro.

### C. Principios SOLID
* **OCP**: Permite que el sistema de precios original escale sin tocar el agregado `Libro`.

### D. Test-Driven Development (TDD)
* Mockear una promoción existente (Ej: del 1 al 10 de Mayo). Testear que al intentar crear una del 5 al 15 de Mayo se lanza `PromocionSolapadaException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `promociones.json`. El `DetalleJsonGateway` debe buscar si hay una promo activa al devolver el libro.
* **Entrega 2**: Tabla `promociones` en BD. Los endpoints de catálogo incluirán lógica transaccional o vistas que apliquen la regla `precio_actual = precio_base * (1 - descuento)`.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/libros/{bookId}/promociones`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `PromocionLibro` y Value Objects (`PeriodoPromocion`, `PorcentajeDescuento`).
- [ ] 2. (TDD) Pruebas de reglas de validación de fechas y solapamientos.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el interactor `CrearDescuentoInventarioInteractor`.
- [ ] 4. Actualizar `VerDetalleLibroUseCase` (C-04) para que aplique el descuento al vuelo si es que existe.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el guardado y búsqueda de solapamiento en `PromocionJsonGateway`.
- [ ] 6. Controlador REST para programar promociones.
