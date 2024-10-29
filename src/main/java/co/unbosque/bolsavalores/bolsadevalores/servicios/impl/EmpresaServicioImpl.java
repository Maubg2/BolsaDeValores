package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.EmpresaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;

@Service
public class EmpresaServicioImpl implements EmpresaServicio{

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Override
    public List<Empresa> listarEmpresas() {
        return empresaRepositorio.findAll();
    }

    @Override
    public Empresa obtenerPorId(Long id) {
        return empresaRepositorio.getReferenceById(id);
    }

}
