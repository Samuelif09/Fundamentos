# Diseño Técnico e Implementación: V-03 - Registro

Este documento define el plan de implementación detallado para la Historia de Usuario **V-03**: 
> *"Como visitante, quiero registrarme con nombre, email y contraseña para crear mi cuenta"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Usuario`.
* **Value Objects**: `Email` (valida formato de correo), `Password` (valida longitud, mayúsculas, números), `RolUsuario`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRegistrarRegistroUseCase`.
* **Output Port (Gateway)**: Interfaz `IRegistroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La encriptación de la contraseña (BCrypt) no debe ocurrir en el dominio, sino a través de un puerto secundario (ej. `IPasswordEncoderGateway`).

### B. Domain Driven Design (DDD)
* Lanzar `EmailDuplicadoException` si las reglas de negocio detectan que la cuenta ya existe.

### C. Principios SOLID
* **DIP**: El Interactor depende de `IPasswordEncoderGateway` y de `IRegistroGateway`, ignorando que debajo se usará Spring Security.

### D. Test-Driven Development (TDD)
* Test unitario asegurando que si las validaciones del `Password` (Value Object) fallan, no se invoca al Gateway de guardado.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `RegistroJsonGateway` añade un nuevo objeto al arreglo JSON en `usuarios.json`. Usar un encriptador dummy/básico si no se ha configurado BCrypt.
* **Entrega 2**: `RegistroJpaGateway` y configuración robusta de Spring Security.
* **API Web**: `RegistroController` expone POST `/api/v1/auth/registro`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Objects `Email` y `Password` con validaciones estrictas.
- [ ] 2. (TDD) Escribir pruebas unitarias para creación de `Usuario`.
- [ ] 3. Definir `IRegistroGateway` y `IPasswordEncoderGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 4. Crear DTO de Request (Nombre, Email, Pass).
- [ ] 5. Implementar `RegistrarRegistroInteractor`.
- [ ] 6. (TDD) Pruebas asegurando que el password se encripta antes de llamar a guardar.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 7. Implementar `BCryptPasswordEncoderAdapter`.
- [ ] 8. Implementar escritura en JSON (`RegistroJsonGateway`).
- [ ] 9. Implementar controlador POST retornando 201 Created.
