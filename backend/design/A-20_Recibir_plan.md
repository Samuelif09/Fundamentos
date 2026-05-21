# Diseño Técnico e Implementación: A-20 - Dashboard métricas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-20**: 
> *"Como admin, quiero alertas automáticas de anomalías para reaccionar rápidamente ante problemas críticos"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReglaAnomalia` y `Alerta`.
* **Value Objects**: `MetricaObjetivo` (Ej: TRÁFICO, FALLOS_PAGO), `UmbralCritico`, `EstadoAlerta`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IEvaluarAnomaliaUseCase` (o un Observer).
* **Output Ports (Gateways)**: `IMetricasGateway`, `INotificacionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El motor de reglas (Domain Service) evalúa si un dato reciente supera el `UmbralCritico`. Si lo hace, dispara un caso de uso interno que contacta a infraestructura para enviar un webhook/email.

### B. Domain Driven Design (DDD)
* Las reglas son entidades de negocio (`ReglaAnomalia`). Por ejemplo: "Si la tasa de transacciones fallidas supera el 15% en 1 hora, emitir Alerta".

### C. Principios SOLID
* **Observer**: Se implementa a través de la escucha de los eventos o métricas periódicas recolectadas por un Cron Job (Infraestructura), desacoplando el monitor del sistema principal.

### D. Test-Driven Development (TDD)
* Mockear la métrica de fallos en 20%. Teniendo la regla configurada en 15%, validar que el Interactor orquesta correctamente la generación del objeto `Alerta` y la llama al servicio de mensajería.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Un `@Scheduled` en Spring Boot evalúa las métricas crudas de los archivos JSON cada 5 minutos e imprime alertas por consola.
* **Entrega 2**: Implementar notificaciones reales a Slack o correo usando Webhooks configurados en la infraestructura.
* **API Web**: Tarea Background interna, no depende de un endpoint frontal excepto para configurar las reglas (Opcional).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear VOs para reglas matemáticas y umbrales.
- [ ] 2. Crear `ReglaAnomaliaDomainService`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar orquestación mediante `EvaluarAnomaliaInteractor`.
- [ ] 4. (TDD) Escribir pruebas de validación matemática de umbrales.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Configurar tarea periódica programada.
- [ ] 6. Implementar Webhooks o Loggers para alertamiento.
