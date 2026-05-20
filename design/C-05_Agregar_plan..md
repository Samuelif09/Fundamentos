# Diseño Técnico e Implementación: C-05 - Carrito

Este documento define el plan de implementación detallado para la Historia de Usuario **C-05**: 
> *"Como comprador, quiero agregar libros al carrito para acumular mi pedido antes de pagar"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CarritoCompras`.
* **Entidades Hijas**: `ItemCarrito`.
* **Value Objects**: `IdUsuario` (vincula el carrito a un usuario autenticado), `Subtotal`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IAgregarCarritoUseCase`.
* **Output Port (Gateway)**: `ICarritoGateway`, `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* A diferencia del visitante, este carrito se asocia a la cuenta de usuario. El `IdUsuario` se inyecta desde el controlador tras validar el JWT.

### B. Domain Driven Design (DDD)
* Evitar que se agreguen más libros de los disponibles. Lanzar `StockInsuficienteException` si falla la regla.

### C. Principios SOLID
* **OCP**: La entidad `CarritoCompras` puede recibir un `CalculadoraDescuentos` como estrategia (Strategy Pattern) para recalcular el subtotal sin modificar su código interno.

### D. Test-Driven Development (TDD)
* Pruebas sobre la Entidad `CarritoCompras` asegurando que sumar items calcula el total con precisión decimal.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `CarritoJsonGateway` que guarde/actualice objetos en `carritos_compradores.json` mapeados por `IdUsuario`.
* **Entrega 2**: Guardar en MongoDB/Redis para alta disponibilidad.
* **API Web**: POST `/api/v1/usuarios/{userId}/carrito/items`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender `CarritoCompras` con lógica para usuarios logueados.
- [ ] 2. Definir puertos en el Dominio.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. (TDD) Tests para `AgregarCarritoInteractor` (usuario autenticado).
- [ ] 4. Implementar el Interactor con validación de stock.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar lectura/escritura en JSON mapeada por ID de usuario.
- [ ] 6. Controlador Web validando que el ID del token coincida con el Request.
