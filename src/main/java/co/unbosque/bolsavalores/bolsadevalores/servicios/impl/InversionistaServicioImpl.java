package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.InversionistaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import jakarta.transaction.Transactional;

@Service
public class InversionistaServicioImpl implements InversionistaServicio{

    @Autowired
    private InversionistaRepositorio inversionistaRepositorio;

    @Override
    public Inversionista obtenerPorEmail(String email) {
        Optional<Inversionista> optInversionista = inversionistaRepositorio.findByEmail(email);
        if(optInversionista.isPresent()){
            return optInversionista.get();
        }
        return null;
    }

    @Override
    public boolean validarCredenciales(String email, String contrasena) {
        Optional<Inversionista> optInversionista = inversionistaRepositorio.findByEmail(email);
        return optInversionista.map(inv -> inv.getContrasena().equals(contrasena)).orElse(false);
    }

    @Override
    @Transactional
    public Inversionista guardarInversionista(Inversionista inversionista) {
        return inversionistaRepositorio.save(inversionista);
    }

    @Override
    public Inversionista obtenerPorId(Long id) {
        return inversionistaRepositorio.getReferenceById(id);
    }

}
