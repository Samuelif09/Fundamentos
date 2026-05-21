# Diseño Técnico e Implementación: A-12 - Dashboard métricas

Este documento define el plan de implementación detallado para la Historia de Usuario **A-12**: 
> *"Como admin, quiero exportar reportes en PDF y Excel para compartir con stakeholders del proyecto"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReportePlataforma`.
* **Value Objects**: `TipoReporte` (VENTAS, USUARIOS, CONTENIDO), `Formato` (PDF, EXCEL), `ParametrosFiltro`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IExportarDashboardMetricasUseCase`.
* **Output Ports (Gateways)**: Múltiples repositorios de lectura + `IGeneradorReportesGlobalGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El sistema compila la `MatrizReporte` en la capa de Aplicación a partir de los diferentes Gateways según el `TipoReporte` solicitado, y luego delega la creación del archivo binario al `IGeneradorReportesGlobalGateway`.

### B. Domain Driven Design (DDD)
* Validar que la compilación de datos no falle si no hay registros. El DTO debe indicar que se trata de un "Reporte Vacío".

### C. Principios SOLID
* **Strategy**: Cada `TipoReporte` puede usar un `Strategy` diferente para recopilar sus datos antes de pasárselos al generador de archivos común.

### D. Test-Driven Development (TDD)
* Mockear la solicitud de un reporte de "USUARIOS" en formato "PDF". Verificar que el Interactor llama al `IUsuarioGateway` para obtener la data y luego llama al Generador pidiendo específicamente el formato PDF.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Generar CSV básicos concatenando strings por comas, independiente del formato solicitado, como un Mock de funcionalidad.
* **Entrega 2**: Implementar JasperReports o iText (para PDF) y Apache POI (para Excel) en la capa de Infraestructura.
* **API Web**: GET `/api/v1/admin/dashboard/reportes/exportar?tipo=VENTAS&formato=PDF`. Retorna un archivo como Attachment.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el objeto intermedio `ReportePlataforma`.
- [ ] 2. Definir interfaz abstracta de generación de reportes multi-formato.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar el orquestador `ExportarDashboardMetricasInteractor`.
- [ ] 4. (TDD) Escribir las pruebas de ruteo de información según el tipo de reporte.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el generador de CSV como fallback en la Entrega 1.
- [ ] 6. Controlador REST configurando `HttpHeaders.CONTENT_DISPOSITION`.
