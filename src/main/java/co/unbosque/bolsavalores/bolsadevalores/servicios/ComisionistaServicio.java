package co.unbosque.bolsavalores.bolsadevalores.servicios;


import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;

public interface ComisionistaServicio {

    public Comisionista obtenerPorId(Long idComisionista);

    public Comisionista obtenerPorEmail(String email);

    public boolean validarCredenciales(String email, String contrasena);

    public Comisionista guardarComisionista(Comisionista comisionista);

}
