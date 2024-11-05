package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;

public interface EmpresaServicio {

    public List<Empresa> listarEmpresas();

    public Empresa obtenerPorId(Long id);

    public void actualizarValorDeAccion();

    public void calcularNuevoSaldo();

}
