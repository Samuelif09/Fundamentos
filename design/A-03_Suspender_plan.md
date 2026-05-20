# Diseño Técnico e Implementación: A-03 - Gestión usuarios

Este documento define el plan de implementación detallado para la Historia de Usuario **A-03**: 
> *"Como admin, quiero suspender cuenta de usuario infractor para aplicar las políticas de la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Usuario` (y su subclase `Vendedor`).
* **Value Objects**: `IdUsuario`, `EstadoCuenta` (ACTIVO, SUSPENDIDO, BANEADO), `MotivoSuspension`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ISuspenderGestiónUsuariosUseCase`.
* **Output Ports (Gateways)**: `IUsuarioGateway`, `INotificacionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La acción de suspender se realiza desde el módulo de administración, pero la entidad `Usuario` es compartida. El cambio de estado se realiza en el dominio del usuario garantizando la consistencia.

### B. Domain Driven Design (DDD)
* `Usuario` tendrá un método `suspender(MotivoSuspension)` que cambia su estado a `SUSPENDIDO`. Si el usuario ya está suspendido o baneado, se lanza `EstadoInvalidoException`.
* La suspensión no borra los datos físicos del usuario (Soft Delete o cambio de bandera).

### C. Principios SOLID
* **SRP**: Un caso de uso específico para las acciones disciplinarias del administrador.

### D. Test-Driven Development (TDD)
* Mockear un usuario ACTIVO. Llamar al interactor con un motivo de suspensión y verificar que el estado cambia y el Gateway guarda los cambios.
* Verificar que se desencadena el envío de un email informando al usuario sobre su suspensión.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar el campo `estado` en el archivo `usuarios.json` (y `vendedores.json` si aplica).
* **Entrega 2**: Actualización en la base de datos PostgreSQL y revocación inmediata de los tokens JWT activos del usuario mediante una "Blacklist" en Redis.
* **API Web**: PATCH `/api/v1/admin/usuarios/{userId}/suspender`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Incorporar el VO `MotivoSuspension` y el enum `EstadoCuenta`.
- [ ] 2. (TDD) Pruebas de reglas de negocio sobre los cambios de estado en la Entidad.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el `SuspenderGestiónUsuariosInteractor`.
- [ ] 4. (TDD) Tests de orquestación (recuperar, mutar, guardar y notificar).

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar actualización parcial en el Gateway JSON.
- [ ] 6. Controlador REST protegido para rol ADMIN.
