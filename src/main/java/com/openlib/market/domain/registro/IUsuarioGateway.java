package com.openlib.market.domain.registro;

import java.util.Optional;

public interface IUsuarioGateway {
    Optional<Usuario> buscarPorId(String id);
    void actualizar(Usuario usuario);
    java.util.List<Usuario> listarTodos();
}
