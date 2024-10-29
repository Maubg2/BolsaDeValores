package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;

public interface OrdenServicio {

    public OrdenCompraVenta guardarOrden(OrdenCompraVenta ordenCompraVenta);

    public List<OrdenCompraVenta> listarOrdenesPorInversionista(Long idInversionista);

    public List<OrdenCompraVentaDTO> listarOrdenesConNombres(Long idInversionista);

    public List<OrdenCompraVentaDTO> listarOrdenesConNombresPorComisionista(Long idComisionista);

    public OrdenCompraVenta obtenerPorId(Long idOrden);

}
