# Diseño Técnico e Implementación: A-21 - Curaduría contenido

Este documento define el plan de implementación detallado para la Historia de Usuario **A-21**: 
> *"Como admin, quiero herramientas de detección automática de contenido inapropiado para moderación eficiente"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `RevisionAutomatica`.
* **Value Objects**: `IdLibro`, `ScoreToxicidad` (0.0 a 1.0), `Veredicto` (APROBADO, SOSPECHOSO, RECHAZADO).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IDetectarCuraduriaContenidoUseCase`.
* **Output Ports (Gateways)**: `ILibroGateway`, `IInteligenciaArtificialGateway` (Motor externo de IA).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El procesamiento de NLP (Natural Language Processing) es infraestructura externa. El dominio solo conoce `IInteligenciaArtificialGateway.analizarTexto(Sinopsis)` y recibe un `ScoreToxicidad`.

### B. Domain Driven Design (DDD)
* Si el `ScoreToxicidad` > 0.8, el `Veredicto` es automáticamente `RECHAZADO`. Si está entre 0.4 y 0.8, es `SOSPECHOSO` (requiere revisión humana A-05). Si es < 0.4, se auto-aprueba.

### C. Principios SOLID
* **Decorator / Chain of Responsibility**: Esta validación se puede enlazar directamente al caso de uso de "Publicar Libro" (S-02) como un paso de la cadena de validación sin que el vendedor se entere.

### D. Test-Driven Development (TDD)
* Mockear la IA devolviendo un 0.9. Asegurar que el dominio dicta "RECHAZADO" y el libro queda inactivo.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Un `IADummyGateway` evalúa usando una simple lista de malas palabras (Regex) sobre el título y descripción.
* **Entrega 2**: Conexión a la API de Google Perspective o OpenAI Moderation API.
* **API Web**: Consumo interno desencadenado por la publicación de un nuevo libro o reseña.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el objeto `RevisionAutomatica` y definir umbrales.
- [ ] 2. Definir interfaz de salida para motores de IA.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `DetectarCuraduriaContenidoInteractor`.
- [ ] 4. (TDD) Pruebas de reglas de ruteo y transiciones de estado basadas en el Score.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el filtro local de palabras bloqueadas en la Entrega 1.
- [ ] 6. Conectar los listeners asíncronos.
