package com.openlib.market.application.resena;

import java.util.List;

public interface IVerResenasUseCase {
    List<ResenaDto> verResenas(String isbnLibro);
}
