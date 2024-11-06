package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;
import java.util.Optional;

import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;

public interface OrdenVentaServicio {

    public OrdenSoloVenta guardarOrdenVenta(OrdenSoloVenta ordenSoloVenta);

    public List<OrdenCompraVentaDTO> listarOrdenesVentaConNombres(Long idInversionista);

    public List<OrdenCompraVentaDTO> listarOrdenesVentaConNombresPorComisionista(Long idComisionista);

    public boolean existeOrdenPendientePorEmpresaEInversionista(Long empresaId, Long inversionistaId);

    public Optional<OrdenSoloVenta> cancelarOrdenVenta(Long idOrdenVenta);

    public List<OrdenSoloVenta> listarOrdenesVentaPorComisionista(Long idComisionista);

    public OrdenSoloVenta obtenerPorId(Long id);

    public List<OrdenSoloVenta> listarOrdenesVentaPorInversionista(Long idInversionista);

}
