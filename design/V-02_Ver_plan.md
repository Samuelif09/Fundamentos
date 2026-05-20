# Diseño Técnico e Implementación: V-02 - Detalle

Este documento define el plan de implementación detallado para la Historia de Usuario **V-02**: 
> *"Como visitante, quiero ver portada, sinopsis y precio del libro para evaluar si me interesa"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `Isbn` (identificador único), `Precio` (no puede ser negativo), `UrlPortada` (valida formato de enlace), `Sinopsis`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerDetalleUseCase`.
* **Output Port (Gateway)**: Interfaz `IDetalleGateway` (método `buscarPorId(Isbn)`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica de ensamblar el modelo completo del libro ocurre en el Interactor; la capa Web solo recibe el DTO de lectura.

### B. Domain Driven Design (DDD)
* Asegurar que la entidad `Libro` nunca se construya si no tiene al menos un Título, Sinopsis y Precio válido.

### C. Principios SOLID
* **SRP**: El `VerDetalleInteractor` solo se encarga de recuperar los datos del libro; si no existe, lanza una excepción de dominio (`LibroNoEncontradoException`).

### D. Test-Driven Development (TDD)
* Escribir el test que asegura que al buscar un ID inexistente, el Interactor lanza la excepción correspondiente.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1 (Persistencia)**: `DetalleJsonGateway` busca por ID exacto en `libros.json`.
* **Entrega 2 (Persistencia)**: `DetalleJpaGateway` busca mediante `findById` en la base de datos relacional.
* **API Web**: `DetalleController` expone GET `/api/v1/libros/{id}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Definir Entidad `Libro` y Value Objects (`Precio`, `Isbn`).
- [ ] 2. Definir interfaz `IDetalleGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de salida con los campos requeridos (portada, sinopsis, precio).
- [ ] 4. (TDD) Pruebas para `VerDetalleInteractor` (caso exitoso y caso no encontrado).
- [ ] 5. Implementar `VerDetalleInteractor`.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar lectura por ID en el `JsonGateway`.
- [ ] 7. Implementar `DetalleController`.
- [ ] 8. Configurar manejo global de excepciones (`@ControllerAdvice`) para el error 404 Not Found.
