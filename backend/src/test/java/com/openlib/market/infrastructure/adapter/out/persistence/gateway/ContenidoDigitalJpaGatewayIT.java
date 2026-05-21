package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.detalle.*;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ContenidoDigitalMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ContenidoDigitalJpaGateway.class, ContenidoDigitalMapper.class})
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ContenidoDigitalJpaGatewayIT {

    @Autowired
    private ContenidoDigitalJpaGateway gateway;

    @Test
    void debeGuardarYRecuperarPolimorficamente() {
        Audiolibro audio = new Audiolibro(new Isbn("978-0-111-22222-1"), "Audio Test", "Sinopsis", new Precio(100), "url", "Cat", "vend-1", new DuracionEnMinutos(120));
        CursoVirtual curso = new CursoVirtual(new Isbn("978-0-111-33333-1"), "Curso Test", "Sinopsis", new Precio(200), "url", "Cat", "vend-1", new DuracionEnMinutos(600));

        gateway.guardarContenido(audio);
        gateway.guardarContenido(curso);

        Optional<ContenidoDigital> recAudio = gateway.obtenerContenidoPorId("978-0-111-22222-1");
        assertTrue(recAudio.isPresent());
        assertTrue(recAudio.get() instanceof Audiolibro);
        assertEquals(120, ((Audiolibro) recAudio.get()).getDuracion().getValor());

        Optional<ContenidoDigital> recCurso = gateway.obtenerContenidoPorId("978-0-111-33333-1");
        assertTrue(recCurso.isPresent());
        assertTrue(recCurso.get() instanceof CursoVirtual);
        assertEquals(600, ((CursoVirtual) recCurso.get()).getDuracionEstimada().getValor());
    }
}
