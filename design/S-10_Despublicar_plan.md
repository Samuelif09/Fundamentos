# Diseño Técnico e Implementación: S-10 - Gestión inventario

Este documento define el plan de implementación detallado para la Historia de Usuario **S-10**: 
> *"Como vendedor, quiero despublicar un libro para retirarlo temporalmente del catálogo sin eliminarlo"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `IdLibro`, `IdVendedor`, `EstadoLibro` (ACTIVO, PAUSADO, BLOQUEADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IDespublicarInventarioUseCase`.
* **Output Port (Gateway)**: `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Validar que el `IdVendedor` que ejecuta la acción corresponde al propietario del libro.

### B. Domain Driven Design (DDD)
* La entidad `Libro` debe tener un método `pausar()` o `despublicar()` que mute internamente el estado a `PAUSADO` y lance una excepción si el libro ya estaba pausado o si estaba `BLOQUEADO` por el administrador.

### C. Principios SOLID
* **SRP**: Un caso de uso específico para mutaciones de estado de visibilidad, separado de la eliminación física (Soft Delete).

### D. Test-Driven Development (TDD)
* Pruebas del Agregado `Libro`: asegurar la correcta transición de estados (`ACTIVO -> PAUSADO`).
* Testear que si un vendedor ajeno intenta despublicarlo, se lance `AccesoDenegadoException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Modificar la propiedad `estado` a `PAUSADO` en `libros.json`. El Gateway de Catálogo (`C-02`) debe asegurarse de filtrar y no mostrar libros pausados.
* **Entrega 2**: Actualización transaccional en PostgreSQL (`UPDATE libros SET estado = 'PAUSADO' WHERE id = ?`).
* **API Web**: PATCH `/api/v1/vendedores/{sellerId}/libros/{bookId}/estado` con body `{"estado": "PAUSADO"}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Añadir el VO / Enum `EstadoLibro` a la entidad `Libro`.
- [ ] 2. (TDD) Testear las transiciones de estado en la entidad.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `DespublicarInventarioInteractor`.
- [ ] 4. (TDD) Pruebas de validación de propiedad.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el PATCH en el `LibroJsonGateway`.
- [ ] 6. Actualizar métodos de lectura públicos para ignorar libros pausados.
- [ ] 7. Implementar el Controlador REST.
