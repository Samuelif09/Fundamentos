# Diseño Técnico e Implementación: S-02 - Publicar libro

Este documento define el plan de implementación detallado para la Historia de Usuario **S-02**: 
> *"Como vendedor, quiero publicar un libro con título, descripción y precio para que compradores lo encuentren"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `Isbn` (generado o proporcionado), `Titulo` (no vacío), `Sinopsis`, `Precio` (mayor o igual a cero), `IdVendedor`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IPublicarLibroUseCase`.
* **Output Port (Gateway)**: `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso garantiza que ningún libro puede ser creado sin pertenecer a un `IdVendedor` válido, vinculando la oferta a la tienda.

### B. Domain Driven Design (DDD)
* Un `Libro` recién publicado arranca con estado `ACTIVO` (o `BORRADOR` si se prefiere). 
* Si se intenta poner un precio negativo, el dominio lanza `PrecioInvalidoException`.

### C. Principios SOLID
* **OCP**: La estructura del libro debe poder ampliarse después para añadir formatos (PDF/EPUB) sin romper la publicación básica.

### D. Test-Driven Development (TDD)
* Validar que la creación de la entidad `Libro` arroja errores si falta el título o el precio, garantizando que al Gateway solo llegan objetos consistentes.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Añadir el objeto a `libros.json`.
* **Entrega 2**: Guardar en base de datos transaccional usando JPA.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/libros`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender entidad `Libro` si es necesario, asegurando reglas de creación.
- [ ] 2. Verificar puerto `ILibroGateway.guardar(Libro)`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Publicación de Libro.
- [ ] 4. (TDD) Tests de validación de negocio en `PublicarLibroInteractor`.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Asegurar escritura en `LibroJsonGateway`.
- [ ] 7. Crear Controlador REST asegurando validación del vendedor.
