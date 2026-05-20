# Diseño Técnico e Implementación: C-14 - Detalle libro

Este documento define el plan de implementación detallado para la Historia de Usuario **C-14**: 
> *"Como comprador, quiero ver libros relacionados para explorar más opciones similares"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo` / `Libro`.
* **Value Objects**: `CriterioSimilitud` (que puede encapsular la categoría, el autor o las etiquetas del libro actual).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerDetalleLibroRelacionadoUseCase`.
* **Output Port (Gateway)**: `ICatalogoGateway` (extender con `buscarRelacionados(IdLibro, CriterioSimilitud, Limite)`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La regla sobre qué hace que un libro sea "relacionado" (ej. misma categoría y autor) pertenece al dominio. El caso de uso define el `CriterioSimilitud` y se lo pasa al Gateway.

### B. Domain Driven Design (DDD)
* Retornar una colección inmutable pequeña (ej. máximo 5 libros). No debe devolver el mismo libro desde el cual se originó la búsqueda.

### C. Principios SOLID
* **OCP**: En el futuro, el `CriterioSimilitud` puede evolucionar de una búsqueda por categoría a un motor de recomendación basado en IA sin cambiar la interfaz del Gateway.

### D. Test-Driven Development (TDD)
* Mockear el catálogo para asegurar que al solicitar libros relacionados del Libro A, la lista de resultados no incluya al Libro A, pero sí a otros de su misma categoría.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `CatalogoJsonGateway` filtra los libros que tengan la misma categoría del libro base, excluyendo el ID original, y limitando a 5 resultados.
* **Entrega 2**: Consulta JPA: `SELECT l FROM Libro l WHERE l.categoria = :categoria AND l.id != :idLibro LIMIT 5`.
* **API Web**: GET `/api/v1/libros/{id}/relacionados`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Definir las reglas en `CriterioSimilitud`.
- [ ] 2. Ampliar el puerto de salida `ICatalogoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear el Response DTO para libros relacionados (resumen de libro).
- [ ] 4. (TDD) Escribir pruebas del `VerDetalleLibroRelacionadoInteractor`.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el algoritmo de filtrado en `JsonGateway`.
- [ ] 7. Crear el controlador REST.
