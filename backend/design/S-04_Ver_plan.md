# Diseño Técnico e Implementación: S-04 - Gestión inventario

Este documento define el plan de implementación detallado para la Historia de Usuario **S-04**: 
> *"Como vendedor, quiero ver todos mis libros publicados para gestionar mi catálogo eficientemente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CatalogoVendedor` (colección de Libros asociados a un Vendedor).
* **Value Objects**: `IdVendedor`, `Paginacion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerInventarioUseCase`.
* **Output Port (Gateway)**: `ILibroGateway` (extender `listarPorVendedorId`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso garantiza el aislamiento de datos multi-tenant básico: el vendedor solo puede ver sus propios registros, asegurando esto mediante el ID extraído del contexto de seguridad.

### B. Domain Driven Design (DDD)
* Retorna un DTO consolidado que incluye el estado del libro (Ej. Activo, Pausado, Bloqueado) y estadísticas básicas de stock.

### C. Principios SOLID
* **SRP**: Caso de uso dedicado exclusivamente a consultar la grilla de administración del vendedor, separándolo del catálogo público (C-02).

### D. Test-Driven Development (TDD)
* Mockear la BD para retornar un libro de otro vendedor y garantizar que el Interactor filtra o falla si ocurre una colisión.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Filtrar la lista completa de `libros.json` comparando el `IdVendedor`.
* **Entrega 2**: Implementar una consulta JPA optimizada `SELECT l FROM Libro l WHERE l.idVendedor = :idVendedor`.
* **API Web**: GET `/api/v1/vendedores/{id}/libros`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Asegurar validación en `IdVendedor`.
- [ ] 2. Definir la firma `listarPorVendedorId` en el puerto de salida.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO especializado para la vista del vendedor.
- [ ] 4. (TDD) Pruebas de orquestación en el Interactor.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el filtro en `LibroJsonGateway`.
- [ ] 7. Implementar el Controlador REST protegiendo la ruta.
