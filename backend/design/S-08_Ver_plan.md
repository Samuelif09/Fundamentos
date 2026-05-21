# Diseño Técnico e Implementación: S-08 - Finanzas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-08**: 
> *"Como vendedor, quiero ver el desglose de comisiones por venta para entender mi ganancia neta"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `TransaccionLiquidacion`.
* **Value Objects**: `MontoBruto`, `ComisionPlataforma` (ej. 10%), `Impuestos`, `GananciaNeta`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerDesgloseFinanzasUseCase`.
* **Output Port (Gateway)**: `ILiquidacionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La regla de negocio de "qué porcentaje retiene OpenLib" pertenece al Dominio (`ReglaComisionDomainService`). Esto permite cambiar las tarifas libremente sin tocar la base de datos ni los controladores.

### B. Domain Driven Design (DDD)
* Un Value Object `DesgloseFinanciero` se asegura de que: `MontoBruto - ComisionPlataforma - Impuestos = GananciaNeta`. Si esta ecuación matemática falla, lanza un error de dominio de inconsistencia.

### C. Principios SOLID
* **SRP / Liskov**: Separar claramente los ingresos brutos (S-07) de la estructura impositiva y de comisiones (S-08).

### D. Test-Driven Development (TDD)
* Pruebas estrictas de punto flotante/BigDecimal. Ej. Bruto $10.00, Comisión 15%. Validar que la Ganancia Neta sea exactamente $8.50 sin errores de redondeo de Double.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: El interactor aplica un cálculo de comisión estático (ej. constante del 15%) sobre las transacciones leídas de `pedidos.json`.
* **Entrega 2**: Consumir tabla `configuraciones_financieras` para usar tasas dinámicas, y usar `BigDecimal` en la capa JPA.
* **API Web**: GET `/api/v1/vendedores/{sellerId}/finanzas/transacciones?page=X`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Implementar servicio de dominio para el cálculo de retenciones.
- [ ] 2. Crear Value Object `DesgloseFinanciero` con verificación contable.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTOs con los cálculos separados.
- [ ] 4. (TDD) Pruebas de cálculos financieros usando `BigDecimal`.
- [ ] 5. Implementar el Interactor de desglose.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Proveer lista transaccional desde el Gateway JSON.
- [ ] 7. Crear el Controlador REST y paginación.
