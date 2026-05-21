# Diseño Técnico e Implementación: A-09 - Soporte

Este documento define el plan de implementación detallado para la Historia de Usuario **A-09**: 
> *"Como admin, quiero ver tickets de soporte abiertos para gestionar los casos de los usuarios"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `TicketSoporte`.
* **Value Objects**: `IdUsuario`, `EstadoTicket` (ABIERTO, EN_PROGRESO, CERRADO), `Prioridad` (BAJA, MEDIA, ALTA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerSoporteUseCase`.
* **Output Port (Gateway)**: `ITicketSoporteGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El administrador lee la "Bandeja de Entrada" de los tickets. La consulta solo interactúa con el puerto de salida sin saber si los tickets vienen de una BD relacional o una herramienta externa.

### B. Domain Driven Design (DDD)
* Devolver una lista paginada de tickets. Los tickets se deben ordenar por defecto considerando su `Prioridad` (ALTA primero) y luego por su fecha de creación (los más antiguos primero para no romper SLAs).

### C. Principios SOLID
* **SRP**: Separar la lectura y listado de los tickets de la resolución/respuesta de los mismos.

### D. Test-Driven Development (TDD)
* Mockear una lista desordenada de tickets. Testear que el Interactor (o el repositorio mockeado) los entrega correctamente ordenados primero por prioridad y luego por antigüedad.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer la lista desde `tickets.json` y aplicar filtros programáticos (`estado != CERRADO`).
* **Entrega 2**: Implementar JPQL: `SELECT t FROM TicketSoporte t WHERE t.estado IN ('ABIERTO', 'EN_PROGRESO') ORDER BY t.prioridad DESC, t.fechaCreacion ASC`.
* **API Web**: GET `/api/v1/admin/soporte/tickets?estado=abiertos&page=0`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `TicketSoporte` y el Enum `Prioridad`.
- [ ] 2. Definir interfaz de Gateway con soporte para filtros y paginación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `VerSoporteInteractor`.
- [ ] 4. (TDD) Pruebas asegurando el ordenamiento exigido por las reglas de negocio.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Proveer los datos desde el Gateway JSON.
- [ ] 6. Controlador REST protegido.
