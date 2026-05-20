package com.openlib.market.infrastructure.afiliado;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.domain.afiliado.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AfiliadoJsonGateway implements IAfiliadoGateway {

    private final ObjectMapper objectMapper;
    private final File fileProgramas;
    private final File fileEnlaces;
    private List<ProgramaDto> programasEnMemoria;
    private List<EnlaceDto> enlacesEnMemoria;

    public AfiliadoJsonGateway() {
        this.objectMapper = new ObjectMapper();
        this.fileProgramas = new File("afiliados_programas.json");
        this.fileEnlaces = new File("afiliados_enlaces.json");
        cargarDatos();
    }

    private void cargarDatos() {
        if (fileProgramas.exists()) {
            try {
                this.programasEnMemoria = objectMapper.readValue(fileProgramas, new TypeReference<List<ProgramaDto>>() {});
            } catch (Exception e) {
                this.programasEnMemoria = new ArrayList<>();
            }
        } else {
            this.programasEnMemoria = new ArrayList<>();
        }

        if (fileEnlaces.exists()) {
            try {
                this.enlacesEnMemoria = objectMapper.readValue(fileEnlaces, new TypeReference<List<EnlaceDto>>() {});
            } catch (Exception e) {
                this.enlacesEnMemoria = new ArrayList<>();
            }
        } else {
            this.enlacesEnMemoria = new ArrayList<>();
        }
    }

    private void guardarDatos() {
        try {
            objectMapper.writeValue(fileProgramas, programasEnMemoria);
            objectMapper.writeValue(fileEnlaces, enlacesEnMemoria);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardarPrograma(ProgramaAfiliado programa) {
        programasEnMemoria.removeIf(p -> p.idVendedor().equals(programa.getIdVendedor()));
        programasEnMemoria.add(new ProgramaDto(programa.getIdVendedor(), programa.getComisionAfiliado().getValor()));
        guardarDatos();
    }

    @Override
    public void guardarEnlace(EnlaceAfiliado enlace) {
        enlacesEnMemoria.add(new EnlaceDto(enlace.getIdAfiliado(), enlace.getIdVendedor(), enlace.getCodigoRastreo().getValor()));
        guardarDatos();
    }

    @Override
    public Optional<ProgramaAfiliado> obtenerProgramaPorVendedor(String idVendedor) {
        return programasEnMemoria.stream()
                .filter(p -> p.idVendedor().equals(idVendedor))
                .map(p -> new ProgramaAfiliado(p.idVendedor(), new PorcentajeComisionAfiliado(p.comision())))
                .findFirst();
    }

    private record ProgramaDto(String idVendedor, double comision) {}
    private record EnlaceDto(String idAfiliado, String idVendedor, String codigoRastreo) {}
}
