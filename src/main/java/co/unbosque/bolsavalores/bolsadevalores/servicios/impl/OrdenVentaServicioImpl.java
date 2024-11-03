package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;
import co.unbosque.bolsavalores.bolsadevalores.repositorios.OrdenVentaRepositorio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenVentaServicio;
import jakarta.transaction.Transactional;

@Service
public class OrdenVentaServicioImpl implements OrdenVentaServicio{

    @Autowired
    private OrdenVentaRepositorio ordenVentaRepositorio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @Autowired
    private ComisionistaServicio comisionistaServicio;

    @Override
    @Transactional
    public OrdenSoloVenta guardarOrdenVenta(OrdenSoloVenta ordenSoloVenta) {
        return ordenVentaRepositorio.save(ordenSoloVenta);
    }

    @Override
    public List<OrdenCompraVentaDTO> listarOrdenesVentaConNombres(Long idInversionista) {
        List<OrdenSoloVenta> ordenes = ordenVentaRepositorio.encontrarOrdenPorInversionista(idInversionista);
        List<OrdenCompraVentaDTO> ordenesDTO = new ArrayList<>();

        for(OrdenSoloVenta orden : ordenes){
            String nombreEmpresa = empresaServicio.obtenerPorId(orden.getFkEmpresa()).getNombre();
            String nombreInversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista()).getNombre();
            String nombreComisionista = comisionistaServicio.obtenerPorId(orden.getFkComisionista()).getNombre();
       
            OrdenCompraVentaDTO dto = new OrdenCompraVentaDTO(
                orden.getId(),
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

    @Override
    public List<OrdenCompraVentaDTO> listarOrdenesVentaConNombresPorComisionista(Long idComisionista) {
        return null;
    }

    @Override
    public boolean existeOrdenPendientePorEmpresaEInversionista(Long empresaId, Long inversionistaId) {
        return ordenVentaRepositorio.existsByFkEmpresaAndFkInversionistaAndEstado(empresaId, inversionistaId);
    }

    @Override
    @Transactional
    public Optional<OrdenSoloVenta> cancelarOrdenVenta(Long idOrdenVenta) {
        Optional<OrdenSoloVenta> optOrden = ordenVentaRepositorio.findById(idOrdenVenta);
        optOrden.ifPresent(orden -> {
            ordenVentaRepositorio.delete(orden);
        });
        return optOrden;
    }

    @Override
    public List<OrdenSoloVenta> listarOrdenesVentaPorComisionista(Long idComisionista) {
        return ordenVentaRepositorio.encontrarOrdenPorComisionista(idComisionista);
    }

    @Override
    public OrdenSoloVenta obtenerPorId(Long id) {
        return ordenVentaRepositorio.getReferenceById(id);
    }

}
