package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;
import java.util.Optional;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;

public interface AccionServicio {

    public Accion guardarAccion(Accion accion);

    public List<Accion> listarAccionesPorInversionista(Long idInversionista);

    public Optional<Accion> eliminarAccion(Long idAccion);

    public Accion obtenerPorId(Long idAccion);

}
