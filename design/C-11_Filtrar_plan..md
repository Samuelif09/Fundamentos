# Diseño Técnico e Implementación: C-11 - Catálogo

Este documento define el plan de implementación detallado para la Historia de Usuario **C-11**: 
> *"Como comprador, quiero filtrar libros por categoría para explorar géneros específicos de mi interés"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo` / `Libro`.
* **Value Objects**: `CategoriaLibro` (enum o VO), `CriterioBusqueda`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IFiltrarCatalogoUseCase`.
* **Output Port (Gateway)**: `ICatalogoGateway` (extender `buscarPorFiltros` para incluir la categoría).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso actualiza el objeto `CriterioBusqueda` existente, acoplándose al patrón *Specification* definido en C-03.

### B. Domain Driven Design (DDD)
* Si se envía una categoría que no existe en el catálogo, el dominio debe retornar una lista vacía, no una excepción, ya que es un escenario válido de búsqueda.

### C. Principios SOLID
* **OCP**: Al usar el patrón Specification, la clase Interactor casi no se modifica; solo añadimos `categoria` al Request DTO y al `CriterioBusqueda`.

### D. Test-Driven Development (TDD)
* Mockear la lista de libros y verificar que el filtro de categoría se aplica correctamente aislando el resto de los filtros.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar `CatalogoJsonGateway` para incluir una condición extra (`if libro.getCategoria().equals(categoriaFiltro)`) en su flujo de iteración.
* **Entrega 2**: Agregar soporte en JPA Criteria API o JPQL (`WHERE l.categoria = :categoria`).
* **API Web**: Modificar el endpoint GET `/api/v1/catalogo` para aceptar el query param `?categoria=FICCIÓN`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Añadir el campo `Categoria` al Value Object `CriterioBusqueda`.
- [ ] 2. Actualizar las validaciones de filtros.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Actualizar Request DTO para aceptar categoría.
- [ ] 4. (TDD) Pruebas de filtrado por categoría en el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el filtrado iterativo por categoría en el Gateway JSON.
- [ ] 6. Actualizar el controlador REST.
