# Diseño Técnico e Implementación: A-18 - Soporte

Este documento define el plan de implementación detallado para la Historia de Usuario **A-18**: 
> *"Como admin, quiero ver y gestionar disputas entre comprador y vendedor para resolver conflictos"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Disputa`.
* **Value Objects**: `IdPedido`, `IdComprador`, `IdVendedor`, `Resolucion` (FAVOR_COMPRADOR, FAVOR_VENDEDOR, EMPATE), `EstadoDisputa` (ABIERTA, EN_MEDIACION, RESUELTA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarSoporteUseCase`.
* **Output Ports (Gateways)**: `IDisputaGateway`, `IPedidoGateway`, `INotificacionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La disputa es un proceso formal de escalamiento. El interactor evalúa la resolución dictaminada por el administrador y, si es a favor del comprador, emite un evento interno (`ReembolsoSolicitadoPorDisputaEvent`) para no acoplarse directamente a la pasarela de pagos.

### B. Domain Driven Design (DDD)
* Una `Disputa` solo puede crearse sobre un `Pedido` existente. 
* Solo puede ser resuelta si está en estado `EN_MEDIACION`. El dominio rechaza cierres prematuros.

### C. Principios SOLID
* **SRP**: Separar la gestión de tickets comunes (A-09) de un proceso legal/financiero como la disputa.

### D. Test-Driven Development (TDD)
* Mockear una disputa abierta. Intentar aplicar una `Resolucion` sin pasar por `EN_MEDIACION` debe lanzar `TransicionEstadoInvalidaException`.
* Verificar que la resolución a favor del comprador dispara correctamente el evento compensatorio.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Archivo `disputas.json`. El interactor actualiza el estado y el ganador.
* **Entrega 2**: Tabla de auditoría en BD. 
* **API Web**: PATCH `/api/v1/admin/soporte/disputas/{id}/resolver`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `Disputa` y las validaciones de estado.
- [ ] 2. Definir eventos de dominio de compensación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `GestionarSoporteInteractor`.
- [ ] 4. (TDD) Escribir las pruebas de resolución y transiciones.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Proveer persistencia en JSON Gateway.
- [ ] 6. Controlador REST protegido para administradores.
