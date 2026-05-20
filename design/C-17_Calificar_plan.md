# Diseño Técnico e Implementación: C-17 - Post-Compra

Este documento define el plan de implementación detallado para la Historia de Usuario **C-17**: 
> *"Como comprador, quiero calificar y escribir reseñas de libros comprados para ayudar a otros usuarios"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Reseña`.
* **Value Objects**: `Calificacion` (1 a 5), `Comentario` (máximo 500 caracteres, sin insultos), `IdUsuario`, `IdLibro`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ICalificarPostCompraUseCase`.
* **Output Ports (Gateways)**: `IReseñaGateway`, `IPedidoGateway` (para verificar propiedad).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La validación de que "el usuario compró el libro" es crítica. El interactor primero consulta al `IPedidoGateway` si existe una compra válida antes de persistir la reseña.

### B. Domain Driven Design (DDD)
* Una reseña es válida solo si contiene una calificación numérica válida. El comentario puede ser opcional.
* Lanzar `CompraRequeridaException` si el usuario intenta reseñar sin haber comprado.

### C. Principios SOLID
* **SRP**: El interactor verifica la regla y delega la creación a la entidad `Reseña`.

### D. Test-Driven Development (TDD)
* (1) Test: Usuario no compró -> Exception. 
* (2) Test: Usuario ya reseñó (regla opcional de 1 reseña por libro) -> Exception.
* (3) Test: Éxito -> Gateway guarda reseña.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `ReseñaJsonGateway` guarda un objeto JSON en `resenas.json`. `PedidoJsonGateway` expone el método para verificar propiedad.
* **Entrega 2**: Tabla de reseñas en PostgreSQL. Añadir validación asíncrona de lenguaje en el comentario.
* **API Web**: POST `/api/v1/libros/{id}/resenas`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `Reseña` con sus Value Objects de validación.
- [ ] 2. Definir puertos correspondientes.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Request (Calificacion, Comentario).
- [ ] 4. (TDD) Escribir pruebas del interactor asegurando el control de propiedad.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar escritura en `JsonGateway`.
- [ ] 7. Implementar el Controlador REST.
