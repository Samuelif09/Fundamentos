# Diseño Técnico e Implementación: C-21 - Soporte

Este documento define el plan de implementación detallado para la Historia de Usuario **C-21**: 
> *"Como comprador, quiero reportar un libro o reseña por contenido inapropiado para mantener la calidad de la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReporteContenido`.
* **Value Objects**: `IdDenunciante`, `ElementoReportado` (enum: LIBRO, RESENA), `IdElemento`, `MotivoReporte`, `EstadoReporte` (PENDIENTE, REVISADO, DESCARTADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IReportarSoporteUseCase`.
* **Output Port (Gateway)**: `IReporteGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El reporte se procesa independientemente del módulo de catálogo. La creación del reporte no bloquea el contenido automáticamente a menos que se cruce un umbral de reportes.

### B. Domain Driven Design (DDD)
* Un `ReporteContenido` nace con estado `PENDIENTE`.
* Validar que un mismo usuario no pueda reportar el mismo elemento más de una vez consecutiva (prevención de spam).

### C. Principios SOLID
* **OCP**: La lógica puede extenderse luego para soportar reportes hacia perfiles de Vendedores sin alterar la estructura base del reporte.

### D. Test-Driven Development (TDD)
* Pruebas del interactor asegurando que si el usuario ya reportó ese ID, se lance `ReporteDuplicadoException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar los reportes en un archivo `reportes.json` con el estado `PENDIENTE`.
* **Entrega 2**: Tabla de reportes en PostgreSQL consumible por el módulo Admin (Dashboard).
* **API Web**: POST `/api/v1/reportes`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `ReporteContenido` e `IdElemento`.
- [ ] 2. Definir interfaz `IReporteGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Request (Tipo de elemento, ID, Motivo).
- [ ] 4. (TDD) Escribir pruebas simulando un reporte nuevo y un reporte duplicado.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar escritura y validación de duplicados en el `JsonGateway`.
- [ ] 7. Implementar el Controlador REST.
