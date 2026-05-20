# Diseño Técnico e Implementación: V-06 - Comparte

Este documento define el plan de implementación detallado para la Historia de Usuario **V-06**: 
> *"Como visitante, quiero compartir un libro por enlace para recomendar a otros usuarios"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**, cumpliendo con los requerimientos técnicos del proyecto OpenLib Market (Java 25, Spring Boot 4x). 

---

## 1. Análisis y Modelado de Dominio (DDD & Clean Architecture)

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `EnlaceCompartir` (valida que el formato de la URL construida sea un hipervínculo seguro y válido), `Isbn`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ICompartirComparteUseCase`.
* **Output Port (Gateway)**: Interfaz `ILibroGateway` (para verificar que el libro existe antes de generar el enlace).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica de generación del formato del enlace (ej. agregar parámetros UTM para analíticas de OpenLib) debe residir en el dominio, independiente del framework web.

### B. Domain Driven Design (DDD)
* Si el libro solicitado para compartir está en estado `INACTIVO` o fue eliminado, el dominio debe lanzar un `LibroNoDisponibleException`.

### C. Principios SOLID
* **SRP**: El Interactor solo recibe el ID del libro, valida su existencia, y delega al Value Object la construcción del enlace.

### D. Test-Driven Development (TDD)
* Test unitario del `EnlaceCompartir` garantizando que la URL base se concatena correctamente con el identificador único del libro.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1 (Persistencia)**: No requiere persistencia de escritura. Solo lectura usando el `DetalleJsonGateway` existente para verificar el libro.
* **Entrega 2**: Implementar analíticas: registrar en la BD cada vez que se genera un enlace (conteo de compartidos) mediante un `MetricasJpaGateway`.
* **API Web**: GET `/api/v1/libros/{id}/compartir`. Devuelve un JSON con la URL generada.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `EnlaceCompartir`.
- [ ] 2. (TDD) Implementar pruebas para la generación de la URL en el VO.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear `ICompartirComparteUseCase` y su DTO de salida (`EnlaceDto`).
- [ ] 4. (TDD) Pruebas para `CompartirComparteInteractor` mockeando el Gateway del libro.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Reutilizar la lectura JSON para verificar existencia.
- [ ] 7. Implementar `CompartirController`.
- [ ] 8. (TDD) Test de integración del endpoint con `MockMvc`.
