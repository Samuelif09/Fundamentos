package com.openlib.market.application.resena;

import java.util.List;

public interface ILeerResenasUseCase {
    List<ResenaResponseDto> leerResenas(String isbnLibro, int offset, int limit);
}
