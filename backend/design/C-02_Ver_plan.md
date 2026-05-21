# Diseño Técnico e Implementación: C-02 - Catálogo

Este documento define el plan de implementación detallado para la Historia de Usuario **C-02**: 
> *"Como comprador, quiero ver catálogo paginado de libros para explorar todas las opciones disponibles"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo`.
* **Value Objects**: `Paginacion` (página actual, tamaño de página, total de elementos).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerCatalogoUseCase`.
* **Output Port (Gateway)**: Interfaz `ICatalogoGateway` (método `listarPaginado(numero, tamano)`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* No utilizar `org.springframework.data.domain.Page` en la capa de Dominio. Crear un envoltorio propio `PaginaDominio<T>` para mantener la independencia.

### B. Domain Driven Design (DDD)
* Validar en el Value Object `Paginacion` que la página no sea menor a 0 y que el tamaño de página no exceda un límite seguro (ej. max 100) para evitar colapsar la memoria.

### C. Principios SOLID
* **SRP**: Este interactor está dedicado únicamente a la lectura estructurada de todo el catálogo.

### D. Test-Driven Development (TDD)
* Mockear un Gateway que devuelve 50 items. Solicitar la página 2 con tamaño 10 y verificar el cálculo de items restantes.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: En `CatalogoJsonGateway`, cargar la lista completa, calcular los índices `(pagina * tamano)` y retornar la sublista (`subList`).
* **Entrega 2**: Utilizar JPA `PageRequest` en la consulta a PostgreSQL para una paginación nativa a nivel de BD.
* **API Web**: GET `/api/v1/catalogo?page=0&size=20`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear abstracción `PaginaDominio<T>` y VO `Paginacion`.
- [ ] 2. (TDD) Probar las validaciones de límites numéricos de paginación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear el DTO `CatalogoPaginadoResponse`.
- [ ] 4. (TDD) Pruebas para `VerCatalogoInteractor`.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el paginado manual en memoria (`JsonGateway`).
- [ ] 7. Implementar el endpoint en el controlador.
