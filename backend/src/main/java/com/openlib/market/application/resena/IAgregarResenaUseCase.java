package com.openlib.market.application.resena;

public interface IAgregarResenaUseCase {
    void ejecutar(String isbn, AgregarResenaRequestDto request);
}
