# Diseño Técnico e Implementación: A-15 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-15**: 
> *"Como admin, quiero gestionar reembolsos solicitados por compradores para proteger sus derechos"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SolicitudReembolso` (asociado a un `Pedido`).
* **Value Objects**: `IdPedido`, `MontoReembolso`, `Motivo`, `EstadoReembolso` (PENDIENTE, APROBADO, DENEGADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarReembolsosUseCase`.
* **Output Ports (Gateways)**: `IReembolsoGateway`, `IPedidoGateway`, `IPasarelaPagoGateway` (para ejecutar el reverso del cargo).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La ejecución real de la devolución de dinero es responsabilidad del `IPasarelaPagoGateway` (Stripe, PayPal). El dominio se encarga de cambiar el estado financiero interno del pedido.

### B. Domain Driven Design (DDD)
* No se puede aprobar un reembolso por un monto mayor al `MontoTotal` del `Pedido` original.
* Una vez aprobado, el estado del pedido o de la licencia de acceso del usuario al libro debe ser revocado (disparando un evento `ReembolsoAprobadoEvent`).

### C. Principios SOLID
* **SRP**: Este caso de uso solo evalúa y ejecuta la orden de reembolso. La revocación del libro digital la procesará el módulo de la biblioteca escuchando el evento.

### D. Test-Driven Development (TDD)
* Intentar aprobar un reembolso donde el monto solicitado supera el monto del pedido. Asegurar que lanza `MontoReembolsoInvalidoException`.
* Verificar que al aprobarse se invoca al `IPasarelaPagoGateway`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar el estado en `reembolsos.json` y marcar el pedido como "REEMBOLSADO" en `pedidos.json`.
* **Entrega 2**: Implementar el llamado real a la API de reembolsos de Stripe/PayPal e invalidar la transacción en BD.
* **API Web**: POST `/api/v1/admin/ventas/reembolsos/{id}/aprobar`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `SolicitudReembolso` y la validación de montos contra el Pedido.
- [ ] 2. Definir evento de dominio `ReembolsoAprobadoEvent`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor de gestión de reembolsos.
- [ ] 4. (TDD) Pruebas de reglas de validación monetaria.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar actualización en el JSON Gateway.
- [ ] 6. Controlador REST para aprobar o denegar solicitudes.
