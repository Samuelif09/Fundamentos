# Diseño Técnico e Implementación: C-12 - Catálogo

Este documento define el plan de implementación detallado para la Historia de Usuario **C-12**: 
> *"Como comprador, quiero filtrar libros por rango de precio para ajustar búsqueda a mi presupuesto"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo`.
* **Value Objects**: `RangoPrecio` (contiene `min` y `max`, validando que `min >= 0` y `max >= min`).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IFiltrarCatalogoUseCase`.
* **Output Port (Gateway)**: `ICatalogoGateway` (extender `CriterioBusqueda`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El Value Object `RangoPrecio` se encarga de lanzar `RangoInvalidoException` si el cliente envía `min=10` y `max=5`.

### B. Domain Driven Design (DDD)
* Integrar `RangoPrecio` dentro de `CriterioBusqueda`.

### C. Principios SOLID
* **SRP**: `RangoPrecio` encapsula exclusivamente la lógica matemática de evaluación de límites.

### D. Test-Driven Development (TDD)
* Test exhaustivo del VO `RangoPrecio`: `max` menor que `min`, valores negativos, o solo límite inferior (`min=0`, `max=null`).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar `CatalogoJsonGateway` para añadir la verificación matemática al recorrer la lista.
* **Entrega 2**: `WHERE l.precio BETWEEN :min AND :max` en PostgreSQL.
* **API Web**: GET `/api/v1/catalogo?minPrice=X&maxPrice=Y`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `RangoPrecio` y agregarlo a `CriterioBusqueda`.
- [ ] 2. (TDD) Test de las reglas de negocio del `RangoPrecio`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Actualizar DTOs y `FiltrarCatalogoInteractor`.
- [ ] 4. (TDD) Test del Interactor integrando los rangos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar validación en memoria en el Gateway JSON.
- [ ] 6. Actualizar Controlador Web.
