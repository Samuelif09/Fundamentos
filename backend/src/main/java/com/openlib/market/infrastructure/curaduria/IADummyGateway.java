package com.openlib.market.infrastructure.curaduria;

import com.openlib.market.domain.curaduria.IInteligenciaArtificialGateway;
import com.openlib.market.domain.curaduria.ScoreToxicidad;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IADummyGateway implements IInteligenciaArtificialGateway {

    private final List<String> palabrasBloqueadas = List.of(
            "odio", "fraude", "estafa", "violencia", "ilegal"
    );

    @Override
    public ScoreToxicidad analizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return new ScoreToxicidad(0.0);
        }

        String textoNormalizado = texto.toLowerCase();
        int conteoMalasPalabras = 0;

        for (String palabra : palabrasBloqueadas) {
            if (textoNormalizado.contains(palabra)) {
                conteoMalasPalabras++;
            }
        }

        // Regla dummy: 0 palabras = 0.0, 1 palabra = 0.5, >=2 palabras = 0.9
        if (conteoMalasPalabras == 0) return new ScoreToxicidad(0.1);
        if (conteoMalasPalabras == 1) return new ScoreToxicidad(0.5);
        return new ScoreToxicidad(0.9);
    }
}
