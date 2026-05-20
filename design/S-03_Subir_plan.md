# Diseño Técnico e Implementación: S-03 - Publicar libro

Este documento define el plan de implementación detallado para la Historia de Usuario **S-03**: 
> *"Como vendedor, quiero subir portada e imagen del libro para generar más atracción visual"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Libro`.
* **Value Objects**: `UrlPortada`, `ArchivoImagen` (que contiene los bytes, el MIME Type y el tamaño).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `ISubirImagenPublicarLibroUseCase`.
* **Output Ports (Gateways)**: `ILibroGateway` (para actualizar la URL), `IAlmacenamientoGateway` (para guardar el archivo físico).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El archivo multipart (Spring `MultipartFile`) no debe cruzar al dominio. La capa web lo convierte a un `ArchivoImagen` (byte[], content-type) antes de invocar el Interactor.

### B. Domain Driven Design (DDD)
* El dominio debe validar que el MIME type corresponda a una imagen (ej. `image/png`, `image/jpeg`) y que el tamaño no supere un límite (ej. 2MB). Lanzar `ArchivoInvalidoException` en caso contrario.

### C. Principios SOLID
* **SRP**: Subir la imagen es un caso de uso independiente a crear los metadatos (S-02), mejorando el rendimiento y manejo de errores.

### D. Test-Driven Development (TDD)
* Mockear un archivo de texto (`text/plain`) y verificar que el Interactor lo rechaza antes de llamar al Gateway de almacenamiento.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `AlmacenamientoLocalGateway` guarda la imagen en la carpeta `./uploads/portadas/` y retorna la ruta local.
* **Entrega 2**: `S3AlmacenamientoGateway` sube a un bucket público y retorna la URL absoluta.
* **API Web**: POST `/api/v1/libros/{id}/portada` (consume `multipart/form-data`).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `ArchivoImagen` con validaciones de peso y extensión.
- [ ] 2. Definir método de guardado en `IAlmacenamientoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor que orquesta validación, subida a disco y actualización del libro.
- [ ] 4. (TDD) Pruebas con archivos válidos e inválidos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el almacenamiento en sistema de archivos local (`java.nio.file.Files`).
- [ ] 6. Controlador REST con inyección del token JWT para validar propiedad del libro.
