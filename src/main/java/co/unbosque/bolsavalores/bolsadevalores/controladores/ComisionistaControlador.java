package co.unbosque.bolsavalores.bolsadevalores.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompletaDTO;
import co.unbosque.bolsavalores.bolsadevalores.servicios.AccionServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenVentaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ReporteServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class ComisionistaControlador {

    @Autowired
    private OrdenServicio ordenServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @Autowired
    private ComisionistaServicio comisionistaServicio;

    @Autowired
    private AccionServicio accionServicio;

    @Autowired
    private OrdenVentaServicio ordenVentaServicio;

    @Autowired
    private ReporteServicio reporteServicio;

    @GetMapping("/portalComisionista")
    public String portalComisionista(){
        return "portalComisionista";
    }

    @GetMapping("/listarOrdenesCom")
    public String listarOrdenesCom(HttpSession session, Model model){

        Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");

        List<OrdenCompraVenta> ordenesConFk = ordenServicio.listarOrdenesPorComisionista(comisionista.getId());
        List<OrdenCompletaDTO> ordenesDTO = new ArrayList<>();

        for (OrdenCompraVenta orden : ordenesConFk) {
            OrdenCompletaDTO dto = new OrdenCompletaDTO();
            dto.setId(orden.getId());
            dto.setTipo(orden.getTipo());
            dto.setEstado(orden.getEstado());
            dto.setFechaCreacion(orden.getFechaCreacion());

            Empresa empresa = empresaServicio.obtenerPorId(orden.getFkEmpresa());
            dto.setNombreEmpresa(empresa != null ? empresa.getNombre() : "No hay");
            dto.setValorAccion(orden.getPrecioCompra());
            dto.setVariacionAccion(empresa.getVariacionAccion());
            
            Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
            dto.setNombreInversionista(inversionista != null ? inversionista.getNombre() : "No hay");
            dto.setSaldo(inversionista.getSaldo());

            ordenesDTO.add(dto);
        }

        List<OrdenSoloVenta> ordenesVentaConFk = ordenVentaServicio.listarOrdenesVentaPorComisionista(comisionista.getId());
        List<OrdenCompletaDTO> ordenesVentaDTO = new ArrayList<>();

        for(OrdenSoloVenta orden : ordenesVentaConFk){
            OrdenCompletaDTO dtoVenta = new OrdenCompletaDTO();
            dtoVenta.setId(orden.getId());
            dtoVenta.setTipo(orden.getTipo());
            dtoVenta.setEstado(orden.getEstado());
            dtoVenta.setFechaCreacion(orden.getFechaCreacion());
            dtoVenta.setFkAccion(orden.getFkAccion());

            Empresa empresa = empresaServicio.obtenerPorId(orden.getFkEmpresa());
            dtoVenta.setNombreEmpresa(empresa != null ? empresa.getNombre() : "No hay");
            dtoVenta.setValorAccion(orden.getPrecioVenta());
            dtoVenta.setVariacionAccion(empresa.getVariacionAccion());
            
            Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
            dtoVenta.setNombreInversionista(inversionista != null ? inversionista.getNombre() : "No hay");
            dtoVenta.setSaldo(inversionista.getSaldo());

            ordenesVentaDTO.add(dtoVenta);
        }

        model.addAttribute("comisionista", comisionista);
        model.addAttribute("ordenes", ordenesDTO);
        model.addAttribute("ordenesVenta", ordenesVentaDTO);

        return "listarOrdenesCom";
    }

    @GetMapping("/listarOrdenesCom/{idOrden}")
    public String aceptarOrden(@PathVariable Long idOrden, HttpSession session, RedirectAttributes redirectAttributes){

        try{

            OrdenCompraVenta orden = ordenServicio.obtenerPorId(idOrden);
            
            Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
            Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");
    
            if(inversionista.getSaldo() >= orden.getPrecioCompra() && orden.getEstado().equals("pendiente")){
                orden.setEstado("ejecutada");
                orden.setFechaCreacion(new Date());
                ordenServicio.guardarOrden(orden);
    
                inversionista.setSaldo(inversionista.getSaldo() - orden.getPrecioCompra());
                inversionistaServicio.guardarInversionista(inversionista);
    
                comisionista.setSaldo(comisionista.getSaldo() + (orden.getPrecioCompra() * 0.10));
                comisionistaServicio.guardarComisionista(comisionista);
    
                Accion accion = new Accion(); 
                accion.setCantAcciones(1);
                accion.setFkEmpresa(orden.getFkEmpresa());
                accion.setFkInversionista(orden.getFkComisionista());
                accionServicio.guardarAccion(accion);
    
                redirectAttributes.addFlashAttribute("mensajeError9", true);
            }else{
                redirectAttributes.addFlashAttribute("mensajeError13", true);
            }
        }catch(Exception e){
            
            return "redirect:index";
        }
        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/listarOrdenesCom/{idOrden}/{idAccion}")
    public String aceptarOrdenVenta(@PathVariable Long idOrden, @PathVariable Long idAccion, HttpSession session, RedirectAttributes redirectAttributes){
        
        try{
            OrdenSoloVenta ordenVenta = ordenVentaServicio.obtenerPorId(idOrden);
    
            Inversionista inversionista = inversionistaServicio.obtenerPorId(ordenVenta.getFkInversionista());
            Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");
    
            ordenVenta.setEstado("ejecutada");
            ordenVenta.setFechaCreacion(new Date());
            ordenVentaServicio.guardarOrdenVenta(ordenVenta);
    
            inversionista.setSaldo(inversionista.getSaldo() + ordenVenta.getPrecioVenta());
            inversionistaServicio.guardarInversionista(inversionista);
    
            comisionista.setSaldo(comisionista.getSaldo() + (ordenVenta.getPrecioVenta() * 0.10));
            comisionistaServicio.guardarComisionista(comisionista);
    
            Accion accion = accionServicio.obtenerPorId(idAccion);
            accion.setFkInversionista(null);
            accionServicio.guardarAccion(accion);
    
            redirectAttributes.addFlashAttribute("mensajeError10", true);

        }catch(Exception e){
            return "redirect:/index";
        }

        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/rechazarOrdenCompra/{idOrden}")
    public String rechazarOrdenCompra(@PathVariable Long idOrden, RedirectAttributes redirectAttributes){

        try{
            OrdenCompraVenta ordenCompra = ordenServicio.obtenerPorId(idOrden);
            ordenCompra.setEstado("rechazada");
            ordenServicio.guardarOrden(ordenCompra);
            redirectAttributes.addFlashAttribute("mensajeError11", true);

        }catch(Exception e){
            return "redirect:/index";
        }
        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/rechazarOrdenVenta/{idOrden}")
    public String rechazarOrdenVenta(@PathVariable Long idOrden, RedirectAttributes redirectAttributes){

        try{
            OrdenSoloVenta ordenVenta = ordenVentaServicio.obtenerPorId(idOrden);
            ordenVenta.setEstado("rechazada");
            ordenVentaServicio.guardarOrdenVenta(ordenVenta);
            redirectAttributes.addFlashAttribute("mensajeError12", true);

        }catch(Exception e){
            return "redirect:/index";
        }
        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/descargarReporteCom")
    public ResponseEntity<byte[]> descargarReporteCom(HttpSession session){

        Comisionista comisionista = (Comisionista) session.getAttribute("comisionista");
        
        List<OrdenCompraVenta> ordenesCompra = ordenServicio.listarOrdenesPorComisionista(comisionista.getId());
        List<OrdenSoloVenta> ordenVenta = ordenVentaServicio.listarOrdenesVentaPorComisionista(comisionista.getId());

        byte[] reportePDF = reporteServicio.generarReportePDF(ordenesCompra, ordenVenta, null, comisionista);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDispositionFormData("attachment", "ReporteComisionista.pdf"); 

        return new ResponseEntity<>(reportePDF, headers, HttpStatus.OK);
    }

}
