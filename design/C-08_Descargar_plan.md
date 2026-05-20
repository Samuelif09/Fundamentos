# Diseño Técnico e Implementación: C-08 - Post-Compra

Este documento define el plan de implementación detallado para la Historia de Usuario **C-08**: 
> *"Como comprador, quiero descargar el libro comprado para poder leerlo inmediatamente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `BibliotecaUsuario`.
* **Value Objects**: `ArchivoDigital` (contiene URL firmada y MIME type), `LicenciaAcceso`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IDescargarPostCompraUseCase`.
* **Output Ports (Gateways)**: `IBibliotecaGateway` (para validar que el usuario es dueño del libro), `IAlmacenamientoGateway` (para recuperar el archivo).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La seguridad de los archivos (Signed URLs) se gestiona en la infraestructura, el dominio solo recibe un `String` con la ruta temporal.

### B. Domain Driven Design (DDD)
* Si el comprador no ha pagado por el libro, el dominio lanza `AccesoDenegadoException`.

### C. Principios SOLID
* **SRP**: Separar la validación de propiedad (Biblioteca) de la gestión física del archivo (Almacenamiento).

### D. Test-Driven Development (TDD)
* Mockear la Biblioteca para retornar "No posee licencia" y asegurar que la descarga se aborta lanzando excepción.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `AlmacenamientoLocalGateway` que lea el archivo PDF de la carpeta `/recursos/libros/` del proyecto.
* **Entrega 2**: `S3AlmacenamientoGateway` que genere una URL pre-firmada válida por 5 minutos usando AWS S3 SDK.
* **API Web**: GET `/api/v1/biblioteca/{idLibro}/descargar`. Devuelve el Stream del archivo o un Redireccionamiento (302) a la Signed URL.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `LicenciaAcceso`.
- [ ] 2. Definir puertos de Biblioteca y Almacenamiento.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `DescargarPostCompraInteractor`.
- [ ] 4. (TDD) Pruebas de acceso autorizado vs denegado.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar lectura de archivos locales (`Files.readAllBytes`).
- [ ] 6. Implementar Controlador que retorne `ResponseEntity<Resource>` con las cabeceras `Content-Disposition`.
