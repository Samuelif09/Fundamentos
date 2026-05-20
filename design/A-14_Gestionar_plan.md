# Diseño Técnico e Implementación: A-14 - Curaduría contenido

Este documento define el plan de implementación detallado para la Historia de Usuario **A-14**: 
> *"Como admin, quiero gestionar banners y promociones en la plataforma para campañas de marketing"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `BannerPromocional`.
* **Value Objects**: `UrlImagen`, `UrlDestino` (enlace al hacer clic), `PeriodoCampana` (Fecha Inicio, Fecha Fin), `EstadoCampana` (ACTIVA, INACTIVA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarBannersUseCase`.
* **Output Ports (Gateways)**: `IBannerGateway`, `IAlmacenamientoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Similar a la gestión de portadas y banners de tiendas, la imagen física no entra al dominio. El dominio gestiona la vigencia temporal del banner mediante el `PeriodoCampana`.

### B. Domain Driven Design (DDD)
* El método `estaVigente()` del `BannerPromocional` debe evaluar dinámicamente si la fecha actual está dentro del `PeriodoCampana` y si el `EstadoCampana` es ACTIVA. Si la fecha ya pasó, el banner se ignora automáticamente sin necesidad de borrarlo.

### C. Principios SOLID
* **OCP**: Permite que el Storefront (página principal) consulte los banners activos inyectando un caso de uso de lectura, separado de esta gestión administrativa de escritura.

### D. Test-Driven Development (TDD)
* Probar la regla de vigencia: Crear un banner con fecha de fin en el pasado y validar que `estaVigente()` retorna `false`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Guardar en `banners.json` y almacenar las imágenes en local (`/uploads/banners_globales/`).
* **Entrega 2**: Guardar metadatos en PostgreSQL y las imágenes en S3. Usar Redis para cachear los banners vigentes en la página de inicio.
* **API Web**: POST `/api/v1/admin/marketing/banners` (consume `multipart/form-data`).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `BannerPromocional` y la validación de `PeriodoCampana`.
- [ ] 2. (TDD) Pruebas de vigencia temporal.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor para crear, editar y archivar banners.
- [ ] 4. (TDD) Pruebas de integración con los Gateways.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar operaciones CRUD en el Gateway JSON.
- [ ] 6. Controlador REST protegido.
