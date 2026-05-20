package com.openlib.market.infrastructure.pago;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.openlib.market.domain.dashboardGlobal.IDashboardGlobalGateway;

@Component
public class PedidoJsonGateway implements IPedidoGateway, IDashboardGlobalGateway {

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
    public java.util.Optional<Pedido> obtenerPorId(String id) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> dto.id().equals(id))
                .findFirst()
                .map(dto -> new Pedido(
                        dto.id(),
                        dto.sesionId(),
                        dto.idUsuario(),
                        dto.total(),
                        com.openlib.market.domain.pago.EstadoPedido.valueOf(dto.estado()),
                        dto.fecha() != null ? java.time.LocalDateTime.parse(dto.fecha()) : null,
                        dto.tipoMetodoPago() != null ? com.openlib.market.domain.pago.TipoMetodoPago.valueOf(dto.tipoMetodoPago()) : null
                ));
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

    @Override
    public List<Pedido> listarTodos(int page, int size) {
        return baseDatosEnMemoria.stream()
                .map(dto -> new Pedido(
                        dto.id(),
                        dto.sesionId(),
                        dto.idUsuario(),
                        dto.total(),
                        com.openlib.market.domain.pago.EstadoPedido.valueOf(dto.estado()),
                        dto.fecha() != null ? java.time.LocalDateTime.parse(dto.fecha()) : java.time.LocalDateTime.now(),
                        dto.tipoMetodoPago() != null ? com.openlib.market.domain.pago.TipoMetodoPago.valueOf(dto.tipoMetodoPago()) : null
                ))
                .sorted((p1, p2) -> p2.getFecha().compareTo(p1.getFecha()))
                .skip((long) page * size)
                .limit(size)
                .toList();
    }

    @Override
    public List<Pedido> obtenerPedidosExitososDePlataforma(int anio) {
        return baseDatosEnMemoria.stream()
                .filter(dto -> "PAGADO".equals(dto.estado()))
                .filter(dto -> dto.fecha() != null && dto.fecha().startsWith(String.valueOf(anio)))
                .map(dto -> new Pedido(
                        dto.id(),
                        dto.sesionId(),
                        dto.idUsuario(),
                        dto.total(),
                        com.openlib.market.domain.pago.EstadoPedido.valueOf(dto.estado()),
                        java.time.LocalDateTime.parse(dto.fecha()),
                        dto.tipoMetodoPago() != null ? com.openlib.market.domain.pago.TipoMetodoPago.valueOf(dto.tipoMetodoPago()) : null
                ))
                .toList();
    }

    private record PedidoDto(String id, String sesionId, String idUsuario, double total, String estado, String fecha, String tipoMetodoPago) {}
}
