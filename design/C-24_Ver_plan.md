# Diseño Técnico e Implementación: C-24 - Mi cuenta

Este documento define el plan de implementación detallado para la Historia de Usuario **C-24**: 
> *"Como comprador, quiero ver estadísticas de mis lecturas para seguir mi progreso lector"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `EstadisticaLector` (o `MetricasUsuario`).
* **Value Objects**: `IdUsuario`, `TotalLibrosComprados`, `TotalResenasEscritas`, `CategoriaFavorita`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerEstadisticasMiCuentaUseCase`.
* **Output Ports (Gateways)**: `IPedidoGateway` (para conteo), `IReseñaGateway` (para conteo).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El cálculo de estadísticas pertenece al caso de uso, que orquesta la recolección de datos de distintos Gateways y los empaqueta en el objeto de dominio `EstadisticaLector` antes de enviarlos a la capa web.

### B. Domain Driven Design (DDD)
* `EstadisticaLector` es un agregado de lectura (proyección) que no requiere estado persistente propio, se calcula al vuelo o se recupera de una tabla de resumen.

### C. Principios SOLID
* **OCP**: La interfaz de estadísticas puede extenderse en el futuro (ej. "Páginas leídas", "Tiempo en la app") sin modificar el caso de uso base.

### D. Test-Driven Development (TDD)
* Mockear respuestas vacías de compras y reseñas y asegurar que las estadísticas matemáticas (como promedios o modas) no lancen errores de división por cero y retornen 0 u objetos vacíos.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: El interactor lee todos los pedidos en `pedidos.json` filtrados por usuario, cuenta los items y extrae la categoría más frecuente usando Streams de Java.
* **Entrega 2**: Implementar una vista materializada o una consulta de agregación (GROUP BY, COUNT) en PostgreSQL delegada en el `UsuarioMetricasJpaGateway` para no sobrecargar la memoria.
* **API Web**: GET `/api/v1/usuarios/{id}/estadisticas`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el objeto `EstadisticaLector`.
- [ ] 2. Definir métodos de agrupación en los puertos existentes (`countByUsuarioId`).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el `VerEstadisticasMiCuentaInteractor`.
- [ ] 4. (TDD) Pruebas de la lógica de agrupamiento y extracción de categoría favorita.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar los métodos de conteo en los JSON Gateways.
- [ ] 6. Controlador REST para servir las estadísticas.
