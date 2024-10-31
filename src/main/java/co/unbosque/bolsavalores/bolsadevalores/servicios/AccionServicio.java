package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;

public interface AccionServicio {

    public Accion guardarAccion(Accion accion);

    public List<Accion> listarAccionesPorInversionista(Long idInversionista);

}
