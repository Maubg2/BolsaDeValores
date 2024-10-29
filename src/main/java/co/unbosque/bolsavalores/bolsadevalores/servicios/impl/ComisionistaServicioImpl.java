package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.ComisionistaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import jakarta.transaction.Transactional;

@Service
public class ComisionistaServicioImpl implements ComisionistaServicio{

    @Autowired
    private ComisionistaRepositorio comisionistaRepositorio;

    @Override
    public Comisionista obtenerPorId(Long idComisionista) {
        return comisionistaRepositorio.getReferenceById(idComisionista);
    }

    @Override
    public Comisionista obtenerPorEmail(String email) {
        Optional<Comisionista> optComisionista = comisionistaRepositorio.findByEmail(email);
        if(optComisionista.isPresent()){
            return optComisionista.get();
        }
        return null;
    }

    @Override
    public boolean validarCredenciales(String email, String contrasena) {
        Optional<Comisionista> optComisionista = comisionistaRepositorio.findByEmail(email);
        return optComisionista.map(inv -> inv.getContrasena().equals(contrasena)).orElse(false);
    }

    @Override
    @Transactional
    public Comisionista guardarComisionista(Comisionista comisionista) {
        return comisionistaRepositorio.save(comisionista);
    }

}
