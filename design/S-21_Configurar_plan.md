# Diseño Técnico e Implementación: S-21 - Mi tienda

Este documento define el plan de implementación detallado para la Historia de Usuario **S-21**: 
> *"Como vendedor, quiero sistema de afiliados para ampliar mis ventas mediante terceros que me recomienden"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ProgramaAfiliado` y `EnlaceAfiliado`.
* **Value Objects**: `IdVendedor`, `PorcentajeComisionAfiliado` (0-100), `CodigoRastreo` (UTM/Referral).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IConfigurarAfiliadosUseCase`.
* **Output Port (Gateway)**: `IAfiliadoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El sistema de afiliados cruza al módulo de ventas: cuando se complete un pedido (Historia C-07), un listener verificará si la venta contenía un `CodigoRastreo` para asentar la comisión.

### B. Domain Driven Design (DDD)
* Validar en el dominio que la suma de la comisión de la plataforma (OpenLib) + la comisión del afiliado no exceda el 100% del valor del libro. Lanza `ComisionInvalidaException`.

### C. Principios SOLID
* **OCP**: La lógica de cálculo financiero (S-08) se amplía sin modificarse. Si hay afiliado, el monto retenido se divide en dos destinatarios (Plataforma y Afiliado).

### D. Test-Driven Development (TDD)
* Probar que si se define una comisión de afiliado del 60% y la plataforma cobra el 50%, la entidad lo rechaza inmediatamente al instanciarse.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `afiliados.json`. Generar códigos de rastreo UUID y retornarlos.
* **Entrega 2**: Tabla de relación y trazabilidad en BD para asociar compras exactas a un `CodigoRastreo`.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/afiliados`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `ProgramaAfiliado` y validaciones matemáticas conjuntas.
- [ ] 2. Definir interfaz de salida `IAfiliadoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar Interactor para dar de alta a un afiliado y generar el link.
- [ ] 4. (TDD) Pruebas de reglas de negocio sobre los porcentajes.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el registro en el `JsonGateway`.
- [ ] 6. Actualizar Controlador REST.
