# Diseño Técnico e Implementación: S-17 - Mi tienda

Este documento define el plan de implementación detallado para la Historia de Usuario **S-17**: 
> *"Como vendedor, quiero personalizar el banner de mi tienda para fortalecer mi marca e identidad visual"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `PerfilTienda`.
* **Value Objects**: `ArchivoImagen` (valida dimensiones anchas para banner), `UrlBanner`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IPersonalizarMiTiendaUseCase`.
* **Output Ports (Gateways)**: `IVendedorGateway`, `IAlmacenamientoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El archivo físico no toca el dominio. Se maneja mediante la infraestructura y el dominio solo guarda la referencia a la URL.

### B. Domain Driven Design (DDD)
* Al igual que las portadas de libros, `ArchivoImagen` debe asegurar que no se suban archivos pesados que arruinen la métrica de carga (< 1.5s). Lanzar error si supera los 3MB.

### C. Principios SOLID
* **LSP**: El puerto de almacenamiento puede recibir banners o portadas de forma polimórfica sin importar el contexto que lo llama.

### D. Test-Driven Development (TDD)
* Probar el caso de uso simulando una subida exitosa y verificando que el Gateway de persistencia recibe la URL del almacenamiento para guardarla en el perfil del vendedor.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Guardar localmente en `./uploads/banners/` y actualizar la propiedad `urlBanner` en `vendedores.json`.
* **Entrega 2**: Subida a AWS S3 y almacenamiento de URL en base de datos relacional.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/tienda/banner` (consume `multipart/form-data`).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender `PerfilTienda` con `UrlBanner`.
- [ ] 2. Ajustar `ArchivoImagen` si las restricciones cambian para banners.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor orquestando Storage y Perfil.
- [ ] 4. (TDD) Tests de subida y rechazo de archivos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Reutilizar `AlmacenamientoLocalGateway` para guardar el banner.
- [ ] 6. Controlador REST protegido para el dueño de la tienda.
