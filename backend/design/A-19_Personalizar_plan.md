# Diseño Técnico e Implementación: A-19 - Dashboard métricas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-19**: 
> *"Como admin, quiero un dashboard personalizable con widgets para adaptar mi vista de trabajo diaria"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ConfiguracionDashboard`.
* **Value Objects**: `IdAdmin`, `ListaWidgets` (Colección de objetos con `TipoWidget`, `PosicionX`, `PosicionY`, `Tamaño`).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IPersonalizarDashboardMetricasUseCase`.
* **Output Port (Gateway)**: `IConfiguracionAdminGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El layout visual (posiciones, tamaños) se gestiona en la capa de interfaz, pero la persistencia de las preferencias del usuario requiere un caso de uso específico que solo actúe como "Save/Load" (CRUD de preferencias).

### B. Domain Driven Design (DDD)
* `ListaWidgets` debe asegurar que los widgets referenciados sean válidos dentro de los enumeradores del dominio. No permite inyectar "Widgets fantasmas".

### C. Principios SOLID
* **OCP**: La estructura de `Widget` debe ser lo suficientemente flexible para soportar nuevos tipos de gráficos sin alterar el esquema de base de datos.

### D. Test-Driven Development (TDD)
* Probar el guardado de un diseño vacío y uno con 5 widgets, asegurando que el Interactor valida la lista correctamente.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar las preferencias en `config_dashboards.json` asociado por `IdAdmin`.
* **Entrega 2**: Guardar la configuración en una columna tipo JSONB en PostgreSQL vinculada a la tabla del administrador.
* **API Web**: PUT `/api/v1/admin/{id}/preferencias/dashboard`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado de configuración de UI.
- [ ] 2. (TDD) Pruebas de validación de elementos superpuestos o tipos inválidos.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `PersonalizarDashboardMetricasInteractor`.
- [ ] 4. Crear DTOs de entrada/salida para el Layout.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el almacenamiento de estructura JSON.
- [ ] 6. Controlador REST para actualización.
