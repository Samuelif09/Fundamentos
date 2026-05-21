package com.openlib.market.application.comunicado;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.comunicado.ComunicadoMasivo;
import com.openlib.market.domain.comunicado.FiltroDestinatarios;
import com.openlib.market.domain.comunicado.INotificacionGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnviarComunicadoInteractor implements IEnviarComunicadoUseCase {

    private final IUsuarioGateway usuarioGateway;
    private final INotificacionGateway notificacionGateway;

    public EnviarComunicadoInteractor(IUsuarioGateway usuarioGateway, INotificacionGateway notificacionGateway) {
        this.usuarioGateway = usuarioGateway;
        this.notificacionGateway = notificacionGateway;
    }

    @Override
    public ComunicadoDto enviar(String asunto, String cuerpoMensaje, String filtroStr) {
        FiltroDestinatarios filtro = FiltroDestinatarios.valueOf(filtroStr.toUpperCase());
        ComunicadoMasivo comunicado = new ComunicadoMasivo(asunto, cuerpoMensaje, filtro);

        List<Usuario> todosLosUsuarios = usuarioGateway.listarTodos();
        
        List<String> correosFiltrados = todosLosUsuarios.stream()
                .filter(u -> cumpleFiltro(u, filtro))
                .map(u -> u.getEmail().getValor())
                .collect(Collectors.toList());

        comunicado.registrarEnvio(correosFiltrados.size());
        
        if (!correosFiltrados.isEmpty()) {
            notificacionGateway.enviarComunicadoMasivo(comunicado, correosFiltrados);
        }

        return new ComunicadoDto(
                comunicado.getId(),
                comunicado.getAsunto(),
                comunicado.getFechaEnvio().toString(),
                comunicado.getCantidadDestinatarios()
        );
    }

    private boolean cumpleFiltro(Usuario usuario, FiltroDestinatarios filtro) {
        if (filtro == FiltroDestinatarios.TODOS) return true;
        if (filtro == FiltroDestinatarios.SOLO_COMPRADORES && usuario.getRol() == RolUsuario.COMPRADOR) return true;
        if (filtro == FiltroDestinatarios.SOLO_VENDEDORES && usuario.getRol() == RolUsuario.VENDEDOR) return true;
        return false;
    }
}
