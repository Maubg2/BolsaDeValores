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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.EmpresaConAccionDTO;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;
import co.unbosque.bolsavalores.bolsadevalores.servicios.AccionServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenVentaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ReporteServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class InversionistaControlador {

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private OrdenServicio ordenServicio;

    @Autowired
    private ComisionistaServicio comisionistaServicio;

    @Autowired
    private AccionServicio accionServicio;

    @Autowired
    private OrdenVentaServicio ordenVentaServicio;

    @Autowired
    private ReporteServicio reporteServicio;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("inversionista", new Inversionista());
        model.addAttribute("comisionista", new Comisionista());
        return "login";
    }

    @PostMapping("/login")
    public String validarCredenciales(@ModelAttribute("inversionista") Inversionista inversionista,
            @ModelAttribute("comisionista") Comisionista comisionista, Model model, HttpSession session) {

        Inversionista inversionistaBD = inversionistaServicio.obtenerPorEmail(inversionista.getEmail());
        Comisionista comisionistaBD = comisionistaServicio.obtenerPorEmail(comisionista.getEmail());

        if (inversionistaServicio.validarCredenciales(inversionista.getEmail(), inversionista.getContrasena())) {
            session.setAttribute("inversionista", inversionistaBD);
            return "redirect:/listarEmpresas";
        } else if (comisionistaServicio.validarCredenciales(comisionista.getEmail(), comisionista.getContrasena())) {
            session.setAttribute("comisionista", comisionistaBD);
            return "redirect:/portalComisionista";
        } else {
            model.addAttribute("mensajeError1", true);
            return "login";
        }
    }

    @GetMapping("/registrar")
    public String registrar(Model model) {
        model.addAttribute("inversionista", new Inversionista());
        return "registrar";
    }

    @PostMapping("/index")
    public String index(@ModelAttribute("inversionista") Inversionista inversionista, Model model) {
        try {
            Inversionista comprobarInversionista = inversionistaServicio.obtenerPorEmail(inversionista.getEmail());
            if (comprobarInversionista == null) {

                inversionista.setSaldo(10000.0);
                inversionistaServicio.guardarInversionista(inversionista);
                return "redirect:/login";
            } else {
                model.addAttribute("mensajeError2", true);
                return "registrar";
            }
        } catch (Exception e) {
            model.addAttribute("mensajeError3", true);
            return "registrar";
        }
    }

    @GetMapping("/listarEmpresas")
    public String listarEmpresas(Model model, HttpSession session) {
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        Inversionista inversionista = (Inversionista) session.getAttribute("inversionista");

        List<Accion> accionesPorInversionista = accionServicio.listarAccionesPorInversionista(inversionista.getId());
        List<EmpresaConAccionDTO> empresasConAccion = new ArrayList<>();

        for (Accion x : accionesPorInversionista) {

            boolean existeOrdenPendiente = ordenVentaServicio
                    .existeOrdenPendientePorEmpresaEInversionista(x.getFkEmpresa(), inversionista.getId());

            if (!existeOrdenPendiente) {
                EmpresaConAccionDTO empresaConAccionDTO = new EmpresaConAccionDTO();
                Empresa empresa = empresaServicio.obtenerPorId(x.getFkEmpresa());
                empresaConAccionDTO.setId(empresa.getId());
                empresaConAccionDTO.setNombre(empresa.getNombre());
                empresaConAccionDTO.setSector(empresa.getSector());
                empresaConAccionDTO.setPais(empresa.getPais());
                empresaConAccionDTO.setValorAccion(empresa.getValorAccion());
                empresaConAccionDTO.setVariacionAccion(empresa.getVariacionAccion());
                empresaConAccionDTO.setFkAccion(x.getId());

                empresasConAccion.add(empresaConAccionDTO);
            }
        }

        model.addAttribute("empresas", empresas);
        model.addAttribute("inversionista", inversionista);
        model.addAttribute("idInversionista", inversionista.getId());
        model.addAttribute("empresasConAccion", empresasConAccion);
        return "listarEmpresas";
    }

    @GetMapping("/listarEmpresas/{idEmpresa}/{idInversionista}")
    public String crearOrden(@PathVariable Long idEmpresa, @PathVariable Long idInversionista,
            RedirectAttributes redirectAttributes, Model model) {

        Inversionista inversionista = inversionistaServicio.obtenerPorId(idInversionista);
        Empresa empresa = empresaServicio.obtenerPorId(idEmpresa);

        if (inversionista.getSaldo() >= empresa.getValorAccion()) {
            OrdenCompraVenta orden = new OrdenCompraVenta();
            orden.setTipo("compra");
            orden.setEstado("pendiente");
            orden.setFechaCreacion(new Date());
            orden.setFkEmpresa(idEmpresa);
            orden.setFkInversionista(idInversionista);
            orden.setFkComisionista(1L);

            ordenServicio.guardarOrden(orden);

            redirectAttributes.addFlashAttribute("mensajeError4", true);

            return "redirect:/listarEmpresas";
        } else {
            redirectAttributes.addFlashAttribute("mensajeError5", true);
            return "redirect:/listarEmpresas";
        }

    }

    @GetMapping("/listarEmpresas/{idEmpresa}/{idInversionista}/{idAccion}")
    public String crearOrdenVenta(@PathVariable Long idEmpresa, @PathVariable Long idInversionista,
            @PathVariable Long idAccion, RedirectAttributes redirectAttributes) {

        OrdenSoloVenta ordenVenta = new OrdenSoloVenta();
        ordenVenta.setTipo("venta");
        ordenVenta.setEstado("pendiente");
        ordenVenta.setFechaCreacion(new Date());
        ordenVenta.setFkEmpresa(idEmpresa);
        ordenVenta.setFkInversionista(idInversionista);
        ordenVenta.setFkComisionista(1L);
        ordenVenta.setFkAccion(idAccion);

        ordenVentaServicio.guardarOrdenVenta(ordenVenta);

        redirectAttributes.addFlashAttribute("mensajeError6", true);

        return "redirect:/listarEmpresas";
    }

    @GetMapping("/listarOrdenes")
    public String listarOrdenes(Model model, HttpSession session) {
        Inversionista inversionista = (Inversionista) session.getAttribute("inversionista");
        List<OrdenCompraVentaDTO> ordenesDTO = ordenServicio.listarOrdenesConNombres(inversionista.getId());
        List<OrdenCompraVentaDTO> ordenesVentaDTO = ordenVentaServicio
                .listarOrdenesVentaConNombres(inversionista.getId());

        model.addAttribute("ordenes", ordenesDTO);
        model.addAttribute("ordenesVenta", ordenesVentaDTO);
        return "listarOrdenes";
    }

    @GetMapping("/cancelarOrden/{ordenId}/{tipoOrden}")
    public String cancelarOrden(@PathVariable Long ordenId, @PathVariable int tipoOrden,
            RedirectAttributes redirectAttributes) {

        if (tipoOrden == 1) {
            ordenServicio.cancelarOrden(ordenId);
            redirectAttributes.addFlashAttribute("mensajeError7", true);
        } else {
            ordenVentaServicio.cancelarOrdenVenta(ordenId);
            redirectAttributes.addFlashAttribute("mensajeError8", true);
        }
        return "redirect:/listarOrdenes";
    }

    @GetMapping("/generarReportes")
    public String generarReportes() {
        return "generarReportes";
    }
/*
    @GetMapping("/crearReporte")
    public String enviarReporte(HttpSession session) {
        // Obtener datos del inversionista autenticado
        Inversionista inversionista = (Inversionista) session.getAttribute("inversionista");

        String email = inversionista.getEmail();
        List<OrdenCompraVenta> ordenes = ordenServicio.listarOrdenesPorInversionista(inversionista.getId());// obtenerMovimientosPorInversionista(inversionista.getId());
        System.out.println("1111111111111111111111111111111111111111");
        // Generar reporte PDF
        byte[] reportePDF = reporteServicio.generarReportePDF(ordenes);
        System.out.println("22222222222222222222222222222222222222222");

        // Enviar el reporte por correo electrónico
        emailServicio.enviarCorreoConReporte(email, reportePDF, "Reporte de Movimientos - Andina Trading");
        System.out.println("33333333333333333333333333333333333333");

        return "redirect:/generarReportes";
    }
 */
    @GetMapping("/descargarReporte")
    public ResponseEntity<byte[]> descargarReporte(HttpSession session) {
        // Obtener datos del inversionista autenticado
        Inversionista inversionista = (Inversionista) session.getAttribute("inversionista");

       

        List<OrdenCompraVenta> ordenes = ordenServicio.listarOrdenesPorInversionista(inversionista.getId());

        // Generar reporte PDF
        byte[] reportePDF = reporteServicio.generarReportePDF(ordenes);

        // Configurar encabezados para que el navegador interprete el contenido como un
        // archivo descargable
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
  /*      headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("reporte_inversionista_" + inversionista.getId() + ".pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(reportePDF); */

        headers.setContentDispositionFormData("attachment", "reporte.pdf"); // Especificar nombre del archivo

        return new ResponseEntity<>(reportePDF, headers, HttpStatus.OK);
    }

}
