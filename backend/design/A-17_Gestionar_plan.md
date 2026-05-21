# Diseño Técnico e Implementación: A-17 - Autenticación admin

Este documento define el plan de implementación detallado para la Historia de Usuario **A-17**: 
> *"Como admin, quiero gestionar roles de administradores para control de acceso granular al panel"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `RolAdmin`.
* **Value Objects**: `NombreRol` (ej. SUPERADMIN, MODERADOR, FINANCIERO), `ListaPermisos` (Conjunto de permisos específicos).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGestionarRolesUseCase`.
* **Output Ports (Gateways)**: `IAdminGateway`, `IRolGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La definición de los permisos y los roles se gestiona como configuración de negocio. La evaluación final de esos roles recae en la infraestructura de seguridad perimetral (Spring Security).

### B. Domain Driven Design (DDD)
* Un administrador no puede auto-quitarse el rol de `SUPERADMIN` si es el único en la plataforma, para evitar dejar el sistema acéfalo (`ReglaSuperAdminUnico`).

### C. Principios SOLID
* **SRP**: Dividir los perfiles de los empleados internos mediante Role-Based Access Control (RBAC).

### D. Test-Driven Development (TDD)
* Mockear un Gateway donde existe solo un `SUPERADMIN`. Intentar cambiarle el rol a `MODERADOR` y verificar que el sistema lanza `ValidacionJerarquiaException`.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Guardar la asignación de roles en un atributo `roles` dentro de `admins.json`.
* **Entrega 2**: Tablas de RBAC completas en BD (`roles`, `permisos`, `admin_roles`). Aplicar anotaciones `@PreAuthorize("hasAuthority('MODERAR_CONTENIDO')")` en los endpoints.
* **API Web**: PUT `/api/v1/admin/usuarios/{adminId}/roles`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Extender `Administrador` y crear `RolAdmin`.
- [ ] 2. (TDD) Pruebas de protección del Super Admin.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor para asignar y remover roles.
- [ ] 4. (TDD) Pruebas de actualización de permisos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el guardado de roles en el Gateway JSON.
- [ ] 6. Controlador REST protegido (solo accesible para SUPERADMIN).
