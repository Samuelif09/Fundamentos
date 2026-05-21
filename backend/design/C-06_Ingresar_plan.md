# Diseño Técnico e Implementación: C-06 - Checkout

Este documento define el plan de implementación detallado para la Historia de Usuario **C-06**: 
> *"Como comprador, quiero ingresar datos de pago de forma segura para completar mi compra"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `TransaccionPago`.
* **Value Objects**: `DatosTarjeta` (Solo mantiene token enmascarado y últimos 4 dígitos en memoria), `Monto`, `Moneda`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IIngresarCheckoutUseCase`.
* **Output Port (Gateway)**: `IPasarelaPagoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El dominio **jamás** guarda el PAN completo o CVV. Estos datos entran desde la web, viajan directamente a la pasarela (ej. Stripe) a través de un proxy, y el dominio solo maneja el `TokenPago` retornado.

### B. Domain Driven Design (DDD)
* La entidad `TransaccionPago` nace en estado `PENDIENTE_AUTORIZACION`.

### C. Principios SOLID
* **ISP**: `IPasarelaPagoGateway` solo debe tener el método `procesarCobro(Token, Monto)`, no requerir métodos de facturación ajenos a la transacción.

### D. Test-Driven Development (TDD)
* Mockear la Pasarela de Pagos para que devuelva `RECHAZADO` y verificar que se lanza la excepción `PagoRechazadoException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `PasarelaPagoDummyGateway` siempre aprueba la transacción si la tarjeta termina en número par. No persiste datos bancarios en JSON.
* **Entrega 2**: Implementar cliente HTTP para conectarse al Sandbox de una pasarela real y almacenar el recibo en PostgreSQL.
* **API Web**: POST `/api/v1/checkout/pagar`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `TokenPago` y `Monto`.
- [ ] 2. Definir interfaz `IPasarelaPagoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO seguro (sin CVV expuesto en logs).
- [ ] 4. (TDD) Pruebas de integración simulada con pasarela.
- [ ] 5. Implementar `IngresarCheckoutInteractor`.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el adaptador Dummy para la pasarela de pagos.
- [ ] 7. Implementar el Controlador REST.
