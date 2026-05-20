# Diseño Técnico e Implementación: C-01 - Autenticación

Este documento define el plan de implementación detallado para la Historia de Usuario **C-01**: 
> *"Como comprador, quiero iniciar sesión con email y contraseña para acceder a mi cuenta de comprador"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Usuario`.
* **Value Objects**: `Email`, `PasswordPlano`, `TokenAcceso`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IIniciarAutenticacionUseCase`.
* **Output Ports (Gateways)**: `IUsuarioGateway` (para buscar por email), `IPasswordEncoderGateway` (para verificar hashes), `ITokenGeneratorGateway` (para emitir JWT).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La generación de tokens JWT es un detalle de infraestructura. El dominio solo interactúa con la interfaz `ITokenGeneratorGateway` que devuelve un `String` o VO.

### B. Domain Driven Design (DDD)
* Si las credenciales fallan, se debe lanzar genéricamente `CredencialesInvalidasException` para no revelar si el error fue el email o la clave (seguridad).

### C. Principios SOLID
* **DIP**: El caso de uso no utiliza clases de Spring Security, utiliza abstracciones puras de Java para comparar y generar.

### D. Test-Driven Development (TDD)
* Pruebas para: (1) Email inexistente, (2) Contraseña incorrecta, (3) Éxito devolviendo el Token.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Lectura del archivo `usuarios.json` en `UsuarioJsonGateway`. Dummy Token Generator (devuelve un string en Base64 estático o UUID).
* **Entrega 2**: Integración profunda con Spring Security, filtros de sesión (Stateless JWT) y persistencia en PostgreSQL.
* **API Web**: POST `/api/v1/auth/login`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Validar `Email` y crear excepciones de dominio (`CredencialesInvalidasException`).
- [ ] 2. Definir puertos (`IUsuarioGateway`, `IPasswordEncoderGateway`, `ITokenGeneratorGateway`).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Login Request y Login Response.
- [ ] 4. (TDD) Tests para `IniciarAutenticacionInteractor`.
- [ ] 5. Implementar lógica de orquestación en el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar `TokenGeneratorAdapter` (UUID temporal).
- [ ] 7. Implementar endpoint POST.
- [ ] 8. Configurar controlador de errores para retornar 401 Unauthorized.
