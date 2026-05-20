package com.openlib.market.infrastructure.almacenamiento;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class AlmacenamientoLocalGateway implements IAlmacenamientoGateway {

    private static final String DIRECTORIO_BASE = "uploads";

    @Override
    public String guardar(ArchivoImagen archivo, String contexto) {
        try {
            Path directorio = Paths.get(DIRECTORIO_BASE, contexto);
            Files.createDirectories(directorio);

            String extension = archivo.getNombreOriginal().contains(".")
                    ? archivo.getNombreOriginal().substring(archivo.getNombreOriginal().lastIndexOf("."))
                    : "";
            String nombreUnico = UUID.randomUUID() + extension;
            Path rutaArchivo = directorio.resolve(nombreUnico);

            Files.write(rutaArchivo, archivo.getContenido());
            return rutaArchivo.toString();

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
}
