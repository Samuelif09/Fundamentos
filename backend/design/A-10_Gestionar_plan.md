# Diseño Técnico e Implementación: A-10 - Configuración sistema

Este documento define el plan de implementación detallado para la Historia de Usuario **A-10**: 
> *"Como admin, quiero gestionar los métodos de pago activos en la plataforma para control financiero"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ConfiguracionMetodoPago`.
* **Value Objects**: `NombreMetodo` (ej. Stripe, PayPal), `Estado` (HABILITADO, DESHABILITADO), `CredencialesAPI` (encriptadas).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarConfiguracionSistemaUseCase`.
* **Output Ports (Gateways)**: `IMetodoPagoConfigGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Esta historia permite prender o apagar pasarelas de pago globalmente. El checkout del comprador (C-16) leerá esta configuración antes de mostrar las opciones disponibles.

### B. Domain Driven Design (DDD)
* Si el admin intenta deshabilitar el **último** método de pago activo, el dominio debe lanzar `ConfiguracionInvalidaException` para evitar que la tienda se quede sin formas de cobrar.

### C. Principios SOLID
* **ISP**: Separar el Gateway de configuración de pagos del Gateway de procesamiento de pagos transaccionales.

### D. Test-Driven Development (TDD)
* Testear la regla crítica: Intentar cambiar a `DESHABILITADO` un método cuando el Gateway reporta que es el único habilitado. Debe fallar y abortar la operación.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Modificar el estado en `metodos_pago.json`.
* **Entrega 2**: Tabla `configuracion_pagos` en PostgreSQL. Las credenciales de las APIs deben guardarse cifradas utilizando un `Cipher` de Java o AWS KMS.
* **API Web**: PATCH `/api/v1/admin/configuracion/metodos-pago/{id}/estado`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el agregado `ConfiguracionMetodoPago`.
- [ ] 2. (TDD) Pruebas de reglas de negocio (impedir apagar todos los métodos).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor para editar el estado.
- [ ] 4. Integrar validación consultando conteo de activos al Gateway.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el Gateway JSON de configuraciones.
- [ ] 6. Controlador REST asegurando rol de administrador.
