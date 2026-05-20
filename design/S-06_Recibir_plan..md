# Diseño Técnico e Implementación: S-06 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-06**: 
> *"Como vendedor, quiero recibir notificación inmediata de nueva venta para estar informado"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `NotificacionVendedor`.
* **Value Objects**: `IdVendedor`, `IdPedido`, `MensajeNotificacion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRecibirVentasUseCase` (Listener).
* **Output Port (Gateway)**: `INotificacionGateway`, `ILibroGateway` (para saber a quién pertenece el libro vendido).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Totalmente desacoplado del flujo de pago del Comprador. El Interactor se activa al escuchar el evento de dominio `PedidoCompletadoEvent` emitido en la historia C-07.

### B. Domain Driven Design (DDD)
* El dominio procesa el evento del pedido (que contiene múltiples items), agrupa los libros por `IdVendedor` propietario, y emite las notificaciones correspondientes a cada uno.

### C. Principios SOLID
* **Observer**: La arquitectura reacciona a los eventos en lugar de estar acoplada estructuralmente (`@EventListener`).

### D. Test-Driven Development (TDD)
* Simular un `PedidoCompletadoEvent` que incluye 3 libros: 2 del Vendedor X y 1 del Vendedor Y. Verificar que el Gateway de notificaciones es invocado exactamente 2 veces con el consolidado para cada vendedor.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `NotificacionLoggerGateway` imprime el mensaje simulando el envío.
* **Entrega 2**: Implementar notificaciones Push (Firebase Cloud Messaging) o Emails transaccionales con `JavaMailSender`.
* **API Web**: Flujo interno impulsado por eventos, no expone un endpoint público para ejecutar esta acción.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Modelar la entidad `NotificacionVendedor`.
- [ ] 2. Configurar la dependencia al evento `PedidoCompletadoEvent`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Listener `RecibirVentasInteractor`.
- [ ] 4. (TDD) Pruebas de enrutamiento de notificaciones por vendedor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Configurar el publicador/receptor de eventos de Spring.
- [ ] 6. Implementar loggers para simular el envío asíncrono.
