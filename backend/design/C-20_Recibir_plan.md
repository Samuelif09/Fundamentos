# Diseño Técnico e Implementación: C-20 - Notificaciones

Este documento define el plan de implementación detallado para la Historia de Usuario **C-20**: 
> *"Como comprador, quiero recibir notificaciones de rebajas en libros de mi lista de deseos para aprovechar descuentos"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `AlertaPrecio` / `Notificacion`.
* **Value Objects**: `IdUsuario`, `IdLibro`, `PrecioObjetivo` (opcional), `EstadoNotificacion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRecibirNotificacionPrecioUseCase`.
* **Output Ports (Gateways)**: `INotificacionGateway`, `IListaDeseosGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Este es un caso de uso *reactivo*. Se debe invocar a través de un Evento de Dominio (`LibroCambioPrecioEvent`) emitido por el módulo del vendedor, manteniendo los módulos desacoplados.

### B. Domain Driven Design (DDD)
* El dominio evalúa: Si el nuevo precio es menor que el precio anterior, y el libro está en la lista de deseos de los usuarios, se genera una `Notificacion`.

### C. Principios SOLID
* **Observer**: El Interactor implementa un listener para los cambios de precio y notifica solo a los usuarios interesados.

### D. Test-Driven Development (TDD)
* Mockear la Lista de Deseos. Emitir un evento de bajada de precio y verificar que el `INotificacionGateway` se llama exactamente por cada usuario que tenía el libro en su lista.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Implementar `NotificacionLoggerGateway` que imprime por consola "Alerta de Precio enviada al usuario X".
* **Entrega 2**: Implementar `NotificacionEmailGateway` (JavaMailSender) o notificaciones in-app (WebSockets / Server-Sent Events).
* **API Web**: Suscripción interna por eventos (Spring ApplicationEvents), no requiere un endpoint REST de entrada directa para el comprador.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Modelar los eventos de dominio para cambios de precio.
- [ ] 2. Definir puertos de notificación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Listener `RecibirNotificacionPrecioInteractor`.
- [ ] 4. (TDD) Tests comprobando la generación masiva de notificaciones según listas de deseos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Configurar el Event Publisher.
- [ ] 6. Implementar el Logger para simular el envío de notificaciones.
