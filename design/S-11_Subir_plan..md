# Diseño Técnico e Implementación: S-11 - Publicar libro

Este documento define el plan de implementación detallado para la Historia de Usuario **S-11**: 
> *"Como vendedor, quiero subir vista previa del libro para aumentar la tasa de conversión de ventas"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `ArchivoVistaPrevia` (validando extensión PDF o EPUB), `UrlVistaPrevia`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ISubirVistaPreviaPublicarLibroUseCase`.
* **Output Ports (Gateways)**: `ILibroGateway`, `IAlmacenamientoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Similar a la historia S-03 (Portada), el archivo entra como byte[] al dominio, pero aquí las validaciones de negocio son distintas (solo documentos, límite de peso mayor).

### B. Domain Driven Design (DDD)
* `ArchivoVistaPrevia` debe rechazar ejecutables o imágenes, limitándose estrictamente a formatos de lectura y a un máximo estipulado (ej. 5 MB). Lanza `FormatoNoPermitidoException`.

### C. Principios SOLID
* **OCP**: Reutilizar el `IAlmacenamientoGateway` creado para portadas, demostrando que la infraestructura sirve a varios casos de uso de dominio.

### D. Test-Driven Development (TDD)
* Probar el VO `ArchivoVistaPrevia` con un MIME type inválido (ej. `application/x-msdownload`) para asegurar su rechazo antes de procesarlo.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en la carpeta `./uploads/previews/` y guardar la ruta en `libros.json`.
* **Entrega 2**: Subir el archivo al bucket S3 correspondiente y actualizar la columna `url_vista_previa` en la base de datos.
* **API Web**: POST `/api/v1/libros/{id}/vista-previa` (consume `multipart/form-data`).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `ArchivoVistaPrevia` con reglas específicas para documentos.
- [ ] 2. Añadir `urlVistaPrevia` a la entidad `Libro`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el interactor orquestando validación, subida a Storage y actualización de la BD.
- [ ] 4. (TDD) Tests asegurando que un usuario no puede subir el archivo a un libro que no le pertenece.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Reutilizar o extender `AlmacenamientoLocalGateway` para previews.
- [ ] 6. Controlador REST con inyección de archivos multipartes.
