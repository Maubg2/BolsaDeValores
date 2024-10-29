package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.OrdenRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenServicio;
import jakarta.transaction.Transactional;

@Service
public class OrdenServicioImpl implements OrdenServicio{

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @Autowired
    private ComisionistaServicio comisionistaServicio;


    @Override
    @Transactional
    public OrdenCompraVenta guardarOrden(OrdenCompraVenta ordenCompraVenta) {
        return ordenRepositorio.save(ordenCompraVenta);
    }

    @Override
    public List<OrdenCompraVenta> listarOrdenesPorInversionista(Long idInversionista) {
        return ordenRepositorio.encontrarOrdenPorInversionista(idInversionista);
    }

    @Override
    public List<OrdenCompraVentaDTO> listarOrdenesConNombres(Long idInversionista) {
        List<OrdenCompraVenta> ordenes = ordenRepositorio.encontrarOrdenPorInversionista(idInversionista);

        List<OrdenCompraVentaDTO> ordenesDTO = new ArrayList<>();
    
        for (OrdenCompraVenta orden : ordenes) {
            String nombreEmpresa = empresaServicio.obtenerPorId(orden.getFkEmpresa()).getNombre();
            String nombreInversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista()).getNombre();
            String nombreComisionista = comisionistaServicio.obtenerPorId(orden.getFkComisionista()).getNombre();

            OrdenCompraVentaDTO dto = new OrdenCompraVentaDTO(
                orden.getTipo(),
                orden.getEstado(),
                orden.getFechaCreacion(),
                nombreEmpresa,
                nombreInversionista,
                nombreComisionista
            );
            ordenesDTO.add(dto);
        }
        return ordenesDTO;
    } 

}