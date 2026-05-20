# Diseño Técnico e Implementación: C-09 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-09**: 
> *"Como comprador, quiero ver historial de todas mis compras para consultar pedidos anteriores"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `HistorialCompras` o `Pedido`.
* **Value Objects**: `IdUsuario`, `Paginacion`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerMiCuentaUseCase` (o `IHistorialComprasUseCase`).
* **Output Port (Gateway)**: `IPedidoGateway` (extender con `listarPorUsuarioId(IdUsuario, Paginacion)`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso solo coordina la petición usando el `IdUsuario` obtenido del token JWT. No accede a la sesión HTTP directamente.

### B. Domain Driven Design (DDD)
* Retornar una lista inmutable de resúmenes de pedidos (solo datos esenciales: fecha, monto, estado, número de items) en lugar de cargar todos los detalles pesados de cada compra.

### C. Principios SOLID
* **SRP**: El Interactor solo devuelve compras de un usuario específico. Delega la validación del usuario al Gateway.

### D. Test-Driven Development (TDD)
* Probar que si el usuario no tiene compras previas, el Gateway devuelve una lista vacía y el Interactor mapea esto correctamente a un DTO sin fallar.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: En `PedidoJsonGateway`, leer `pedidos.json`, iterar sobre todos los pedidos y filtrar los que coincidan con `IdUsuario`.
* **Entrega 2**: Implementar consulta JPA indexada por `usuario_id` ordenada descendentemente por fecha (`ORDER BY fecha DESC`).
* **API Web**: GET `/api/v1/usuarios/{id}/pedidos`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Asegurar que `IdUsuario` esté validado en el dominio.
- [ ] 2. Definir método de búsqueda en `IPedidoGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO `HistorialPedidoResponse`.
- [ ] 4. (TDD) Pruebas para `VerMiCuentaInteractor` (caso con pedidos y sin pedidos).
- [ ] 5. Implementar el Interactor.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar el filtro en `PedidoJsonGateway`.
- [ ] 7. Crear Controlador REST validando que el ID del token coincida con la ruta.
