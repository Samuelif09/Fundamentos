# Diseño Técnico e Implementación: V-05 - Pago

Este documento define el plan de implementación detallado para la Historia de Usuario **V-05**: 
> *"Como visitante, quiero realizar compra directa con un click para agilizar el proceso"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Pedido` o `OrdenCompra`.
* **Value Objects**: `MetodoPago` (ej. Tarjeta enmascarada), `EstadoPedido` (Enum: PENDIENTE, PAGADO, FALLIDO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRealizarPagoUseCase`.
* **Output Ports (Gateways)**: `IPedidoGateway`, `IPagoExternoGateway` (para procesar el cobro).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La pasarela de pagos real (Stripe, PayPal) queda completamente en la capa de Infraestructura; el dominio solo ve `IPagoExternoGateway.procesar(monto, metodo)`.

### B. Domain Driven Design (DDD)
* El `Pedido` nace en estado `PENDIENTE`. Solo cambia a `PAGADO` mediante un evento de dominio exitoso.

### C. Principios SOLID
* **Facade Pattern / SRP**: El `RealizarPagoInteractor` orquesta (1) vaciar el carrito, (2) generar el pedido, (3) procesar pago.
* **LSP**: Diferentes métodos de pago deben poder ser inyectados sin alterar el proceso central.

### D. Test-Driven Development (TDD)
* Simular un Gateway de pago que rechaza la tarjeta (retorna false) y asegurar que el pedido queda en estado `FALLIDO` y no se vacía el carrito.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: El `PagoExternoGateway` será un "Dummy" que siempre aprueba la transacción. Persistir el pedido en `pedidos.json`.
* **Entrega 2**: Implementar lógica transaccional (`@Transactional` en el adaptador JPA) para evitar inconsistencias si falla la red al cobrar.
* **API Web**: POST `/api/v1/pedidos/checkout-directo`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `Pedido` y Enum `EstadoPedido`.
- [ ] 2. Definir puertos `IPedidoGateway` y `IPagoExternoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `RealizarPagoInteractor`.
- [ ] 4. (TDD) Pruebas de orquestación (pago exitoso vs pago rechazado).

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar Dummy Adapter para pagos en la Entrega 1.
- [ ] 6. Implementar guardado en `pedidos.json`.
- [ ] 7. Implementar el controlador web y mapear excepciones a 400 Bad Request o 402 Payment Required.
