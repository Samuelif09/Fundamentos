# Diseño Técnico e Implementación: C-10 - Autenticación

Este documento define el plan de implementación detallado para la Historia de Usuario **C-10**: 
> *"Como comprador, quiero recuperar mi contraseña para no perder acceso a mi cuenta"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Usuario`.
* **Value Objects**: `Email`, `TokenRecuperacion` (con fecha de expiración).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRecuperarAutenticacionUseCase`.
* **Output Ports (Gateways)**: `IUsuarioGateway`, `ITokenRecuperacionGateway`, `IEmailGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lógica para generar el código de reseteo debe estar en el dominio (ej. `TokenRecuperacion.generar()`), mientras que el envío del email es infraestructura.

### B. Domain Driven Design (DDD)
* Por razones de seguridad (evitar ataques de enumeración), si el correo no existe, el sistema debe responder exitosamente de todos modos, pero sin enviar ningún correo (o enviando un correo de alerta genérico).

### C. Principios SOLID
* **ISP**: Usar `IEmailGateway` con un método específico `enviarTokenRecuperacion(Email, TokenRecuperacion)`.

### D. Test-Driven Development (TDD)
* Probar que el Interactor orquesta correctamente la búsqueda del usuario, generación del token y el llamado al `IEmailGateway`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Generar el token (UUID), persistirlo asociado al usuario en memoria y mostrarlo por consola (Logger) para simular el email.
* **Entrega 2**: Guardar el token en una tabla de redis con TTL (Time-To-Live) de 15 minutos y enviar con `JavaMailSender`.
* **API Web**: POST `/api/v1/auth/recuperar-password` (recibe el email).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `TokenRecuperacion` (validar que expire en 15-30 minutos).
- [ ] 2. Definir puertos (`ITokenRecuperacionGateway`, `IEmailGateway`).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `RecuperarAutenticacionInteractor`.
- [ ] 4. (TDD) Test simulando envío exitoso.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el Dummy Mail Sender (consola).
- [ ] 6. Implementar persistencia del token de recuperación en memoria.
- [ ] 7. Crear el endpoint de recuperación.
