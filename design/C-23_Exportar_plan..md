# Diseño Técnico e Implementación: C-23 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-23**: 
> *"Como comprador, quiero exportar mis datos personales para cumplir con mis derechos de privacidad (GDPR/Habeas Data)"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `DataExportadaUsuario`.
* **Value Objects**: `IdUsuario`, `FormatoExportacion` (JSON, CSV).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IExportarMiCuentaUseCase`.
* **Output Ports (Gateways)**: `IUsuarioGateway`, `IPedidoGateway`, `IListaDeseosGateway`, `IReseñaGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso actúa como un "Orquestador de Lectura". Recopila información de múltiples Gateways a través de sus interfaces sin inyectar lógica de base de datos directa.

### B. Domain Driven Design (DDD)
* La `DataExportadaUsuario` es un objeto de dominio de solo lectura y transitorio que estructura toda la información legalmente exigible de un usuario.

### C. Principios SOLID
* **Facade**: El Interactor es una fachada que compila información de compras, perfil y reseñas en un único paquete DTO estandarizado.

### D. Test-Driven Development (TDD)
* Mockear todos los Gateways. Validar que si el usuario no tiene historial de compras, el DTO de exportación no falle y retorne arreglos vacíos de forma controlada.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: El interactor lee de `usuarios.json`, `pedidos.json` y `resenas.json`. Retorna un objeto complejo que el controlador de Spring convierte automáticamente a JSON.
* **Entrega 2**: El controlador puede generar un archivo `.zip` al vuelo utilizando `ZipOutputStream` para entregar toda la información de forma empaquetada.
* **API Web**: GET `/api/v1/usuarios/{id}/exportar-datos`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el DTO/Agregado transversal `DataExportadaUsuario`.
- [ ] 2. Consolidar definiciones de puertos de lectura.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `ExportarMiCuentaInteractor`.
- [ ] 4. (TDD) Escribir pruebas de ensamblaje de datos comprobando que todas las dependencias son llamadas.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Adaptar los Gateways existentes para soportar búsquedas por `IdUsuario` de manera eficiente.
- [ ] 6. Implementar el Controlador REST y configurar la respuesta para que se descargue como archivo (Header `Content-Disposition`).
