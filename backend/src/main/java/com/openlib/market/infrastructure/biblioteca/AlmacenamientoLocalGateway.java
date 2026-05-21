package com.openlib.market.infrastructure.biblioteca;

import com.openlib.market.domain.biblioteca.ArchivoDigital;
import com.openlib.market.domain.biblioteca.IAlmacenamientoGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("bibliotecaAlmacenamientoLocalGateway")
public class AlmacenamientoLocalGateway implements IAlmacenamientoGateway {

    @Override
    public Optional<ArchivoDigital> recuperarArchivo(String idLibro) {
        // En una implementación real, esto leería "Files.readAllBytes(Paths.get(...))"
        // Para esta prueba, generamos un byte[] dummy simulando un archivo PDF.
        String dummyContent = "PDF Simulado del libro " + idLibro;
        ArchivoDigital archivo = new ArchivoDigital(
                "file://local/libros/" + idLibro + ".pdf",
                "application/pdf",
                dummyContent.getBytes()
        );
        return Optional.of(archivo);
    }
}
