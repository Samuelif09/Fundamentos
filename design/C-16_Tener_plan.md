# Diseño Técnico e Implementación: C-16 - Checkout

Este documento define el plan de implementación detallado para la Historia de Usuario **C-16**: 
> *"Como comprador, quiero múltiples métodos de pago para elegir el que más me convenga"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `TransaccionPago` / `Pedido`.
* **Value Objects**: `TipoMetodoPago` (Enum: TARJETA, PAYPAL, CRYPTO, TRANSFERENCIA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IProcesarCheckoutUseCase`.
* **Output Port (Gateway)**: Factory `IPasarelaPagoFactory`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El dominio no se acopla a las SDKs de Stripe o PayPal. Solo expone la interfaz abstracta que la infraestructura debe resolver.

### B. Domain Driven Design (DDD)
* La entidad `Pedido` almacena el `TipoMetodoPago` seleccionado para histórico y conciliación futura.

### C. Principios SOLID
* **OCP y Factory Method**: El sistema usa una fábrica abstracta en el dominio `IPasarelaPagoFactory.obtenerPasarela(TipoMetodoPago)` que devuelve la instancia correcta de `IPasarelaPagoGateway` en tiempo de ejecución.

### D. Test-Driven Development (TDD)
* Validar que la fábrica de pasarelas selecciona y rutea correctamente el pago dependiendo del enum recibido.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Implementar `PasarelaDummyGateway` genérica en infraestructura que imprima un log distinto según el método elegido.
* **Entrega 2**: Implementar múltiples adaptadores (StripeAdapter, PayPalAdapter) y registrarlos en la Factory gestionada por Spring (`@Component`).
* **API Web**: Extender el endpoint de checkout existente para incluir el campo `tipoMetodoPago`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Enum `TipoMetodoPago`.
- [ ] 2. Definir la `IPasarelaPagoFactory`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Actualizar el DTO de Checkout Request.
- [ ] 4. (TDD) Refactorizar tests del checkout integrando la Factory.
- [ ] 5. Actualizar el Interactor de pagos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar la Factory en Infraestructura resolviendo dependencias de Spring.
- [ ] 7. Crear los adaptadores Dummy correspondientes.
