# Diseño Técnico e Implementación: V-01 - Búsqueda

Este documento define el plan de implementación detallado para la Historia de Usuario **V-01**: 
> *"Como visitante, quiero buscar libros por palabras clave para encontrar lo que busco rápidamente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**, cumpliendo con los requerimientos técnicos del proyecto OpenLib Market (Java 25, Spring Boot 4x). 

---

## 1. Análisis y Modelado de Dominio (DDD & Clean Architecture)

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo` / `Libro`.
* **Value Objects**: `PalabraClave` (valida longitud mínima, sin caracteres especiales peligrosos para inyección), `CriterioBusqueda`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IBuscarBusquedaUseCase` (o `IBuscarLibroUseCase`) que recibe la petición de búsqueda.
* **Output Port (Gateway)**: Interfaz `IBusquedaGateway` que define cómo el dominio consulta el repositorio sin saber si es JSON o JPA.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* **Regla de Dependencia**: Los Value Objects de búsqueda y la interfaz del Gateway no deben importar paquetes de Spring ni JPA.

### B. Domain Driven Design (DDD)
* **Lenguaje Ubicuo**: Usaremos términos como `Catalogo`, `Filtro`, `Coincidencia` en lugar de términos genéricos de BD.

### C. Principios SOLID
* **SRP**: `BuscarBusquedaInteractor` solo orquesta la lógica de dividir las palabras clave y llamar al Gateway.
* **OCP**: Diseñar el caso de uso para que permita agregar nuevos filtros (autor, categoría) en el futuro sin modificar el buscador base.

### D. Test-Driven Development (TDD)
* Las pruebas unitarias del Interactor deben simular diferentes combinaciones de `PalabraClave` y verificar la interacción con el Gateway mockeado.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1 (Persistencia)**: Crear `BusquedaJsonGateway` que cargue un archivo `libros.json` en memoria, itere sobre las propiedades y aplique un filtrado por texto (substring/regex básico).
* **Entrega 2 (Persistencia)**: Crear `BusquedaJpaGateway` usando consultas nativas o JPQL (`LIKE %keyword%`) en PostgreSQL para optimizar el rendimiento y cumplir el SLA de < 1.5s.
* **API Web**: `BusquedaController` expone un endpoint GET `/api/v1/catalogo/buscar?q={keyword}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `PalabraClave` con sus validaciones.
- [ ] 2. (TDD) Implementar pruebas unitarias para `PalabraClave`.
- [ ] 3. Definir la interfaz `IBusquedaGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 4. Crear interfaz `IBuscarBusquedaUseCase` y su DTO de salida `LibroBuscadoDto`.
- [ ] 5. (TDD) Escribir pruebas con `@Mock` para `BuscarBusquedaInteractor`.
- [ ] 6. Implementar `BuscarBusquedaInteractor`.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 7. Crear `BusquedaJsonGateway` con lectura usando Jackson (Entrega 1).
- [ ] 8. Crear `BusquedaController` (GET `/api/v1/catalogo/buscar`).
- [ ] 9. (TDD) Pruebas de integración con `MockMvc` para el endpoint.


