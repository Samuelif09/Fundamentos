# Diseño Técnico e Implementación: V-04 - Carrito

Este documento define el plan de implementación detallado para la Historia de Usuario **V-04**: 
> *"Como visitante, quiero agregar libros al carrito para acumular mi pedido"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CarritoCompras`.
* **Entidad Hija**: `ItemCarrito` (referencia al libro, cantidad, subtotal).
* **Value Objects**: `SesionId`, `Cantidad` (mayor a 0).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IAgregarCarritoUseCase`.
* **Output Ports (Gateways)**: `ICarritoGateway` y `ILibroGateway` (para validar que el libro existe antes de agregarlo).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica de sumar el total del carrito pertenece a la Entidad `CarritoCompras`, no al Interactor.

### B. Domain Driven Design (DDD)
* El agregado `CarritoCompras` protege sus items. Debería tener un método `agregarItem(libro, cantidad)` que internamente recalcule el total.

### C. Principios SOLID
* **OCP**: Permitir que las reglas de cálculo de subtotal se extiendan en el futuro (ej. descuentos) sin modificar la lógica base de agregar items.

### D. Test-Driven Development (TDD)
* Test unitario sobre `CarritoCompras.agregarItem()` verificando que si se agrega el mismo libro, se suma la cantidad en lugar de crear un item duplicado.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `CarritoJsonGateway` asocia el carrito al ID de sesión o a una cookie temporal.
* **Entrega 2**: Migrar este almacenamiento a Redis o MongoDB, ya que los carritos son efímeros y de rápido acceso.
* **API Web**: POST `/api/v1/carrito/items`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear `CarritoCompras`, `ItemCarrito` y Value Object `Cantidad`.
- [ ] 2. (TDD) Pruebas para la suma de totales e items duplicados.
- [ ] 3. Definir `ICarritoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 4. Implementar `AgregarCarritoInteractor`.
- [ ] 5. Validar existencia del libro consultando el `ILibroGateway`.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar almacenamiento de carritos en memoria/JSON (`CarritoJsonGateway`).
- [ ] 7. Implementar endpoint POST con manejo de IDs de sesión/cookies.
