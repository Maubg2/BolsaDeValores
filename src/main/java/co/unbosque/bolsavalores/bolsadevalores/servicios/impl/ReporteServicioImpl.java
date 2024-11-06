package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ReporteServicio;

@Service
public class ReporteServicioImpl implements ReporteServicio{

    @Override
    public byte[] generarReportePDF(List<OrdenCompraVenta> ordenes){
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Movimientos de Inversionista"));
            document.add(new Paragraph("Fecha: "+ new Date()));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Tipo");
            table.addCell("Estado");
            table.addCell("Fecha");

            for(OrdenCompraVenta x : ordenes){
                table.addCell(x.getTipo());
                table.addCell(x.getEstado());
                table.addCell(x.getFechaCreacion().toString());
                System.out.println("SIRVE?: "+x.getFechaCreacion());
            }   
            document.add(table);
            document.close();

            return out.toByteArray();

        }catch(Exception e){
            throw new RuntimeException("Error al generar reporte PDF", e);
        }
    }

}
