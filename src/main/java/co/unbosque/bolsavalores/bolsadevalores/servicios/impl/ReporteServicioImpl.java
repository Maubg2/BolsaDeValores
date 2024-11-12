package co.unbosque.bolsavalores.bolsadevalores.servicios.impl;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ReporteServicio;

@Service
public class ReporteServicioImpl implements ReporteServicio{

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @Autowired
    private ComisionistaServicio comisionistaServicio;

    @Override
    public byte[] generarReportePDF(List<OrdenCompraVenta> ordenes, List<OrdenSoloVenta> ordenesVenta, Inversionista inversionista, Comisionista comisionista){
        Double cont = 0.0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph titulo = new Paragraph("Andina Trading", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)); 
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            if(inversionista != null){

                document.add(new Paragraph("Reporte de Movimientos de Inversionista"));
                document.add(new Paragraph("Fecha: "+ new Date()));
                document.add(new Paragraph("Ordenes de compra de: "+ inversionista.getNombre()));
                document.add(new Paragraph("Saldo: " + inversionista.getSaldo()));
            }else{

                document.add(new Paragraph("Reporte de Movimientos de Comisionista"));
                document.add(new Paragraph("Fecha: "+ new Date()));
                document.add(new Paragraph("Ordenes de compra realizadas por: "+ comisionista.getNombre()));
                document.add(new Paragraph("Saldo: " + comisionista.getSaldo()));

            }

            document.add(new Paragraph(" "));

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
                table.addCell(x.getPrecioCompra().toString());

                cont += (x.getEstado().equals("ejecutada") ? x.getPrecioCompra() : 0);

            }   
            document.add(table);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Monto total de órdenes de compra ejecutadas: " + cont));
            cont = 0.0;

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Ordenes de venta " + (inversionista != null ? 
                                                                    "de: " + inversionista.getNombre() :
                                                                    "realizadas por: " + comisionista.getNombre())));
            document.add(new Paragraph(" "));

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
                table2.addCell(x.getPrecioVenta().toString());

                cont += (x.getEstado().equals("ejecutada") ? x.getPrecioVenta() : 0);

            }
            document.add(table2);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Monto total de órdenes de venta ejecutadas: " + cont));

            document.close();

            return out.toByteArray();

        }catch(Exception e){
            throw new RuntimeException("Error al generar reporte PDF", e);
        }
    }

    @Override
    public byte[] generarReportePDFEmpresa(List<OrdenCompraVenta> ordenes, List<OrdenSoloVenta> ordenesVenta) {
   
        Double cont = 0.0;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph titulo = new Paragraph("Andina Trading", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)); 
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            document.add(new Paragraph("Reporte de Movimientos"));
            document.add(new Paragraph("Fecha: "+ new Date()));
            document.add(new Paragraph("Ordenes de compra de todas las empresas"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.addCell("Estado");
            table.addCell("Nombre");
            table.addCell("Fecha");
            table.addCell("Valor acción");
            table.addCell("Inversionista");
            table.addCell("Comisionista");

            for(OrdenCompraVenta x : ordenes){

                Empresa empresaReporte = empresaServicio.obtenerPorId(x.getFkEmpresa());
                Inversionista inversionista = inversionistaServicio.obtenerPorId(x.getFkInversionista());
                Comisionista comisionista = comisionistaServicio.obtenerPorId(x.getFkComisionista());
   
                table.addCell(x.getEstado());
                table.addCell(empresaReporte.getNombre());
                table.addCell(x.getFechaCreacion().toString());
                table.addCell(x.getPrecioCompra().toString());
                table.addCell(inversionista.getNombre());
                table.addCell(comisionista.getNombre());
            
                cont += (x.getEstado().equals("ejecutada") ? x.getPrecioCompra() : 0);

            }   

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Monto total de órdenes de compra ejecutadas: " + cont));
            cont = 0.0;

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Ordenes de venta para todas las empresas"));
            document.add(new Paragraph(" "));

            PdfPTable table2 = new PdfPTable(6);
            table2.addCell("Estado");
            table.addCell("Nombre");
            table2.addCell("Fecha");
            table2.addCell("Valor acción");
            table2.addCell("Inversionista");
            table2.addCell("Comisionista");

            for(OrdenSoloVenta x : ordenesVenta){

                Empresa empresaReporte = empresaServicio.obtenerPorId(x.getFkEmpresa());
                Inversionista inversionista = inversionistaServicio.obtenerPorId(x.getFkInversionista());
                Comisionista comisionista = comisionistaServicio.obtenerPorId(x.getFkComisionista());
                    
                table2.addCell(x.getEstado());
                table2.addCell(empresaReporte.getNombre());
                table2.addCell(x.getFechaCreacion().toString());
                table2.addCell(x.getPrecioVenta().toString());
                table2.addCell(inversionista.getNombre());
                table2.addCell(comisionista.getNombre());
            
                cont += (x.getEstado().equals("ejecutada") ? x.getPrecioVenta() : 0);
            

            }
            document.add(table2);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Monto total de órdenes de venta ejecutadas: " + cont));

            document.close();

            return out.toByteArray();     

        }catch(Exception e){
            throw new RuntimeException("Error al generar reporte PDF", e);
        }
    }

}
