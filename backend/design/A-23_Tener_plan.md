# Diseño Técnico e Implementación: A-23 - Soporte

Este documento define el plan de implementación detallado para la Historia de Usuario **A-23**: 
> *"Como admin, quiero chatbot de soporte de primera línea para reducir la carga de tickets manuales"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `SesionChatbot`.
* **Value Objects**: `IdUsuario`, `MensajeUsuario`, `RespuestaBot`, `NivelConfianza` (0.0 a 1.0), `EstadoSesion` (ACTIVA, ESCALADA_A_HUMANO, RESUELTA).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IProcesarMensajeChatbotUseCase`.
* **Output Ports (Gateways)**: `IChatbotGateway` (Proveedor de IA/NLP), `ITicketSoporteGateway` (Para escalar si no sabe la respuesta).

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El procesamiento del lenguaje natural (NLP) se delega a infraestructura. El dominio recibe un `MensajeUsuario`, lo envía al `IChatbotGateway`, y recibe una `RespuestaBot` con un `NivelConfianza`.

### B. Domain Driven Design (DDD)
* Si el `NivelConfianza` de la respuesta de la IA es bajo (ej. < 0.6), el dominio cambia el `EstadoSesion` a `ESCALADA_A_HUMANO` y orquesta la creación automática de un `TicketSoporte` (A-09).

### C. Principios SOLID
* **OCP**: La lógica de escalamiento se mantiene independiente del motor de IA elegido (Dialogflow, OpenAI, Gemini), ya que se interactúa únicamente a través de la abstracción `IChatbotGateway`.

### D. Test-Driven Development (TDD)
* Mockear el proveedor de IA para que devuelva una respuesta con confianza 0.3 (No entiende la pregunta). Validar que el Interactor llama automáticamente al `ITicketSoporteGateway` para abrir un ticket y responde al usuario "Te transferiré con un agente".

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: `ChatbotDummyGateway` que responde usando un mapa estático de palabras clave (ej. Si el mensaje contiene "reembolso", responde con un texto fijo).
* **Entrega 2**: Integrar un proveedor de IA real (ej. Dialogflow CX o Gemini API) y persistir el historial del chat en MongoDB o Redis.
* **API Web**: POST `/api/v1/soporte/chatbot/mensajes`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `SesionChatbot` y definir los umbrales de confianza.
- [ ] 2. (TDD) Escribir pruebas de las transiciones (ej. Auto-resolución vs Escalamiento).

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el Interactor `ProcesarMensajeChatbotInteractor`.
- [ ] 4. Integrar lógica de creación de tickets en caso de fallo de IA.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el Dummy Gateway (basado en Regex/Palabras clave).
- [ ] 6. Controlador REST para interactuar con la ventana de chat del frontend.
