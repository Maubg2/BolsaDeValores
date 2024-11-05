package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.AccionRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.EmpresaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.InversionistaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;

@Service
public class EmpresaServicioImpl implements EmpresaServicio{

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private InversionistaRepositorio inversionistaRepositorio;

    @Autowired
    private AccionRepositorio accionRepositorio;

    private final Random random = new Random();

    @Override
    public List<Empresa> listarEmpresas() {
        return empresaRepositorio.findAll();
    }

    @Override
    public Empresa obtenerPorId(Long id) {
        return empresaRepositorio.getReferenceById(id);
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void actualizarValorDeAccion() {
        List<Empresa> empresas = empresaRepositorio.findAll();
        for(Empresa empresa : empresas){
            double variacion = -3 + (3 - (-3)) * random.nextDouble();
            System.out.println(variacion);
            empresa.setVariacionAccion(Math.floor(variacion * 100) / 100);
            empresa.setValorAccion(Math.floor((empresa.getValorAccion() * (1 + variacion / 100)) * 100) / 100);
            //    empresa.setValorAccion(empresa.getValorAccion() * (1 + variacion / 100));
            empresaRepositorio.save(empresa);
        }

        calcularNuevoSaldo();

    }

    @Override
    public void calcularNuevoSaldo() {
        List<Accion> acciones = accionRepositorio.findAll();
        for(Accion accion : acciones){
            if(accion.getFkInversionista() != null){
                Inversionista inversionista = inversionistaRepositorio.getReferenceById(accion.getFkInversionista());
                Empresa empresa = empresaRepositorio.getReferenceById(accion.getFkEmpresa());

                inversionista.setSaldo(empresa.getVariacionAccion() > 0 ? 
                    inversionista.getSaldo() + 1000 * empresa.getVariacionAccion() : 
                    inversionista.getSaldo() - 1000 * empresa.getVariacionAccion());
                
                inversionistaRepositorio.save(inversionista);
            }
        }        
    }

}
