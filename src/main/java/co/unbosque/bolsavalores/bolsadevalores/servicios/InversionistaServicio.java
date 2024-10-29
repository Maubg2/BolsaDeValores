package co.unbosque.bolsavalores.bolsadevalores.servicios;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;

public interface InversionistaServicio {

    public Inversionista obtenerPorEmail(String email);

    public boolean validarCredenciales(String email, String contrasena);

    public Inversionista guardarInversionista(Inversionista inversionista);

    public Inversionista obtenerPorId(Long id);

}
