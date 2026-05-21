# Diseño Técnico e Implementación: A-04 - Gestión usuarios

Este documento define el plan de implementación detallado para la Historia de Usuario **A-04**: 
> *"Como admin, quiero aprobar solicitud de nuevo vendedor para habilitarle la publicación de libros"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Vendedor` y `VerificacionIdentidad`.
* **Value Objects**: `IdVendedor`, `EstadoVerificacion` (EN_REVISION -> APROBADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IAprobarGestiónUsuariosUseCase`.
* **Output Ports (Gateways)**: `IVendedorGateway`, `IEmailGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Completa el flujo iniciado en la historia S-18. El administrador, a través del Interactor de administración, aprueba la identidad del vendedor.

### B. Domain Driven Design (DDD)
* Un vendedor solo puede ser aprobado si su `EstadoVerificacion` es `EN_REVISION`. Si está en `NO_INICIADO`, lanza `SolicitudInvalidaException`.
* La aprobación cambia el estado y habilita permisos de publicación (`puedePublicar = true`).

### C. Principios SOLID
* **OCP**: La lógica de qué sucede al aprobarse puede extenderse emitiendo un evento `VendedorAprobadoEvent` para que otros módulos (como el de catálogos) le den prioridad.

### D. Test-Driven Development (TDD)
* Mockear un vendedor en revisión. Probar que al aprobarlo se cambia su estado y se envía la notificación de éxito.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Localizar al vendedor en `vendedores.json`, actualizar el estado de revisión y guardar.
* **Entrega 2**: Actualizar registro en BD relacional.
* **API Web**: POST `/api/v1/admin/vendedores/{sellerId}/aprobar`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender los métodos de comportamiento del Agregado `Vendedor` para aceptar la aprobación.
- [ ] 2. (TDD) Pruebas sobre los estados inválidos.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor.
- [ ] 4. (TDD) Tests de integración lógica con el Gateway y el servicio de email.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el guardado de la actualización en el Gateway correspondiente.
- [ ] 6. Controlador REST exclusivo para administradores.
