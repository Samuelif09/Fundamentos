# Diseño Técnico e Implementación: A-24 - Configuración sistema

Este documento define el plan de implementación detallado para la Historia de Usuario **A-24**: 
> *"Como admin, quiero API pública documentada para facilitar integraciones de terceros con la plataforma"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `CredencialApi`.
* **Value Objects**: `ApiKey` (Cadena generada criptográficamente), `IdVendedor` / `IdAdmin`, `PermisosAcceso`, `EstadoLlave` (ACTIVA, REVOCADA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGenerarCredencialesApiUseCase`.
* **Output Ports (Gateways)**: `IApiKeyGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La documentación (Swagger/OpenAPI) es una capa de presentación y no debe contaminar el dominio con anotaciones. La capa de aplicación maneja la emisión de las llaves (`ApiKeys`) para que terceros consuman el sistema.

### B. Domain Driven Design (DDD)
* Un administrador o vendedor puede tener múltiples `CredencialApi` para diferentes sistemas externos, pero deben poder ser revocadas de forma independiente.

### C. Principios SOLID
* **SRP**: Separar la autenticación de usuarios por UI (Login con JWT) de la autenticación de máquina a máquina (M2M) mediante API Keys.

### D. Test-Driven Development (TDD)
* Probar la generación de la llave asegurando que cumple con los estándares de complejidad (longitud, entropía).
* Validar que al revocar una llave, el estado cambia a `REVOCADA` y no puede reactivarse.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Generar API Keys como UUIDs y almacenarlas en `api_keys.json`. Documentar los endpoints actuales manualmente en un archivo Markdown.
* **Entrega 2**: Implementar **SpringDoc OpenAPI** (`swagger-ui`) para la autogeneración de la documentación interactiva en `/swagger-ui.html`. Implementar validación de API Keys mediante filtros de Spring Security y Rate Limiting (ej. Bucket4j + Redis) para evitar abusos.
* **API Web**: POST `/api/v1/admin/integraciones/api-keys`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `CredencialApi` y VO `ApiKey`.
- [ ] 2. (TDD) Pruebas de reglas de negocio de revocación.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el UseCase `GenerarCredencialesApiInteractor`.
- [ ] 4. (TDD) Escribir pruebas de orquestación.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Instalar y configurar dependencias de OpenAPI/Swagger.
- [ ] 6. Implementar Filtro de Seguridad para interceptar el Header `X-API-KEY`.
- [ ] 7. Crear Controlador REST para la gestión de llaves.
