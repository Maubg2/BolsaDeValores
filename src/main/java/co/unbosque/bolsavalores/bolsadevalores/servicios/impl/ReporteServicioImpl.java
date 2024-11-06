package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ReporteServicio;

@Service
public class ReporteServicioImpl implements ReporteServicio{

    @Autowired
    private EmpresaServicio empresaServicio;

    @Override
    public byte[] generarReportePDF(List<OrdenCompraVenta> ordenes, List<OrdenSoloVenta> ordenesVenta, Inversionista inversionista){

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Movimientos de Inversionista"));
            document.add(new Paragraph("Fecha: "+ new Date()));
            document.add(new Paragraph("Ordenes de compra de: "+ inversionista.getNombre()));
            document.add(new Paragraph("Saldo: "+inversionista.getSaldo()));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Estado");
            table.addCell("Fecha");
            table.addCell("Empresa");
            table.addCell("Valor acción");

            for(OrdenCompraVenta x : ordenes){

                Empresa empresa = empresaServicio.obtenerPorId(x.getFkEmpresa());

                table.addCell(x.getEstado());
                table.addCell(x.getFechaCreacion().toString());
                table.addCell(empresa.getNombre());
                table.addCell(empresa.getValorAccion().toString());

            }   
            document.add(table);

            document.add(new Paragraph("Ordenes de venta de: " + inversionista.getNombre()));

            PdfPTable table2 = new PdfPTable(4);
            table2.addCell("Estado");
            table2.addCell("Fecha");
            table2.addCell("Empresa");
            table2.addCell("Valor acción");

            for(OrdenSoloVenta x : ordenesVenta){

                Empresa empresaVenta = empresaServicio.obtenerPorId(x.getFkEmpresa());

                table2.addCell(x.getEstado());
                table2.addCell(x.getFechaCreacion().toString());
                table2.addCell(empresaVenta.getNombre());
                table2.addCell(empresaVenta.getValorAccion().toString());

            }
            document.add(table2);

            document.close();

            return out.toByteArray();

        }catch(Exception e){
            throw new RuntimeException("Error al generar reporte PDF", e);
        }
    }

}
