# Diseño Técnico e Implementación: A-02 - Dashboard métricas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-02**: 
> *"Como admin, quiero ver KPIs del día en el dashboard para monitorear la actividad de la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `DashboardKpi`.
* **Value Objects**: `Metrica` (Nombre, Valor numérico, Variación porcentual vs ayer).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerMetricasDashboardUseCase`.
* **Output Ports (Gateways)**: Múltiples Gateways (ej. `IUsuarioGateway` para nuevas altas, `IPedidoGateway` para ingresos totales).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El Interactor actúa como un "Orquestador de Lectura Global". Inyecta varios Gateways para compilar la "foto del día" de toda la plataforma sin acoplar los módulos entre sí.

### B. Domain Driven Design (DDD)
* `DashboardKpi` es un DTO de lectura (proyección) puro. No tiene lógica de negocio transaccional, solo empaqueta los indicadores críticos.

### C. Principios SOLID
* **Facade**: Simplifica la vista de la capa web. El frontend solo llama a un endpoint y recibe todos los KPIs juntos.

### D. Test-Driven Development (TDD)
* Mockear todos los Gateways para que devuelvan valores estáticos (ej. 10 altas nuevas, $500 en ventas) y asegurar que el `DashboardKpi` los consolida correctamente sin fallar.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: El Interactor llama a los métodos de lectura general (ej. `countUsuarios()`, `sumTotalVentasHoy()`) leyendo desde los múltiples JSON.
* **Entrega 2**: Implementar una vista materializada (Materialized View) en PostgreSQL o Jobs que actualicen estos KPIs en una tabla en Redis para no colapsar la base de datos principal al cargar el dashboard.
* **API Web**: GET `/api/v1/admin/dashboard/kpis` (Protegido con JWT de Admin).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el objeto consolidado `DashboardKpi` y el VO `Metrica`.
- [ ] 2. Validar que las interfaces de los Gateways exponen métodos de conteo diario.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `VerMetricasDashboardInteractor`.
- [ ] 4. (TDD) Pruebas de orquestación de lecturas paralelas.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar lógica de conteo en memoria para la Entrega 1.
- [ ] 6. Controlador REST con validación `@PreAuthorize("hasRole('ADMIN')")`.
