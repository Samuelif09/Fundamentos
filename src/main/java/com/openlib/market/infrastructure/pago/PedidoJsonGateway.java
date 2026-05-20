package com.openlib.market.infrastructure.pago;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoJsonGateway implements IPedidoGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;
    private List<PedidoDto> baseDatosEnMemoria;

    public PedidoJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("pedidos.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (jsonFile.exists()) {
            try {
                this.baseDatosEnMemoria = objectMapper.readValue(jsonFile, new TypeReference<List<PedidoDto>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                this.baseDatosEnMemoria = new ArrayList<>();
            }
        } else {
            this.baseDatosEnMemoria = new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(jsonFile, baseDatosEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(Pedido pedido) {
        PedidoDto dto = new PedidoDto(
                pedido.getId(),
                pedido.getSesionId(),
                pedido.getIdUsuario(),
                pedido.getTotal(),
                pedido.getEstado().name(),
                pedido.getFecha().toString(),
                pedido.getTipoMetodoPago() != null ? pedido.getTipoMetodoPago().name() : null
        );
        
        // Simular un "upsert" removiendo el viejo si existe y agregando el nuevo estado
        baseDatosEnMemoria.removeIf(p -> p.id().equals(pedido.getId()));
        baseDatosEnMemoria.add(dto);
        
        guardarDatos();
    }

    @Override
    public List<Pedido> listarPorUsuarioId(String idUsuario, int offset, int limit) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> idUsuario.equals(dto.idUsuario()))
                .skip(offset)
                .limit(limit)
                .map(dto -> new Pedido(
                        dto.id(),
                        dto.sesionId(),
                        dto.idUsuario(),
                        dto.total(),
                        com.openlib.market.domain.pago.EstadoPedido.valueOf(dto.estado()),
                        dto.fecha() != null ? java.time.LocalDateTime.parse(dto.fecha()) : java.time.LocalDateTime.now(),
                        dto.tipoMetodoPago() != null ? com.openlib.market.domain.pago.TipoMetodoPago.valueOf(dto.tipoMetodoPago()) : null
                ))
                .sorted((p1, p2) -> p2.getFecha().compareTo(p1.getFecha())) // Descendente
                .toList();
    }

    private record PedidoDto(String id, String sesionId, String idUsuario, double total, String estado, String fecha, String tipoMetodoPago) {}
}
