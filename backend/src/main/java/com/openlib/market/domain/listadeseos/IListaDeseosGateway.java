package com.openlib.market.domain.listadeseos;

import java.util.List;

public interface IListaDeseosGateway {
    /**
     * Retorna la lista de IDs de usuarios que tienen el libro en su lista de deseos.
     */
    List<String> obtenerUsuariosInteresados(String idLibro);

    void guardar(String idUsuario, java.util.Set<String> isbns);
    java.util.Set<String> obtenerPorUsuario(String idUsuario);
}
