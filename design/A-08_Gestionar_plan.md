# Diseño Técnico e Implementación: A-08 - Curaduría contenido

Este documento define el plan de implementación detallado para la Historia de Usuario **A-08**: 
> *"Como admin, quiero gestionar las categorías de libros para mantener el catálogo bien organizado"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CategoriaCatalogo`.
* **Value Objects**: `NombreCategoria` (único, sin caracteres especiales inválidos), `EstadoCategoria` (ACTIVA, INACTIVA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarCuraduriaContenidoUseCase`.
* **Output Port (Gateway)**: `ICategoriaGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Este caso de uso permite crear, editar y deshabilitar categorías. El dominio asegura que no se puedan crear dos categorías con el mismo nombre normalizado (ej. "Ciencia Ficción" y "ciencia ficcion").

### B. Domain Driven Design (DDD)
* Si un administrador intenta desactivar una categoría que actualmente tiene libros vinculados, el dominio puede rechazar la acción (`CategoriaEnUsoException`) o forzar una migración de los libros a una categoría "General".
* La creación de una categoría requiere que su `NombreCategoria` pase las reglas del Value Object.

### C. Principios SOLID
* **OCP**: La gestión de categorías se aísla de la gestión de libros. El catálogo solo hace referencia a las categorías activas.

### D. Test-Driven Development (TDD)
* Mockear el Gateway retornando que la categoría "Fantasía" ya existe. Intentar crear una nueva llamada " fAntasÍa  " y verificar que el Value Object la normaliza y el interactor lanza `CategoriaDuplicadaException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `categorias.json`.
* **Entrega 2**: Tabla paramétrica `categorias` en PostgreSQL.
* **API Web**: 
  - POST `/api/v1/admin/categorias`
  - PUT `/api/v1/admin/categorias/{id}`
  - PATCH `/api/v1/admin/categorias/{id}/estado`

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `CategoriaCatalogo` y VOs asociados.
- [ ] 2. (TDD) Pruebas de normalización de cadenas en `NombreCategoria`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor para operaciones CRUD administrativas de categorías.
- [ ] 4. (TDD) Pruebas de validación de duplicados.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el Gateway JSON de categorías.
- [ ] 6. Controlador REST con validación de roles de Admin.
