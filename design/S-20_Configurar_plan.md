# Diseño Técnico e Implementación: S-20 - Gestión inventario

Este documento define el plan de implementación detallado para la Historia de Usuario **S-20**: 
> *"Como vendedor, quiero herramientas de pricing dinámico para competir mejor frente a vendedores similares"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReglaPricing`.
* **Value Objects**: `IdLibro`, `PrecioMinimo`, `PrecioMaximo`, `EstrategiaCompetencia` (Enum: IGUALAR_MAS_BAJO, POR_DEBAJO_DEL_PROMEDIO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IConfigurarPricingDinamicoUseCase`.
* **Output Ports (Gateways)**: `IReglaPricingGateway`, `ICatalogoGateway` (para consultar precios de la competencia).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El algoritmo de cálculo de precios es una regla pura de negocio y pertenece al Dominio (`PricingDomainService`). La infraestructura solo se encarga de ejecutar el Job periódico que llama a este servicio.

### B. Domain Driven Design (DDD)
* `ReglaPricing` debe validar que el `PrecioMinimo` nunca sea mayor al `PrecioMaximo` para evitar que el algoritmo establezca precios irreales o genere pérdidas.

### C. Principios SOLID
* **Strategy**: Cada valor del Enum `EstrategiaCompetencia` puede implementarse como una clase separada (`IgualarPrecioStrategy`, `PromedioStrategy`) que el motor de pricing ejecuta polimórficamente.

### D. Test-Driven Development (TDD)
* Mockear el catálogo de la competencia retornando precios: $10, $12 y $15. Configurar `PrecioMinimo=$9`. Si la estrategia es "Igualar al más bajo", el test debe asegurar que el nuevo precio asignado sea $10.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Archivo `reglas_pricing.json`. Un Interactor simula la ejecución manual para verificar el cálculo.
* **Entrega 2**: Implementar una tarea programada con `@Scheduled` en Spring Boot (Capa de Infraestructura) que evalúe y actualice los precios en la BD PostgreSQL automáticamente cada 24 horas.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/libros/{bookId}/pricing-dinamico`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `ReglaPricing` y sus límites de seguridad (`PrecioMinimo`/`Maximo`).
- [ ] 2. (TDD) Implementar el patrón Strategy para los algoritmos de competencia.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el UseCase para configurar la regla.
- [ ] 4. Crear un UseCase interno (`AEjecutarPricingAutomaticoUseCase`) para ser llamado por el CronJob.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el persistidor en `JsonGateway`.
- [ ] 6. Controlador REST para que el vendedor defina su estrategia.
