# Diseño Técnico e Implementación: C-19 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-19**: 
> *"Como comprador, quiero ver mi historial de navegación para encontrar libros que vi pero no compré"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `HistorialNavegacion`.
* **Value Objects**: `IdUsuario`, `ItemNavegacion` (contiene `IdLibro` y `FechaVista`), `LimiteHistorial` (ej. máximo 50 libros recientes).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerHistorialNavegacionUseCase`.
* **Output Port (Gateway)**: `IHistorialNavegacionGateway`, `ILibroGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El registro de la vista del libro debe ser un evento asíncrono disparado cuando se ejecuta el caso de uso `VerDetalleLibroUseCase`, para no penalizar el tiempo de respuesta de la consulta original.

### B. Domain Driven Design (DDD)
* La entidad `HistorialNavegacion` debe garantizar que no haya libros duplicados. Si un usuario vuelve a ver un libro, se actualiza su `FechaVista` y se mueve al inicio de la lista.

### C. Principios SOLID
* **SRP**: Separar el caso de uso de "Registrar Vista" del caso de uso de "Ver Historial". Aquí implementaremos la lectura (`IVerHistorialNavegacionUseCase`).

### D. Test-Driven Development (TDD)
* Testear la lógica de la entidad `HistorialNavegacion` asegurando que al superar el `LimiteHistorial` (ej. 51 libros), el libro más antiguo se elimina automáticamente (política FIFO).

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `historial_navegacion.json`, indexado por `IdUsuario`.
* **Entrega 2**: Migrar a **Redis** o MongoDB, ya que los historiales de navegación tienen muchas escrituras y no requieren transaccionalidad estricta ni integridad referencial fuerte en PostgreSQL.
* **API Web**: GET `/api/v1/usuarios/{id}/historial-navegacion`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Entidad `HistorialNavegacion` con la regla de ordenamiento y límite.
- [ ] 2. Definir interfaz `IHistorialNavegacionGateway`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO de salida con el resumen de los libros vistos.
- [ ] 4. (TDD) Pruebas de orquestación en el `VerHistorialNavegacionInteractor`.
- [ ] 5. Implementar el Interactor inyectando el Gateway de libros para recuperar metadatos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar lectura en el JSON Gateway.
- [ ] 7. Crear el controlador REST asociado al perfil del usuario.
