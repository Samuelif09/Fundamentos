# Diseño Técnico e Implementación: S-13 - Gestión ventas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-13**: 
> *"Como vendedor, quiero exportar reporte de ventas a Excel para análisis externo y contabilidad"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReporteExportable`.
* **Value Objects**: `Periodo`, `FormatoExportacion` (EXCEL/CSV).

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IExportarVentasUseCase`.
* **Output Ports (Gateways)**: `ILiquidacionGateway` (o `IPedidoGateway`), `IGeneradorReportesGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El Dominio agrupa las ventas en una matriz de datos genérica (`MatrizReporte`). La conversión física a un archivo `.xlsx` (Apache POI) es estrictamente responsabilidad del `IGeneradorReportesGateway` en Infraestructura.

### B. Domain Driven Design (DDD)
* Si en el periodo solicitado no hay transacciones, el reporte se genera igual pero con una única fila indicando "Sin movimientos", evitando excepciones.

### C. Principios SOLID
* **DIP**: Inyectando `IGeneradorReportesGateway`, mañana podemos cambiar de Excel a PDF o CSV sin alterar el dominio.

### D. Test-Driven Development (TDD)
* Mockear la consulta de ventas para que devuelva 5 registros. Testear que el interactor los transfiere correctamente a la `MatrizReporte` y llama al `IGeneradorReportesGateway` exactamente 1 vez.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Generador Dummy que construye un CSV en memoria (String con comas) y lo retorna en la respuesta HTTP.
* **Entrega 2**: Implementar el Gateway real utilizando **Apache POI** para construir el binario XLSX.
* **API Web**: GET `/api/v1/vendedores/{id}/finanzas/exportar?formato=excel&inicio=X&fin=Y`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Definir la estructura independiente `MatrizReporte`.
- [ ] 2. Crear las interfaces para la obtención de datos y la generación de archivos.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Implementar `ExportarVentasInteractor`.
- [ ] 4. (TDD) Escribir las pruebas que aseguren el ensamblado de los datos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 5. Implementar el generador de CSV en la Entrega 1.
- [ ] 6. Configurar el endpoint para retornar `ResponseEntity<byte[]>` con Header `application/vnd.ms-excel` (o texto plano para CSV).
