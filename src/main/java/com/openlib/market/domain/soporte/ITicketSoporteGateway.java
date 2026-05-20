package com.openlib.market.domain.soporte;

import java.util.List;

public interface ITicketSoporteGateway {
    List<TicketSoporte> listarPorEstados(List<EstadoTicket> estados, int page, int size);
}
