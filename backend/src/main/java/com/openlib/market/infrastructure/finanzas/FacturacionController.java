package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.application.finanzas.DescargarFacturaUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/facturas")
public class FacturacionController {

    private final DescargarFacturaUseCase descargarFacturaUseCase;

    public FacturacionController(DescargarFacturaUseCase descargarFacturaUseCase) {
        this.descargarFacturaUseCase = descargarFacturaUseCase;
    }

    @GetMapping("/{idFactura}/descargar")
    public ResponseEntity<String> descargarFactura(
            @PathVariable String idVendedor,
            @PathVariable String idFactura) {
        try {
            String contenidoFactura = descargarFacturaUseCase.descargar(idFactura);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + idFactura + ".txt");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(contenidoFactura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
