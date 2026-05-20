# Diseño Técnico e Implementación: A-01 - Autenticación admin

Este documento define el plan de implementación detallado para la Historia de Usuario **A-01**: 
> *"Como admin, quiero iniciar sesión con credenciales seguras para acceder al panel de administración"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Administrador` (hereda lógicamente de Usuario).
* **Value Objects**: `CredencialesAdmin` (Email y Password), `Rol` (ROLE_ADMIN).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IIniciarAutenticacionAdminUseCase`.
* **Output Ports (Gateways)**: `IAdminGateway`, `IPasswordEncoderGateway`, `ITokenGeneratorGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Aunque comparte similitudes con C-01 (Login de comprador), la separación por roles permite que las políticas de seguridad del administrador (ej. MFA, expiración de tokens más corta) evolucionen de forma independiente.

### B. Domain Driven Design (DDD)
* Si un usuario con credenciales correctas intenta iniciar sesión aquí pero su rol es `ROLE_BUYER`, el dominio lanza `AccesoDenegadoException` para prevenir escalada de privilegios.

### C. Principios SOLID
* **SRP**: Caso de uso dedicado a emitir tokens estrictamente para la administración del Storeback.

### D. Test-Driven Development (TDD)
* Validar que credenciales válidas de un comprador devuelven `AccesoDenegadoException` y no un token válido en este Interactor.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer de un archivo especial `admins.json` o filtrar la lista global de usuarios por `rol == 'ADMIN'`.
* **Entrega 2**: Configuración robusta en `SecurityFilterChain` de Spring Security exigiendo perfiles administrativos. Emisión de un JWT con claims de alto privilegio.
* **API Web**: POST `/api/v1/auth/admin/login`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Asegurar la Entidad `Administrador` y la restricción de Roles.
- [ ] 2. Definir puertos específicos para evitar acoplamiento.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `IniciarAutenticacionAdminInteractor`.
- [ ] 4. (TDD) Tests de aislamiento de roles (Comprador intentando entrar como Admin).

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el Gateway que lee `admins.json`.
- [ ] 6. Controlador REST de entrada y generación de token.
