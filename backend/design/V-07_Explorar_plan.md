# Diseño Técnico e Implementación: V-07 - Búsqueda

Este documento define el plan de implementación detallado para la Historia de Usuario **V-07**: 
> *"Como visitante, quiero explorar libros por tendencias para descubrir novedades populares"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Catalogo`.
* **Value Objects**: `CriterioTendencia` (Enum o VO que define si es "Más Vendidos", "Mejor Calificados", "Nuevos").

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IExplorarBusquedaUseCase`.
* **Output Port (Gateway)**: Interfaz `ITendenciaGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El algoritmo que define qué es "tendencia" (ej. ponderación de ventas recientes vs calificaciones) es una regla de negocio y pertenece al dominio/caso de uso, no a una consulta SQL compleja acoplada.

### B. Domain Driven Design (DDD)
* Retornar una lista inmutable de `Libro` ordenada según la política de tendencias del negocio.

### C. Principios SOLID
* **OCP/Strategy**: Podemos inyectar diferentes estrategias de cálculo de tendencias (basado en vistas, basado en ventas) implementando el patrón `Strategy` en el dominio.

### D. Test-Driven Development (TDD)
* Mockear el Gateway para que devuelva una lista desordenada y probar que el Interactor la ordena y recorta (Top 10) correctamente.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `TendenciaJsonGateway` lee todos los libros, y el Interactor los ordena por fecha de publicación o un campo ficticio de "vistas".
* **Entrega 2**: `TendenciaJpaGateway` que aproveche índices en PostgreSQL o Redis para traer los "Top K" rápidamente sin cargar toda la base a memoria.
* **API Web**: GET `/api/v1/catalogo/tendencias`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `CriterioTendencia`.
- [ ] 2. Definir interfaz `ITendenciaGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar lógica de filtrado/ordenamiento en `ExplorarBusquedaInteractor`.
- [ ] 4. (TDD) Pruebas para asegurar que solo se devuelve el Top N libros (ej. 10 máximos).

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar lectura y recolección de datos en el `JsonGateway`.
- [ ] 6. Implementar el endpoint y documentarlo (Swagger/OpenAPI).
