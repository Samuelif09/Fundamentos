package com.openlib.market.domain.estadisticas;

public class EstadisticaLector {
    private final String idUsuario;
    private final int totalPedidosRealizados;
    private final int totalResenasEscritas;
    private final String categoriaFavorita;

    public EstadisticaLector(String idUsuario, int totalPedidosRealizados, int totalResenasEscritas, String categoriaFavorita) {
        this.idUsuario = idUsuario;
        this.totalPedidosRealizados = totalPedidosRealizados;
        this.totalResenasEscritas = totalResenasEscritas;
        this.categoriaFavorita = categoriaFavorita;
    }

    public String getIdUsuario() { return idUsuario; }
    public int getTotalPedidosRealizados() { return totalPedidosRealizados; }
    public int getTotalResenasEscritas() { return totalResenasEscritas; }
    public String getCategoriaFavorita() { return categoriaFavorita; }
}
