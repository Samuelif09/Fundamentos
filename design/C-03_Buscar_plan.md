# Diseño Técnico e Implementación: C-03 - Catálogo

Este documento define el plan de implementación detallado para la Historia de Usuario **C-03**: 
> *"Como comprador, quiero buscar libros por título o autor para encontrar lo que quiero específicamente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `CriterioBusqueda` (que ahora incluye campos específicos: `titulo`, `autor`).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IBuscarCatalogoUseCase`.
* **Output Port (Gateway)**: Interfaz `ICatalogoGateway` (extender con `buscarPorFiltros(CriterioBusqueda)`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El Interactor formatea la búsqueda (ej. trim, a minúsculas) y la pasa al Gateway.

### B. Domain Driven Design (DDD)
* Si los parámetros están vacíos, se puede lanzar un error de dominio de `BusquedaInvalidaException` o redirigir el comportamiento al caso de uso de catálogo general.

### C. Principios SOLID
* **OCP**: Usar el patrón Specification o un Query Object (`CriterioBusqueda`) permite que mañana agreguemos "buscar por género" sin cambiar la firma del método en el puerto.

### D. Test-Driven Development (TDD)
* Pruebas del interactor asegurando el mapeo correcto entre los parámetros de entrada y el objeto de criterios enviado al Gateway.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `CatalogoJsonGateway` itera la lista y aplica `String.contains()` en los atributos Título o Autor.
* **Entrega 2**: Implementar JPA Criteria API o consultas nativas robustas para soportar búsquedas parciales (LIKE) sin problemas de "Case Sensitivity".
* **API Web**: GET `/api/v1/catalogo/buscar?titulo={x}&autor={y}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear/Mejorar Value Object `CriterioBusqueda`.
- [ ] 2. Definir método `buscarPorFiltros` en `ICatalogoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de petición de búsqueda estructurada.
- [ ] 4. (TDD) Tests para `BuscarCatalogoInteractor` combinando título y autor.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el filtrado complejo iterativo en `JsonGateway`.
- [ ] 7. Implementar endpoint GET.
