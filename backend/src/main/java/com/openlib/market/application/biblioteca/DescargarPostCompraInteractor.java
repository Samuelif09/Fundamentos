package com.openlib.market.application.biblioteca;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.biblioteca.*;

@Service
public class DescargarPostCompraInteractor implements IDescargarPostCompraUseCase {

    private final IBibliotecaGateway bibliotecaGateway;
    private final IAlmacenamientoGateway almacenamientoGateway;

    public DescargarPostCompraInteractor(IBibliotecaGateway bibliotecaGateway, IAlmacenamientoGateway almacenamientoGateway) {
        this.bibliotecaGateway = bibliotecaGateway;
        this.almacenamientoGateway = almacenamientoGateway;
    }

    @Override
    public ArchivoDigital descargarLibro(String idUsuario, String idLibro) {
        LicenciaAcceso licencia = new LicenciaAcceso(idUsuario, idLibro);

        if (!bibliotecaGateway.validarLicencia(licencia)) {
            throw new AccesoDenegadoException("El usuario no posee una licencia válida para este libro.");
        }

        return almacenamientoGateway.recuperarArchivo(idLibro)
                .orElseThrow(() -> new RuntimeException("El archivo digital del libro no se encuentra disponible."));
    }
}
