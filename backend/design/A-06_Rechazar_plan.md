# Diseño Técnico e Implementación: A-06 - Curaduría contenido

Este documento define el plan de implementación detallado para la Historia de Usuario **A-06**: 
> *"Como admin, quiero rechazar un libro que viola políticas para proteger a los usuarios y la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro` y `RegistroCuraduria`.
* **Value Objects**: `IdLibro`, `EstadoLibro` (RECHAZADO), `MotivoRechazo`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRechazarCuraduríaContenidoUseCase`.
* **Output Ports (Gateways)**: `ILibroGateway`, `INotificacionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El acto de rechazar es una transición de estado compleja. Cambia el estado del libro a `RECHAZADO` y registra una nota interna (auditoría).

### B. Domain Driven Design (DDD)
* Un libro en estado `ACTIVO` puede ser rechazado (post-moderación), o uno en `EN_REVISION` puede ser rechazado (pre-moderación). 
* Obligatorio adjuntar un `MotivoRechazo` (Value Object con validación de no estar vacío).

### C. Principios SOLID
* **ISP**: El Gateway de notificaciones tendrá un método específico para alertar al vendedor sobre el rechazo y los motivos.

### D. Test-Driven Development (TDD)
* Testear que se lanza una excepción de dominio si el motivo de rechazo viene vacío.
* Asegurar el cambio de estado en la entidad libro.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar el estado y guardar el motivo en un campo adjunto en `libros.json`.
* **Entrega 2**: Actualización transaccional en PostgreSQL y registro en una tabla secundaria de `auditoria_curaduria`.
* **API Web**: POST `/api/v1/admin/curaduria/libros/{bookId}/rechazar` con body `{"motivo": "..."}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el VO `MotivoRechazo` y extender las transiciones del `Libro`.
- [ ] 2. (TDD) Pruebas de transiciones y validaciones del motivo.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor.
- [ ] 4. (TDD) Pruebas de flujo completo incluyendo el disparo de la notificación.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar escritura en los Gateways JSON.
- [ ] 6. Controlador REST.
