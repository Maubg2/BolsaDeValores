package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;

import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;

public interface ReporteServicio {

    public byte[] generarReportePDF(List<OrdenCompraVenta> ordenes);

}
