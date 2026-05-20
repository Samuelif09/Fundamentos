package com.openlib.market.application.soporte;

import java.util.List;

public interface IVerSoporteUseCase {
    List<TicketSoporteDto> listarTicketsAbiertos(int page, int size);
}
