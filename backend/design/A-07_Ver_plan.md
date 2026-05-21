# Diseño Técnico e Implementación: A-07 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-07**: 
> *"Como admin, quiero ver todas las transacciones del sistema para supervisión financiera completa"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `TransaccionGlobal` (Proyección).
* **Value Objects**: `Paginacion`, `FiltroFecha`, `FiltroEstado`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerGestiónVentasUseCase`.
* **Output Port (Gateway)**: `ITransaccionGateway` o `IPedidoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* A diferencia del vendedor (S-07) que solo ve sus ventas, este caso de uso tiene acceso sin restricciones a todas las transacciones de la plataforma. La abstracción debe permitir filtros complejos.

### B. Domain Driven Design (DDD)
* Devolver un DTO resumido que identifique `IdPedido`, `IdComprador`, `MontoTotal` y `Estado`, omitiendo los items individuales a menos que se solicite el detalle.

### C. Principios SOLID
* **OCP**: La interfaz de búsqueda permite la adición de nuevos `Filtros` en el futuro mediante el patrón Specification (por comprador, por vendedor, por rango de fechas).

### D. Test-Driven Development (TDD)
* Probar el filtro de fechas y paginación a nivel del Interactor.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer la lista completa de `pedidos.json`, aplicar los filtros programáticamente y devolver la página requerida.
* **Entrega 2**: Consulta paginada en base de datos `SELECT p FROM Pedido p WHERE ... ORDER BY p.fecha DESC`.
* **API Web**: GET `/api/v1/admin/ventas/transacciones?page=0&size=50&desde=X&hasta=Y`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear VOs para los filtros.
- [ ] 2. Definir la interfaz de salida de consultas globales.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO para la vista de transacción global.
- [ ] 4. (TDD) Tests sobre la combinación de filtros.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar los filtros iterativos en el Gateway JSON.
- [ ] 7. Controlador REST para acceder a la data.
