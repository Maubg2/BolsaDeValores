package co.unbosque.bolsavalores.bolsadevalores.servicios;

import java.util.List;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;

public interface ReporteServicio {

    public byte[] generarReportePDF(List<OrdenCompraVenta> ordenes, List<OrdenSoloVenta> ordenesVenta, Inversionista inversionista, Comisionista comisionista);

    public byte[] generarReportePDFEmpresa(List<OrdenCompraVenta> ordenes, List<OrdenSoloVenta> ordenesVenta);

}
