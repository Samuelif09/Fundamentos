# Diseño Técnico e Implementación: C-18 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-18**: 
> *"Como comprador, quiero gestionar mi lista de deseos para guardar libros de interés"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ListaDeseos`.
* **Value Objects**: `IdUsuario`, `IdLibro`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarMiCuentaUseCase` (Agregar/Quitar/Ver ListaDeseos).
* **Output Ports (Gateways)**: `IListaDeseosGateway`, `ILibroGateway` (para validar que el libro existe).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La lista de deseos es independiente del carrito de compras. Sus responsabilidades y ciclos de vida (persistencia prolongada) justifican un agregado separado.

### B. Domain Driven Design (DDD)
* `ListaDeseos` tiene un comportamiento tipo Set (no permite libros duplicados). Un libro se puede mover desde aquí hacia el carrito mediante otro caso de uso futuro.

### C. Principios SOLID
* **ISP**: Dividir el caso de uso si es muy complejo (ej. `IAgregarListaUseCase`, `IRemoverListaUseCase`), o unificar bajo `IGestionarMiCuentaUseCase` con comandos si es simple.

### D. Test-Driven Development (TDD)
* Probar que al agregar un libro ya existente en la lista, la entidad no crece de tamaño ni arroja un error crítico (simplemente lo ignora o retorna un mensaje informativo).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Mantener en `lista_deseos.json` indexada por ID de usuario.
* **Entrega 2**: Tabla asociativa `usuario_lista_deseos` en PostgreSQL.
* **API Web**: 
  - POST `/api/v1/usuarios/{id}/lista-deseos` (agregar)
  - DELETE `/api/v1/usuarios/{id}/lista-deseos/{idLibro}` (quitar)
  - GET `/api/v1/usuarios/{id}/lista-deseos` (ver)

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `ListaDeseos` validando duplicidad.
- [ ] 2. Definir puertos.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTOs de Request y Response.
- [ ] 4. (TDD) Pruebas unitarias para Agregar y Quitar elementos.
- [ ] 5. Implementar el Interactor/Interactors.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar operaciones CRUD en el JSON Gateway de Lista de Deseos.
- [ ] 7. Implementar Controladores REST.
