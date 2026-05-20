# Diseño Técnico e Implementación: S-01 - Registro vendedor

Este documento define el plan de implementación detallado para la Historia de Usuario **S-01**: 
> *"Como vendedor, quiero registrarme como vendedor con datos del negocio para operar en la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Vendedor` (hereda lógicamente de un Usuario base o tiene una relación 1:1 con él).
* **Value Objects**: `RazonSocial`, `IdentificacionTributaria` (NIT/RUT/VAT con validación estricta), `CuentaBancaria` (para futuros desembolsos).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IRegistrarVendedorUseCase`.
* **Output Port (Gateway)**: `IVendedorGateway`, `IPasswordEncoderGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El registro del vendedor tiene reglas de negocio radicalmente distintas a las del comprador (requiere validaciones tributarias). Por ello, tiene su propio Interactor y Agregado.

### B. Domain Driven Design (DDD)
* Si el ID tributario tiene un formato inválido, lanzar `IdentificacionFiscalInvalidaException`.
* Un vendedor nace en estado `ACTIVO` o `PENDIENTE_REVISION` según lo exija el negocio.

### C. Principios SOLID
* **SRP**: `RegistrarVendedorInteractor` solo se ocupa de dar de alta el perfil comercial.

### D. Test-Driven Development (TDD)
* Pruebas del Value Object `IdentificacionTributaria` inyectando strings vacíos, con caracteres especiales no permitidos, o longitudes incorrectas.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `vendedores.json`. Usar un encriptador dummy para la clave.
* **Entrega 2**: Tabla `vendedores` o tabla unificada `usuarios` con un discriminador (Single Table Strategy) y rol `ROLE_SELLER` en Spring Security.
* **API Web**: POST `/api/v1/auth/vendedores/registro`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `Vendedor` y VOs comerciales (`IdentificacionTributaria`).
- [ ] 2. Definir interfaz `IVendedorGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de Registro Vendedor.
- [ ] 4. (TDD) Tests para la validación de negocio y guardado.
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar persistencia en JSON.
- [ ] 7. Implementar endpoint POST devolviendo código HTTP 201.
