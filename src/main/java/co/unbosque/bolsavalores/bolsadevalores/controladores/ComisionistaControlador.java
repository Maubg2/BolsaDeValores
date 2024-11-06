package co.unbosque.bolsavalores.bolsadevalores.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
            Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
            dto.setNombreEmpresa(empresa != null ? empresa.getNombre() : "No hay");
            dto.setValorAccion(empresa.getValorAccion());
            dto.setVariacionAccion(empresa.getVariacionAccion());

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
            Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
            dtoVenta.setNombreEmpresa(empresa != null ? empresa.getNombre() : "No hay");
            dtoVenta.setValorAccion(empresa.getValorAccion());
            dtoVenta.setVariacionAccion(empresa.getVariacionAccion());

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

        OrdenCompraVenta orden = ordenServicio.obtenerPorId(idOrden);
        
        Empresa empresa = empresaServicio.obtenerPorId(orden.getFkEmpresa());
        Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
        Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");

        if(inversionista.getSaldo() >= empresa.getValorAccion() && orden.getEstado().equals("pendiente")){
            orden.setEstado("ejecutada");
            orden.setFechaCreacion(new Date());
            ordenServicio.guardarOrden(orden);

            inversionista.setSaldo(inversionista.getSaldo() - empresa.getValorAccion());
            inversionistaServicio.guardarInversionista(inversionista);

            comisionista.setSaldo(comisionista.getSaldo() + empresa.getValorAccion() * 0.10);
            comisionistaServicio.guardarComisionista(comisionista);

            Accion accion = new Accion();
            accion.setCantAcciones(1);
            accion.setFkEmpresa(orden.getFkEmpresa());
            accion.setFkInversionista(orden.getFkComisionista());
            accionServicio.guardarAccion(accion);

            redirectAttributes.addFlashAttribute("mensajeError9", true);
        }

        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/listarOrdenesCom/{idOrden}/{idAccion}")
    public String aceptarOrdenVenta(@PathVariable Long idOrden, @PathVariable Long idAccion, HttpSession session, RedirectAttributes redirectAttributes){
        
        OrdenSoloVenta ordenVenta = ordenVentaServicio.obtenerPorId(idOrden);

        Empresa empresa = empresaServicio.obtenerPorId(ordenVenta.getFkEmpresa());
        Inversionista inversionista = inversionistaServicio.obtenerPorId(ordenVenta.getFkInversionista());
        Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");

        ordenVenta.setEstado("ejecutada");
        ordenVenta.setFechaCreacion(new Date());
        ordenVentaServicio.guardarOrdenVenta(ordenVenta);

        inversionista.setSaldo(inversionista.getSaldo() + empresa.getValorAccion());
        inversionistaServicio.guardarInversionista(inversionista);

        comisionista.setSaldo(comisionista.getSaldo() + empresa.getValorAccion() * 0.10);
        comisionistaServicio.guardarComisionista(comisionista);

        Accion accion = accionServicio.obtenerPorId(idAccion);
        accion.setFkInversionista(null);
        accionServicio.guardarAccion(accion);

        redirectAttributes.addFlashAttribute("mensajeError10", true);

        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/rechazarOrdenCompra/{idOrden}")
    public String rechazarOrdenCompra(@PathVariable Long idOrden, RedirectAttributes redirectAttributes){

        OrdenCompraVenta ordenCompra = ordenServicio.obtenerPorId(idOrden);
        ordenCompra.setEstado("rechazada");
        ordenServicio.guardarOrden(ordenCompra);
        redirectAttributes.addFlashAttribute("mensajeError11", true);
        return "redirect:/listarOrdenesCom";
    }

    @GetMapping("/rechazarOrdenVenta/{idOrden}")
    public String rechazarOrdenVenta(@PathVariable Long idOrden, RedirectAttributes redirectAttributes){

        OrdenSoloVenta ordenVenta = ordenVentaServicio.obtenerPorId(idOrden);
        ordenVenta.setEstado("rechazada");
        ordenVentaServicio.guardarOrdenVenta(ordenVenta);
        redirectAttributes.addFlashAttribute("mensajeError12", true);
        return "redirect:/listarOrdenesCom";
    }

}
