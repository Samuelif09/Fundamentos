# Diseño Técnico e Implementación: S-18 - Registro vendedor

Este documento define el plan de implementación detallado para la Historia de Usuario **S-18**: 
> *"Como vendedor, quiero verificar mi identidad para obtener sello de vendedor confiable en la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `Vendedor` y `VerificacionIdentidad`.
* **Value Objects**: `IdVendedor`, `DocumentoIdentidad` (archivo escaneado), `EstadoVerificacion` (NO_INICIADO, EN_REVISION, APROBADO, RECHAZADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerificarRegistroVendedorUseCase`.
* **Output Ports (Gateways)**: `IVendedorGateway`, `IAlmacenamientoGateway`, `INotificacionAdminGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* Este caso de uso solo *inicia* el proceso. Sube los documentos, cambia el estado a `EN_REVISION` y notifica al administrador. La aprobación real ocurrirá en otro caso de uso de la Épica de Curaduría (A-21).

### B. Domain Driven Design (DDD)
* Un vendedor no puede enviar una nueva solicitud si ya tiene una en estado `EN_REVISION`. Lanza `VerificacionEnCursoException`.

### C. Principios SOLID
* **SRP**: Separado del caso de uso de registro inicial (S-01), ya que la verificación ocurre a posteriori y no bloquea las ventas de inmediato (o sí, dependiendo de la política de negocio configurada en el dominio).

### D. Test-Driven Development (TDD)
* Pruebas comprobando las transacciones de estado. Si el vendedor ya está `APROBADO`, y vuelve a intentar verificar, el sistema debe arrojar `VendedorYaVerificadoException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Actualizar `estadoVerificacion` en `vendedores.json` y guardar la ruta local del archivo de identidad subido.
* **Entrega 2**: Integrar almacenamiento seguro de documentos (cifrados) y enviar evento a la cola del Dashboard del Administrador.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/verificacion` (consume `multipart/form-data`).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Añadir el VO `EstadoVerificacion` a la entidad `Vendedor`.
- [ ] 2. (TDD) Escribir las reglas de negocio de los estados de verificación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `VerificarRegistroVendedorInteractor`.
- [ ] 4. (TDD) Pruebas orquestando el guardado y el cambio de estado.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar almacenamiento de documentos privados.
- [ ] 6. Controlador REST con inyección del archivo.
