package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.categoria.IGestionarCategoriasUseCase;
import com.openlib.market.application.configuracion.IConfigurarComisionesUseCase;
import com.openlib.market.application.configuracion.IGestionarConfiguracionSistemaUseCase;
import com.openlib.market.application.configuracion.ComisionDto;
import com.openlib.market.application.configuracion.MetodoPagoConfigDto;
import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.infrastructure.adapter.in.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/admin/configuracion")
public class AdminConfigController {

    private final IGestionarConfiguracionSistemaUseCase configSistemaUseCase;
    private final IConfigurarComisionesUseCase configurarComisionesUseCase;
    private final IGestionarCategoriasUseCase gestionarCategoriasUseCase;

    private static final String DEFAULT_CATEGORY_ID = "DEFAULT";

    public AdminConfigController(IGestionarConfiguracionSistemaUseCase configSistemaUseCase,
                                 IConfigurarComisionesUseCase configurarComisionesUseCase,
                                 IGestionarCategoriasUseCase gestionarCategoriasUseCase) {
        this.configSistemaUseCase = configSistemaUseCase;
        this.configurarComisionesUseCase = configurarComisionesUseCase;
        this.gestionarCategoriasUseCase = gestionarCategoriasUseCase;
    }

    // ── MÉTODOS DE PAGO ──────────────────────────────────────────────────────

    @GetMapping("/metodos-pago")
    public ResponseEntity<List<SysPaymentMethodDto>> getPaymentMethods() {
        List<MetodoPagoConfigDto> metodos = configSistemaUseCase.listarMetodosPago();
        List<SysPaymentMethodDto> response = metodos.stream()
                .map(m -> new SysPaymentMethodDto(m.getId(), m.getNombre(), "Stripe/PayPal", m.getEstado())) // Asumiendo proveedor
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/metodos-pago/{id}/estado")
    public ResponseEntity<String> updatePaymentMethodStatus(@PathVariable String id, @RequestBody PaymentMethodStatusRequestDto request) {
        try {
            configSistemaUseCase.cambiarEstadoMetodoPago(id, request.getStatus());
            return ResponseEntity.ok("Estado actualizado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ── COMISIONES ───────────────────────────────────────────────────────────

    @GetMapping("/comisiones")
    public ResponseEntity<SysCommissionDto> getCommissions() {
        try {
            ComisionDto comision = configurarComisionesUseCase.obtenerComisionParaCategoria(DEFAULT_CATEGORY_ID);
            return ResponseEntity.ok(new SysCommissionDto(comision.getPorcentajeComision()));
        } catch (Exception e) {
            // Valor por defecto si no está configurado
            return ResponseEntity.ok(new SysCommissionDto(15.0));
        }
    }

    @PutMapping("/comisiones")
    public ResponseEntity<SysCommissionDto> updateCommissions(@RequestBody SysCommissionDto request) {
        configurarComisionesUseCase.configurarComision(DEFAULT_CATEGORY_ID, request.getPlatformFeePercentage());
        return ResponseEntity.ok(request);
    }

    // ── CATEGORÍAS ───────────────────────────────────────────────────────────

    @GetMapping("/categorias")
    public ResponseEntity<List<SysCategoryDto>> getCategories() {
        List<CategoriaCatalogo> categorias = gestionarCategoriasUseCase.listarTodas();
        List<SysCategoryDto> response = categorias.stream()
                .map(c -> new SysCategoryDto(c.getId(), c.getNombre().getValor()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/categorias")
    public ResponseEntity<SysCategoryDto> createCategory(@RequestBody SysCategoryDto request) {
        try {
            CategoriaCatalogo cat = gestionarCategoriasUseCase.crearCategoria(request.getName());
            return ResponseEntity.ok(new SysCategoryDto(cat.getId(), cat.getNombre().getValor()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        try {
            // La capa de aplicación define 'cambiarEstado' para borrado lógico o inactivación.
            gestionarCategoriasUseCase.cambiarEstado(id, "INACTIVO");
            return ResponseEntity.ok("Categoría eliminada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
