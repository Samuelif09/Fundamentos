# Diseño Técnico e Implementación: A-05 - Curaduría contenido

Este documento define el plan de implementación detallado para la Historia de Usuario **A-05**: 
> *"Como admin, quiero revisar libros pendientes de aprobación para publicar solo contenido de calidad"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CatalogoPendiente`.
* **Value Objects**: `EstadoLibro` (EN_REVISION), `Paginacion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRevisarCuraduríaContenidoUseCase`.
* **Output Port (Gateway)**: `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El administrador tiene una vista especializada del catálogo. Mientras el visitante ve libros `ACTIVO` (C-02), el administrador solicita libros en estado `EN_REVISION`.

### B. Domain Driven Design (DDD)
* Retornar un `LibroParaRevisionDto` que contenga no solo la info del libro, sino metadatos sobre el vendedor para dar contexto al administrador.

### C. Principios SOLID
* **SRP**: Caso de uso dedicado a listar la "bandeja de entrada" del curador, sin mezclar con la lógica pública.

### D. Test-Driven Development (TDD)
* Mockear un Gateway que tiene 10 libros (3 activos, 5 en revisión, 2 rechazados). Asegurar que el Interactor retorna exactamente los 5 en revisión.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Iterar sobre `libros.json`, filtrar por `estado == "EN_REVISION"` y paginar manualmente.
* **Entrega 2**: Consulta JPA `SELECT l FROM Libro l WHERE l.estado = 'EN_REVISION'`.
* **API Web**: GET `/api/v1/admin/curaduria/libros-pendientes?page=0&size=20`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Asegurar la definición del estado de revisión en el Dominio.
- [ ] 2. Definir método `listarPorEstado` en el puerto de salida.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de vista administrativa.
- [ ] 4. (TDD) Pruebas de filtrado por estado.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el filtrado y paginación en `JsonGateway`.
- [ ] 7. Controlador REST protegido.
