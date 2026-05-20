# Diseño Técnico e Implementación: A-22 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-22**: 
> *"Como admin, quiero módulo antifraude para detección automática de transacciones sospechosas"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `EvaluacionFraude`.
* **Value Objects**: `IdPedido`, `RiesgoTransaccion` (0-100), `MotivoAlerta`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IEvaluarGestionVentasUseCase`.
* **Output Ports (Gateways)**: `IAntifraudeGateway`, `IPedidoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Total aislamiento del Checkout (C-06). Cuando ocurre la transacción, el `IEvaluarGestionVentasUseCase` se dispara. Las reglas heurísticas de fraude (muchos intentos fallidos en IP, compras masivas repentinas) pueden ser definidas en el Dominio o delegadas a un proveedor de infraestructura (Stripe Radar / Kount).

### B. Domain Driven Design (DDD)
* Si `RiesgoTransaccion` es extremo, la entidad dispara un evento para suspender la cuenta del comprador automáticamente y revertir el cargo.

### C. Principios SOLID
* **Strategy**: Podemos combinar "Reglas Heurísticas Propias" (Domain) + "Score Externo" (Infraestructura) implementando estrategias conjuntas.

### D. Test-Driven Development (TDD)
* Mockear un pedido válido, pero la API antifraude retorna un riesgo de 95 (Tarjeta reportada robada). Verificar que el Interactor bloquea el pedido y alerta al admin.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `AntifraudeDummyGateway` bloquea cualquier transacción superior a $1,000 asumiendo riesgo.
* **Entrega 2**: Implementación de Stripe Radar o reglas heurísticas usando caché de Redis para contar intentos de IP.
* **API Web**: Integración síncrona/asíncrona durante la fase de pago.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado de `EvaluacionFraude`.
- [ ] 2. Definir puertos del módulo antifraude.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar orquestación en el Interactor.
- [ ] 4. (TDD) Test de respuesta a fraude y prevención.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar validaciones limitadas locales en la Entrega 1.
- [ ] 6. Enlazar el interceptor al flujo de transacciones.
