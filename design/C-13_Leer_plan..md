# Diseño Técnico e Implementación: C-13 - Detalle libro

Este documento define el plan de implementación detallado para la Historia de Usuario **C-13**: 
> *"Como comprador, quiero leer reseñas de otros compradores para tomar una decisión más informada"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Reseña` o `Review` (Agregado independiente asociado al ID del libro).
* **Value Objects**: `Calificacion` (1 a 5 estrellas), `Comentario`, `IdLibro`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ILeerDetalleLibroUseCase` (específico para reseñas).
* **Output Port (Gateway)**: `IReseñaGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lectura de reseñas debe ser un caso de uso independiente a la carga inicial del libro (`VerDetalleLibroInteractor`) para no saturar la respuesta principal (Lazy Loading conceptual).

### B. Domain Driven Design (DDD)
* Un libro puede tener cientos de reseñas, por lo cual es obligatorio implementar paginación (VO `Paginacion`).

### C. Principios SOLID
* **ISP**: No saturar `ILibroGateway` con métodos de reseñas. Usar `IReseñaGateway`.

### D. Test-Driven Development (TDD)
* Mockear un Gateway que retorne 3 reseñas. El interactor debe promediarlas o simplemente empaquetarlas en el DTO de salida sin romper la estructura.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Archivo `resenas.json`. El `ResenaJsonGateway` filtra por el `IdLibro` recibido.
* **Entrega 2**: Tabla relacional `reviews` con Foreign Key al libro e índices de lectura paginada en BD.
* **API Web**: GET `/api/v1/libros/{id}/resenas`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `Reseña` y Value Object `Calificacion`.
- [ ] 2. Definir interfaz `IReseñaGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear `ILeerDetalleLibroUseCase` (para Reseñas) y Response DTO paginado.
- [ ] 4. (TDD) Tests de extracción y paginación de reseñas.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar `ResenaJsonGateway` y su lectura en memoria.
- [ ] 7. Implementar el endpoint REST con soporte de Paginación.
