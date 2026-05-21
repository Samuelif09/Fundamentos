package com.openlib.market.domain.exportacion;

import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.suscripcion.SuscripcionAutor;

import java.util.List;

public class DataExportadaUsuario {
    private final Usuario perfil;
    private final List<Pedido> pedidos;
    private final List<SuscripcionAutor> suscripciones;
    // Agregamos más colecciones a medida que se necesite, ej: reportes, historial

    public DataExportadaUsuario(Usuario perfil, List<Pedido> pedidos, List<SuscripcionAutor> suscripciones) {
        this.perfil = perfil;
        this.pedidos = pedidos;
        this.suscripciones = suscripciones;
    }

    public Usuario getPerfil() { return perfil; }
    public List<Pedido> getPedidos() { return pedidos; }
    public List<SuscripcionAutor> getSuscripciones() { return suscripciones; }
}
