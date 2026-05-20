# Diseño Técnico e Implementación: A-13 - Gestión usuarios

Este documento define el plan de implementación detallado para la Historia de Usuario **A-13**: 
> *"Como admin, quiero enviar comunicados masivos a usuarios para notificaciones importantes de la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ComunicadoMasivo`.
* **Value Objects**: `Asunto` (no vacío), `CuerpoMensaje` (soporte HTML básico), `FiltroDestinatarios` (TODOS, SOLO_COMPRADORES, SOLO_VENDEDORES).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IEnviarComunicadoUseCase`.
* **Output Ports (Gateways)**: `INotificacionGateway`, `IUsuarioGateway` (para recuperar la lista de correos según el filtro).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso orquesta la recuperación de usuarios y la delegación del envío masivo a un servicio de infraestructura (ej. Amazon SES o SendGrid). El dominio no sabe cómo se envían los correos.

### B. Domain Driven Design (DDD)
* Un `ComunicadoMasivo` es inmutable una vez enviado. Se puede guardar un registro del envío (auditoría) con la fecha y la cantidad de destinatarios alcanzados.

### C. Principios SOLID
* **SRP**: Separar el filtrado de usuarios (responsabilidad de consulta) del proceso de despacho de correos (responsabilidad de notificación).

### D. Test-Driven Development (TDD)
* Mockear el `IUsuarioGateway` para devolver 100 usuarios (50 compradores, 50 vendedores). Filtrar por `SOLO_VENDEDORES` y probar que el `INotificacionGateway` se invoca exactamente 50 veces (o 1 vez con una lista de 50 destinatarios).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Filtrar los usuarios desde `usuarios.json` y usar el `NotificacionLoggerGateway` para imprimir por consola el envío del mensaje.
* **Entrega 2**: Implementar una cola de mensajería (RabbitMQ o Kafka) para no bloquear el hilo HTTP del administrador mientras se envían miles de correos mediante un proveedor real.
* **API Web**: POST `/api/v1/admin/comunicados`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Entidad `ComunicadoMasivo` y el VO `FiltroDestinatarios`.
- [ ] 2. Definir puertos necesarios para filtrado y envío.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor orquestando la lectura de emails y el envío.
- [ ] 4. (TDD) Pruebas de orquestación y filtros de destinatarios.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el envío simulado a consola.
- [ ] 6. Crear Controlador REST protegido para administradores.
