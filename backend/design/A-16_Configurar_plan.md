# Diseño Técnico e Implementación: A-16 - Configuración sistema

Este documento define el plan de implementación detallado para la Historia de Usuario **A-16**: 
> *"Como admin, quiero configurar reglas de comisiones por categoría para optimizar ingresos de la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReglaComision`.
* **Value Objects**: `IdCategoria` (o "GLOBAL" por defecto), `PorcentajeComision` (0 a 100).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IConfigurarComisionesUseCase`.
* **Output Port (Gateway)**: `IConfiguracionComisionGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Esto evoluciona la historia S-08 (Cálculo de ganancias del vendedor). Ahora, en lugar de usar una tarifa plana estática, el dominio solicitará a `IConfiguracionComisionGateway` la tarifa específica para la categoría del libro vendido.

### B. Domain Driven Design (DDD)
* Si no existe una regla específica para la categoría de un libro, el sistema debe tener la capacidad de retroceder (fallback) a una `ReglaComision` global por defecto.

### C. Principios SOLID
* **OCP**: El servicio de cálculo `ReglaComisionDomainService` creado en S-08 no se reescribe drásticamente; simplemente se le inyecta la nueva política variable en lugar de una constante.

### D. Test-Driven Development (TDD)
* Mockear una configuración: Global = 10%, Categoría 'Ficción' = 15%. Vender un libro de Ficción por $100 -> Comisión = $15. Vender un libro de Ciencia por $100 -> Comisión = $10.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar un mapa `{ "GLOBAL": 10, "FICCION": 15 }` en `comisiones.json`.
* **Entrega 2**: Tabla de configuración en caché (Redis) para evitar consultar a PostgreSQL en cada transacción de checkout.
* **API Web**: POST `/api/v1/admin/configuracion/comisiones`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `ReglaComision` y actualizar el `ReglaComisionDomainService`.
- [ ] 2. (TDD) Pruebas sobre la jerarquía de aplicación (Específica vs Global).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor de configuración.
- [ ] 4. (TDD) Pruebas de guardado de la nueva configuración.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar operaciones CRUD en el JSON de configuraciones.
- [ ] 6. Controlador REST protegido.
