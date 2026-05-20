# Diseño Técnico e Implementación: C-15 - Carrito

Este documento define el plan de implementación detallado para la Historia de Usuario **C-15**: 
> *"Como comprador, quiero aplicar cupones de descuento para ahorrar en mi compra"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CuponDescuento`, `CarritoCompras`.
* **Value Objects**: `CodigoCupon`, `PorcentajeDescuento` (entre 0 y 100) o `MontoDescuento`, `FechaExpiracion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IAplicarCarritoUseCase` (Aplicar Cupon).
* **Output Ports (Gateways)**: `ICuponGateway`, `ICarritoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso orquesta la validación del cupón y su posterior aplicación sobre el carrito en memoria antes de guardarlo.

### B. Domain Driven Design (DDD)
* `CuponDescuento` debe tener un método `esValido()` que compruebe su fecha de expiración y límite de usos.
* `CarritoCompras` delega el recálculo a un método interno `aplicarDescuento(CuponDescuento)`.

### C. Principios SOLID
* **Strategy Pattern**: Diferentes tipos de cupones (porcentaje vs monto fijo) pueden implementar la interfaz `EstrategiaDescuento`.

### D. Test-Driven Development (TDD)
* Probar la entidad `CuponDescuento` con fechas de expiración vencidas y asegurar que lance `CuponExpiradoException`.
* Probar el recálculo matemático en `CarritoCompras`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `CuponJsonGateway` lee códigos predefinidos desde `cupones.json`.
* **Entrega 2**: Tabla de cupones en PostgreSQL con control de concurrencia optimista para evitar que un cupón de 1 solo uso se aplique múltiples veces.
* **API Web**: POST `/api/v1/usuarios/{userId}/carrito/cupon` con el código en el body.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `CuponDescuento` y `EstrategiaDescuento`.
- [ ] 2. Actualizar `CarritoCompras` para soportar descuentos.
- [ ] 3. (TDD) Pruebas de reglas de negocio de cupones.

### 🟡 Capa de Aplicación (Application)
- [ ] 4. Crear puertos de entrada/salida.
- [ ] 5. Implementar el Interactor orquestando validación y actualización del carrito.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar persistencia de cupones en JSON.
- [ ] 7. Implementar el Controlador REST.
