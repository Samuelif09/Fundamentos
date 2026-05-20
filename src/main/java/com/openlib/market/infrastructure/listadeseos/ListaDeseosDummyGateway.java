package com.openlib.market.infrastructure.listadeseos;

import com.openlib.market.domain.listadeseos.IListaDeseosGateway;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ListaDeseosDummyGateway implements IListaDeseosGateway {

    // Simula una base de datos de listas de deseos: Map<IdLibro, List<IdUsuario>>
    private final Map<String, List<String>> interesadosPorLibro = new ConcurrentHashMap<>();

    public ListaDeseosDummyGateway() {
        // Datos dummy para probar la historia C-20
        List<String> interesados = new ArrayList<>();
        interesados.add("usuario1");
        interesados.add("usuario2");
        interesadosPorLibro.put("978-0134685991", interesados); // Effective Java
    }

    @Override
    public List<String> obtenerUsuariosInteresados(String idLibro) {
        return interesadosPorLibro.getOrDefault(idLibro, new ArrayList<>());
    }
}
