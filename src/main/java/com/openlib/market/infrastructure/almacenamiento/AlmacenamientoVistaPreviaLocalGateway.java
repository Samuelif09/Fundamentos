package com.openlib.market.infrastructure.almacenamiento;

import com.openlib.market.domain.almacenamiento.ArchivoVistaPrevia;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoVistaPreviaGateway;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AlmacenamientoVistaPreviaLocalGateway implements IAlmacenamientoVistaPreviaGateway {

    private final String directorioBase = "uploads/previews";

    public AlmacenamientoVistaPreviaLocalGateway() {
        File dir = new File(directorioBase);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public String guardar(ArchivoVistaPrevia archivo, String contexto) {
        String nombreArchivo = contexto + "_" + System.currentTimeMillis() + archivo.obtenerExtension();
        File archivoSalida = new File(directorioBase, nombreArchivo);

        try (FileOutputStream fos = new FileOutputStream(archivoSalida)) {
            fos.write(archivo.getContenido());
            // En un entorno local, podríamos simular una URL sirviendo estáticos. Para el MVP retornamos la ruta o URL local
            return "/previews/" + nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la vista previa en el almacenamiento local", e);
        }
    }
}
