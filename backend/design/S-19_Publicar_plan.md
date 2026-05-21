# Diseño Técnico e Implementación: S-19 - Publicar libro

Este documento define el plan de implementación detallado para la Historia de Usuario **S-19**: 
> *"Como vendedor, quiero publicar audiolibros y cursos para diversificar mi oferta de contenido digital"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ContenidoDigital` (Abstracto / Interfaz base).
* **Entidades Específicas**: `Libro`, `Audiolibro`, `CursoVirtual`.
* **Value Objects**: `DuracionEnMinutos` (para audios/cursos), `TipoFormato`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IPublicarContenidoDigitalUseCase`.
* **Output Port (Gateway)**: `IContenidoDigitalGateway` (y Factory Method `DigitalContentFactory`).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El caso de uso no debe depender de lógicas rígidas. Recibe un DTO polimórfico, utiliza un Factory Method (patrón sugerido en el análisis de GoF) para construir el tipo correcto de contenido, y lo pasa al Gateway.

### B. Domain Driven Design (DDD)
* Un `Audiolibro` requiere el VO `DuracionEnMinutos`, mientras que un `Libro` clásico puede no requerirlo. El constructor de cada Entidad valida sus propias reglas.

### C. Principios SOLID
* **OCP y Factory Method Pattern**: Crear un `ContenidoDigitalFactory` permite añadir más adelante "Podcasts" o "Revistas" sin tocar el código de publicación base.
* **LSP**: Todos los formatos deben poder tratarse genéricamente como `ContenidoDigital` al renderizarse en el catálogo.

### D. Test-Driven Development (TDD)
* Probar la Factory enviando un tipo "AUDIOLIBRO". Validar que devuelve la instancia correcta de la entidad y exige duración.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Almacenar en `contenidos.json` usando un campo de discriminación (`"tipo": "audiolibro"`).
* **Entrega 2**: Implementar herencia en JPA usando `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` o `JOINED`.
* **API Web**: POST `/api/v1/vendedores/{sellerId}/contenidos` (payload JSON polimórfico).

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Refactorizar `Libro` hacia una jerarquía abstracta `ContenidoDigital`.
- [ ] 2. Implementar `DigitalContentFactory`.
- [ ] 3. (TDD) Probar la creación polimórfica de entidades.

### 🟡 Capa de Aplicación (Application)
- [ ] 4. Modificar el DTO para soportar diferentes formatos (`@JsonSubTypes`).
- [ ] 5. Implementar el Interactor de publicación usando la Factory.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Actualizar el JSON Gateway para soportar polimorfismo.
- [ ] 7. Crear Controlador REST unificado para contenido digital.
