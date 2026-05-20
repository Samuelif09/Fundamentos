# Diseño Técnico e Implementación: S-09 - Mi tienda

Este documento define el plan de implementación detallado para la Historia de Usuario **S-09**: 
> *"Como vendedor, quiero tener una página de tienda propia para mostrar todos mis libros publicados"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `PerfilTienda`.
* **Value Objects**: `IdVendedor`, `NombreTienda`, `UrlAmigable` (slug), `CatalogoPublico` (lista de libros activos).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ITenerMiTiendaUseCase`.
* **Output Ports (Gateways)**: `IVendedorGateway`, `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso compila dos fuentes (la info del vendedor y sus libros activos) en un DTO optimizado para lectura (`TiendaPublicaDto`).

### B. Domain Driven Design (DDD)
* `PerfilTienda` solo contiene libros en estado `ACTIVO`. Los libros en borrador, bloqueados o sin stock (si aplica a productos digitales) no se indexan aquí.

### C. Principios SOLID
* **Facade**: El Interactor centraliza múltiples consultas para evitar que el frontend deba hacer 5 llamadas distintas para pintar la pantalla de una tienda.

### D. Test-Driven Development (TDD)
* Mockear un vendedor y sus 4 libros (3 activos, 1 borrador). Asegurar que el Interactor retorna la Tienda con solo los 3 libros activos.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Consulta secuencial a `vendedores.json` y `libros.json`.
* **Entrega 2**: Implementar caché temporal (Redis) para las páginas de las tiendas, ya que son URLs públicas de alta concurrencia.
* **API Web**: GET `/api/v1/tiendas/{slugTienda}`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `UrlAmigable` (generar un slug a partir del nombre sin caracteres especiales).
- [ ] 2. Definir puertos necesarios.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de `PerfilTienda`.
- [ ] 4. (TDD) Pruebas de filtrado de estados de libros.
- [ ] 5. Implementar el Interactor consolidado.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Añadir búsqueda por slug (`findByUrlAmigable`) en el Gateway.
- [ ] 7. Implementar el Controlador REST público (sin autenticación requerida para ver).
