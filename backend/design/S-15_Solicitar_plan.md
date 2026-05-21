# Diseño Técnico e Implementación: S-15 - Finanzas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-15**: 
> *"Como vendedor, quiero solicitar retiro de fondos para acceder a mis ganancias acumuladas"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SolicitudRetiro` y `BilleteraVendedor`.
* **Value Objects**: `IdVendedor`, `MontoRetiro`, `CuentaDestino`, `EstadoRetiro` (PENDIENTE, PROCESANDO, COMPLETADO, RECHAZADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ISolicitarRetiroFinanzasUseCase`.
* **Output Ports (Gateways)**: `IRetiroGateway`, `IBilleteraGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica que define cuál es el saldo mínimo retirable o cuánto se puede retirar reside estrictamente en el dominio (`BilleteraVendedor`).

### B. Domain Driven Design (DDD)
* Si el vendedor intenta retirar más de lo que tiene disponible, la entidad lanza `FondosInsuficientesException`.
* La `SolicitudRetiro` nace en estado `PENDIENTE`.

### C. Principios SOLID
* **SRP**: Separar el caso de uso de ver ganancias (S-07) de la mutación que implica solicitar un retiro de fondos.

### D. Test-Driven Development (TDD)
* Probar el Agregado `BilleteraVendedor`. Dado un saldo de $100, solicitar un retiro de $150 debe fallar, pero un retiro de $50 debe ser exitoso y bloquear temporalmente esos $50.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer y actualizar saldo en `billeteras.json`. Guardar la solicitud en `retiros.json`.
* **Entrega 2**: Implementar base de datos transaccional estricta (PostgreSQL) usando `@Transactional` y bloqueos pesimistas (`PESSIMISTIC_WRITE`) para evitar condiciones de carrera (doble gasto).
* **API Web**: POST `/api/v1/vendedores/{sellerId}/finanzas/retiros`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear `BilleteraVendedor` y `SolicitudRetiro`.
- [ ] 2. (TDD) Pruebas de reglas de fondos suficientes.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Request (Monto a retirar).
- [ ] 4. Implementar `SolicitarRetiroFinanzasInteractor`.
- [ ] 5. (TDD) Pruebas de orquestación de billetera y solicitud.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el control concurrente y persistencia en los Gateways JSON.
- [ ] 7. Implementar Controlador REST.
