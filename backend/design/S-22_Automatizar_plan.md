# Diseño Técnico e Implementación: S-22 - Finanzas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-22**: 
> *"Como vendedor, quiero facturación automática de mis ventas para simplificar obligaciones tributarias"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `FacturaTributaria`.
* **Value Objects**: `DatosFiscalesVendedor`, `DatosFiscalesComprador`, `DesgloseImpuestos` (IVA, Retenciones).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IGenerarFacturaFinanzasUseCase` (Generalmente invocado por un Evento de Dominio).
* **Output Ports (Gateways)**: `IFacturacionGateway`, `IPedidoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* La generación de la factura es agnóstica a si en el país se factura con la DIAN (Colombia) o el SAT (México). El dominio solo arma la `FacturaTributaria`; la integración con entidades gubernamentales es exclusiva del `IFacturacionGateway` (Infraestructura).

### B. Domain Driven Design (DDD)
* Un Value Object `DesgloseImpuestos` calcula en el constructor el impuesto aplicable (ej. IVA de productos digitales) sobre el subtotal del pedido.

### C. Principios SOLID
* **SRP**: Separar el cobro (Checkout, C-06) de la facturación formal contable.

### D. Test-Driven Development (TDD)
* Mockear un pedido y validar que la `FacturaTributaria` calcula correctamente Base Imponible + Impuestos = Total sin discrepancias de decimales.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Implementar un `FacturaJsonGateway` que simplemente guarde un registro en `facturas.json` simulando la emisión.
* **Entrega 2**: Implementar la generación de un archivo PDF (`PdfGeneratorAdapter` con iText o JasperReports) y el firmado electrónico.
* **API Web**: Suscripción interna a evento de compra, y endpoint GET `/api/v1/vendedores/{id}/facturas/{facturaId}/descargar`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear el Agregado `FacturaTributaria` y la lógica tributaria.
- [ ] 2. (TDD) Tests asegurando consistencia matemática.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar Event Listener `GenerarFacturaFinanzasInteractor`.
- [ ] 4. Implementar UseCase de descarga de factura.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar almacenamiento simulado.
- [ ] 6. Controlador REST para consulta de PDF.
