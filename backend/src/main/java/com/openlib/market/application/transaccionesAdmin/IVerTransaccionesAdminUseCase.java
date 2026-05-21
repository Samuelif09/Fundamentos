package com.openlib.market.application.transaccionesAdmin;

import java.util.List;

public interface IVerTransaccionesAdminUseCase {
    List<TransaccionGlobalDto> listarTransacciones(int page, int size);
}
