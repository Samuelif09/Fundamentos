# Diseño Técnico e Implementación: C-07 - Post-Compra

Este documento define el plan de implementación detallado para la Historia de Usuario **C-07**: 
> *"Como comprador, quiero recibir confirmación de compra inmediata para tener constancia del pedido"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Pedido` (en estado COMPLETADO).
* **Value Objects**: `ReciboCompra`, `EmailDestino`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRecibirPostCompraUseCase`.
* **Output Port (Gateway)**: `INotificacionGateway` (o `IEmailGateway`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso puede estar suscrito a un evento de dominio (`PedidoCompletadoEvent`) en lugar de ser llamado imperativamente, desacoplando el checkout de la notificación.

### B. Domain Driven Design (DDD)
* Un `ReciboCompra` es inmutable y contiene un resumen de los items y el total cobrado.

### C. Principios SOLID
* **Observer Pattern**: Utilizar un despachador de eventos donde `NotificadorComprador` es un listener de la compra.

### D. Test-Driven Development (TDD)
* Verificar que el `INotificacionGateway` es invocado exactamente 1 vez cuando se procesa el evento.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Imprimir por consola (Logger) el "Email simulado" utilizando un `NotificacionLoggerGateway`.
* **Entrega 2**: Implementar `JavaMailSenderGateway` para enviar un correo real con plantilla HTML.
* **API Web**: No requiere endpoint propio; es un proceso asíncrono disparado internamente.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Definir los eventos de dominio (`PedidoCompletadoEvent`).
- [ ] 2. Definir interfaz `INotificacionGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Listener/Interactor `RecibirPostCompraInteractor`.
- [ ] 4. (TDD) Pruebas comprobando la invocación del Gateway al recibir el evento.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Configurar EventPublisher de Spring (`@EventListener`).
- [ ] 6. Implementar el envío a consola/log para la Entrega 1.
