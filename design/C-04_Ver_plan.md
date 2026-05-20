# Diseño Técnico e Implementación: C-04 - Detalle libro

Este documento define el plan de implementación detallado para la Historia de Usuario **C-04**: 
> *"Como comprador, quiero ver precio, sinopsis y disponibilidad del libro para decidir si compro"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**, cumpliendo con los requerimientos técnicos del proyecto OpenLib Market.

---

## 1. Análisis y Modelado de Dominio (DDD & Clean Architecture)

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro` (lectura).
* **Entidad Secundaria**: `Inventario` (para verificar disponibilidad de licencias/copias).
* **Value Objects**: `Precio`, `StockDisponible`, `Isbn`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerDetalleLibroUseCase`.
* **Output Ports (Gateways)**: `ILibroGateway` (para datos base) e `IInventarioGateway` (para validar copias restantes).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El cruce de información entre el catálogo y el inventario se realiza en el Interactor, no en la base de datos mediante un `JOIN` rígido, permitiendo que el inventario pueda vivir en otro microservicio a futuro.

### B. Domain Driven Design (DDD)
* Retornar un `LibroDetalleDto` que incluya un campo booleano `disponibleParaCompra` basado en las reglas del `StockDisponible`.

### C. Principios SOLID
* **SRP**: Separamos el Gateway de catálogo del Gateway de inventario.

### D. Test-Driven Development (TDD)
* Mockear `IInventarioGateway` para simular que un libro tiene 0 unidades y asegurar que el DTO de salida refleje "No Disponible".

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer la disponibilidad de un campo en `libros.json` a través de `InventarioJsonGateway`.
* **Entrega 2**: Consultar a PostgreSQL la tabla de inventario bloqueando la lectura de forma optimista.
* **API Web**: GET `/api/v1/libros/{id}/detalle-comprador`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `StockDisponible`.
- [ ] 2. Definir interfaz `IInventarioGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO que consolide Libro e Inventario.
- [ ] 4. (TDD) Pruebas para `VerDetalleLibroInteractor`.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar métodos de búsqueda en JSON para inventario.
- [ ] 7. Crear el controlador REST asociado.
