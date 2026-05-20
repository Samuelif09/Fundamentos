# Diseño Técnico e Implementación: C-22 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-22**: 
> *"Como comprador, quiero seguir a un vendedor/autor para enterarme cuando publique un libro nuevo"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SuscripcionAutor`.
* **Value Objects**: `IdComprador`, `IdVendedor`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ISeguirMiCuentaUseCase`.
* **Output Ports (Gateways)**: `ISuscripcionGateway`, `IUsuarioGateway` (para validar existencia del vendedor).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica de seguir autores debe encapsularse en un agregador propio (`SuscripcionAutor`). No se debe cargar toda la lista de seguidores al cargar un usuario.

### B. Domain Driven Design (DDD)
* Lanzar `OperacionInvalidaException` si un usuario intenta seguirse a sí mismo o si el ID del vendedor no corresponde a un perfil con rol `VENDEDOR`.

### C. Principios SOLID
* **SRP**: Este caso de uso solo crea el vínculo. La notificación real de publicación se hace mediante eventos reactivos (similar a C-20).

### D. Test-Driven Development (TDD)
* Test unitario del interactor asegurando la validación del rol del destinatario (solo se puede seguir a vendedores).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar la relación en `suscripciones.json` (arreglo de pares `[idComprador, idVendedor]`).
* **Entrega 2**: Tabla de relación N:M `suscripciones_autores` en PostgreSQL.
* **API Web**: POST `/api/v1/usuarios/{id}/suscripciones/{idVendedor}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad de relación `SuscripcionAutor`.
- [ ] 2. Definir puertos en el dominio.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el UseCase y su Interactor.
- [ ] 4. (TDD) Tests validando las reglas de no auto-seguimiento y roles válidos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el almacenamiento de la suscripción en el JSON.
- [ ] 6. Controlador REST con validación de ID de usuario autenticado.
