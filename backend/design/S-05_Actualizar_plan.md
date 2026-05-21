# Diseño Técnico e Implementación: S-05 - Gestión inventario

Este documento define el plan de implementación detallado para la Historia de Usuario **S-05**: 
> *"Como vendedor, quiero actualizar precio del libro en cualquier momento para responder al mercado"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `Precio` (mayor o igual a cero), `IdLibro`, `IdVendedor`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IActualizarInventarioUseCase`.
* **Output Port (Gateway)**: `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso debe validar obligatoriamente que el usuario que intenta modificar el precio sea el propietario del libro (verificando `IdVendedor`).

### B. Domain Driven Design (DDD)
* La entidad `Libro` tendrá un método de comportamiento explícito `actualizarPrecio(NuevoPrecio)` en lugar de setters genéricos. Esto permite disparar eventos de dominio internos en el futuro (ej. `PrecioActualizadoEvent` para notificar a compradores, según la historia C-20).

### C. Principios SOLID
* **SRP**: Un Interactor exclusivo para la actualización de precios, aislando esta lógica de la publicación (S-02) o edición de metadatos (título/sinopsis).

### D. Test-Driven Development (TDD)
* Mockear un libro que pertenece al "Vendedor A". Intentar actualizar su precio enviando credenciales del "Vendedor B" y verificar que se lanza `AccesoDenegadoException`.
* Validar que precios negativos fallan en la creación del Value Object `Precio`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `LibroJsonGateway` carga la lista, busca el ID, actualiza la propiedad y sobrescribe `libros.json`.
* **Entrega 2**: Implementar `@Modifying` o `save()` en JPA, asegurando control de concurrencia optimista (`@Version`) por si dos dispositivos del vendedor intentan actualizar el precio al mismo tiempo.
* **API Web**: PATCH `/api/v1/vendedores/{sellerId}/libros/{bookId}/precio`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear/Asegurar validaciones en el VO `Precio`.
- [ ] 2. Implementar `actualizarPrecio(Precio)` en el Agregado `Libro`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Request (`ActualizarPrecioRequest`).
- [ ] 4. (TDD) Pruebas de cambio exitoso y violación de propiedad.
- [ ] 5. Implementar el Interactor `ActualizarInventarioInteractor`.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Actualizar métodos de escritura parcial en el JSON Gateway.
- [ ] 7. Implementar el Controlador REST utilizando HTTP PATCH.
