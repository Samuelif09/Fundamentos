package com.openlib.market.application.curaduria;

import java.util.List;

public interface IRevisarCuraduriaContenidoUseCase {
    List<LibroParaRevisionDto> listarLibrosPendientes(int page, int size);
}
