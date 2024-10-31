package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.AccionRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.AccionServicio;
import jakarta.transaction.Transactional;

@Service
public class AccionServicioImpl implements AccionServicio{

    @Autowired
    private AccionRepositorio accionRepositorio;

    @Override
    @Transactional
    public Accion guardarAccion(Accion accion) {
        return accionRepositorio.save(accion);
    }

    @Override
    public List<Accion> listarAccionesPorInversionista(Long idInversionista) {
        return accionRepositorio.listarAccionPorInversionista(idInversionista);
    }

}
