# Diseño Técnico e Implementación: S-16 - Reputación

Este documento define el plan de implementación detallado para la Historia de Usuario **S-16**: 
> *"Como vendedor, quiero responder a reseñas de compradores para gestionar mi reputación públicamente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Reseña` (que ahora incluye la `RespuestaVendedor`).
* **Value Objects**: `IdReseña`, `IdVendedor`, `ComentarioRespuesta` (con límite de caracteres y filtro de lenguaje).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IResponderReputacionUseCase`.
* **Output Ports (Gateways)**: `IReseñaGateway`, `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso garantiza que el vendedor que responde sea el dueño del libro reseñado. Esto se valida cruzando información entre `IReseñaGateway` e `ILibroGateway`.

### B. Domain Driven Design (DDD)
* Un vendedor solo puede responder **una vez** por cada reseña. Si intenta responder de nuevo, lanza `RespuestaDuplicadaException`.
* La respuesta no puede estar vacía.

### C. Principios SOLID
* **OCP**: La entidad `Reseña` se extiende para albergar un objeto anidado o campo de respuesta, sin alterar cómo los compradores la crean.

### D. Test-Driven Development (TDD)
* Mockear una reseña. Probar que si el vendedor envía una respuesta válida, se adjunta correctamente. Si envía una segunda respuesta, la prueba debe validar que el sistema la rechace.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Buscar la reseña en `resenas.json`, actualizarla añadiéndole el campo `respuestaVendedor`, y sobreescribir.
* **Entrega 2**: Actualizar la tabla de PostgreSQL (`UPDATE reseñas SET respuesta = ?, fecha_respuesta = ? WHERE id = ?`).
* **API Web**: POST `/api/v1/resenas/{reviewId}/respuesta`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Ampliar el agregado `Reseña` para incorporar el VO `ComentarioRespuesta`.
- [ ] 2. Implementar regla de respuesta única.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `ResponderReputacionInteractor`.
- [ ] 4. (TDD) Pruebas asegurando que un vendedor ajeno no pueda responder.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar modificación en `ReseñaJsonGateway`.
- [ ] 6. Crear Endpoint validando la identidad del token JWT.
