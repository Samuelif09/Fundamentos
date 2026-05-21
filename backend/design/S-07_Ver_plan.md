# Diseño Técnico e Implementación: S-07 - Finanzas

Este documento define el plan de implementación detallado para la Historia de Usuario **S-07**: 
> *"Como vendedor, quiero ver mis ingresos totales del período para conocer mis ganancias"*

El diseño se basa en **Clean Architecture**, **Domain-Driven Design (DDD)**, **TDD** y los principios **SOLID**.

---

## 1. Análisis y Modelado de Dominio

### 1.1 Entidades y Agregados (Aggregate Roots)
* **Agregado Principal**: `ReporteFinanciero`.
* **Value Objects**: `IdVendedor`, `Periodo` (Fecha Inicio, Fecha Fin), `MontoTotal`.

### 1.2 Puertos (Ports - In/Out)
* **Input Port (Use Case)**: Interfaz `IVerFinanzasUseCase`.
* **Output Port (Gateway)**: `ILiquidacionGateway` o `IPedidoGateway`.

---

## 2. 🛡️ Cumplimiento de Patrones y Principios

### A. Clean Architecture
* El cálculo financiero es crucial. La suma de los montos no es un simple cálculo SQL, sino un modelo de negocio que debe instanciarse en la capa de Dominio a través de un `CalculadoraIngresosDomainService`.

### B. Domain Driven Design (DDD)
* Validar que la Fecha Fin del VO `Periodo` no sea menor a la Fecha Inicio.
* Si no hay ventas en el periodo, retornar un reporte válido con saldo 0 en lugar de `null` o excepción.

### C. Principios SOLID
* **OCP**: La estrategia de filtrado de periodos puede extenderse (Mes actual, Trimestre, Año) sin modificar el motor de suma.

### D. Test-Driven Development (TDD)
* Mockear 3 transacciones: 2 dentro del periodo y 1 fuera. Verificar que el `VerFinanzasInteractor` suma solo las 2 válidas y respeta el redondeo monetario.

---

## 3. Diseño de la Solución (Persistencia y Endpoints)

* **Entrega 1**: Leer todos los pedidos del `pedidos.json`, filtrar los que contengan items del vendedor, verificar que su fecha coincida con el rango, y sumar en memoria (Stream & Reduce).
* **Entrega 2**: Implementar una consulta JPQL con funciones de agregación: `SELECT SUM(i.subtotal) FROM ItemPedido i WHERE i.libro.idVendedor = :id AND i.pedido.fecha BETWEEN :inicio AND :fin`.
* **API Web**: GET `/api/v1/vendedores/{sellerId}/finanzas/ingresos?desde=X&hasta=Y`.

---

## 4. 📋 Tareas de Implementación (Checklist)

### 🟢 Capa de Dominio (Domain)
- [ ] 1. Crear Value Object `Periodo`.
- [ ] 2. Crear Entidad `ReporteFinanciero`.

### 🟡 Capa de Aplicación (Application)
- [ ] 3. Crear DTO para el reporte de ingresos.
- [ ] 4. (TDD) Pruebas sobre la suma y filtrado de periodos.
- [ ] 5. Implementar el Interactor orquestando los datos.

### 🔵 Capa de Infraestructura (Infrastructure)
- [ ] 6. Implementar filtros de fecha y suma en JSON Gateway.
- [ ] 7. Implementar el Controlador REST mapeando parámetros de URL.
