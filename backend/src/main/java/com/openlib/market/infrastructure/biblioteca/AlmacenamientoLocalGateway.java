package com.openlib.market.infrastructure.biblioteca;

import com.openlib.market.domain.biblioteca.ArchivoDigital;
import com.openlib.market.domain.biblioteca.IAlmacenamientoGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

@Component("bibliotecaAlmacenamientoLocalGateway")
public class AlmacenamientoLocalGateway implements IAlmacenamientoGateway {

    private final ContenidoDigitalRepository contenidoRepository;

    public AlmacenamientoLocalGateway(ContenidoDigitalRepository contenidoRepository) {
        this.contenidoRepository = contenidoRepository;
    }

    @Override
    public Optional<ArchivoDigital> recuperarArchivo(String idLibro) {
        // Buscar el libro para ver si tiene un archivo real cargado por el vendedor
        Optional<ContenidoDigitalEntity> entidadOpt = contenidoRepository.findById(idLibro);
        
        if (entidadOpt.isPresent()) {
            ContenidoDigitalEntity entidad = entidadOpt.get();
            String urlVistaPrevia = entidad.getUrlVistaPrevia();
            
            if (urlVistaPrevia != null && urlVistaPrevia.startsWith("/previews/")) {
                String nombreArchivo = urlVistaPrevia.substring("/previews/".length());
                File archivoReal = new File("uploads/previews", nombreArchivo);
                
                if (archivoReal.exists()) {
                    try {
                        byte[] contenido = Files.readAllBytes(archivoReal.toPath());
                        String mimeType = nombreArchivo.toLowerCase().endsWith(".epub") ? "application/epub+zip" : "application/pdf";
                        ArchivoDigital archivo = new ArchivoDigital(
                                "file://local/libros/" + nombreArchivo,
                                mimeType,
                                contenido
                        );
                        return Optional.of(archivo);
                    } catch (Exception e) {
                        System.err.println("Error leyendo el archivo real: " + e.getMessage());
                    }
                }
            }
        }

        // Si no hay archivo real (libros antiguos), generamos un byte[] dummy simulando un archivo PDF válido.
        String minimalPdf = "%PDF-1.4\n" +
                "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n" +
                "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n" +
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n" +
                "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n" +
                "5 0 obj\n<< /Length 50 >>\nstream\nBT /F1 24 Tf 100 700 Td (Libro: " + idLibro + ") Tj ET\nendstream\nendobj\n" +
                "xref\n0 6\n0000000000 65535 f \n0000000009 00000 n \n0000000058 00000 n \n0000000115 00000 n \n0000000224 00000 n \n0000000312 00000 n \ntrailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n413\n%%EOF\n";
        
        ArchivoDigital archivo = new ArchivoDigital(
                "file://local/libros/" + idLibro + ".pdf",
                "application/pdf",
                minimalPdf.getBytes()
        );
        return Optional.of(archivo);
    }
}
