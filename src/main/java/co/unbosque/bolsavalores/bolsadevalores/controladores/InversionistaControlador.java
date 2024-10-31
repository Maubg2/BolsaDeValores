package co.unbosque.bolsavalores.bolsadevalores.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;
import co.unbosque.bolsavalores.bolsadevalores.servicios.AccionServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenServicio;
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

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("inversionista", new Inversionista());
        model.addAttribute("comisionista", new Comisionista());
        return "login";
    }

    @PostMapping("/login")
    public String validarCredenciales(@ModelAttribute("inversionista") Inversionista inversionista, 
                @ModelAttribute("comisionista") Comisionista comisionista, Model model, HttpSession session){
        
        Inversionista inversionistaBD = inversionistaServicio.obtenerPorEmail(inversionista.getEmail());
        Comisionista comisionistaBD = comisionistaServicio.obtenerPorEmail(comisionista.getEmail());
        if(inversionistaServicio.validarCredenciales(inversionista.getEmail(), inversionista.getContrasena())){
            session.setAttribute("inversionista", inversionistaBD);
            return "redirect:/listarEmpresas";
        }else if(comisionistaServicio.validarCredenciales(comisionista.getEmail(), comisionista.getContrasena())){
            session.setAttribute("comisionista", comisionistaBD);
            return "redirect:/portalComisionista";
        }else{
            model.addAttribute("mensajeError1", "Usuario y/o contraseña incorrectos");
            return "/index";
        }
    }

    @GetMapping("/registrar")
    public String registrar(Model model){
        model.addAttribute("inversionista", new Inversionista());
        return "registrar";
    }

    @PostMapping("/index")
    public String index(@ModelAttribute("inversionista") Inversionista inversionista, Model model){
        try{
            Inversionista comprobarInversionista = inversionistaServicio.obtenerPorEmail(inversionista.getEmail());
            if(comprobarInversionista == null){
       
            //    usuario.setFkRol(1L);
                inversionista.setSaldo(10000.0);
                inversionistaServicio.guardarInversionista(inversionista);
                return "redirect:/login";
            }else{
                model.addAttribute("mensajeError", "Usuario ya existe");
                return "registrar";
            }
        }catch(Exception e){
            model.addAttribute("error", "Error, intente de nuevo");
            return "registrar";
        }
    }

    @GetMapping("/listarEmpresas")
    public String listarEmpresas(Model model, HttpSession session){
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        Inversionista inversionista = (Inversionista)session.getAttribute("inversionista");

        List<Accion> accionesPorInversionista = accionServicio.listarAccionesPorInversionista(inversionista.getId());
        List<Empresa> empresasParaVender = new ArrayList<>();
        for(Accion x : accionesPorInversionista){
            empresasParaVender.add(empresaServicio.obtenerPorId(x.getFkEmpresa()));
        }

        model.addAttribute("empresas", empresas);
        model.addAttribute("inversionista", inversionista);
        model.addAttribute("idInversionista", inversionista.getId());
        model.addAttribute("empresasVender", empresasParaVender);
        return "listarEmpresas";
    }

    @GetMapping("/listarEmpresas/{idEmpresa}/{idInversionista}")
    public String crearOrden(@PathVariable Long idEmpresa, @PathVariable Long idInversionista, RedirectAttributes redirectAttributes){
        
        Inversionista inversionista = inversionistaServicio.obtenerPorId(idInversionista);
        Empresa empresa = empresaServicio.obtenerPorId(idEmpresa);

        if(inversionista.getSaldo() >= empresa.getValorAccion()){
            OrdenCompraVenta orden = new OrdenCompraVenta();
            orden.setTipo("compra");
            orden.setEstado("pendiente");
            orden.setFechaCreacion(new Date());
            orden.setFkEmpresa(idEmpresa);
            orden.setFkInversionista(idInversionista);
            orden.setFkComisionista(1L);

       //     inversionista.setSaldo(inversionista.getSaldo() - empresa.getValorAccion());

       //     inversionistaServicio.guardarInversionista(inversionista);
            ordenServicio.guardarOrden(orden);

        //    redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        //    redirectAttributes.addFlashAttribute("mensaje", "Orden creada con éxito");
            
            return "redirect:/listarEmpresas";
        }else{
        //    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        //    redirectAttributes.addFlashAttribute("mensaje", "No cuenta con suficiente saldo");
            return "redirect:/listarEmpresas";
        }
       
    }

    @GetMapping("/listarEmpresas")
    public String crearOrdenVenta(){
        return "";
    }

    @GetMapping("/listarOrdenes")
    public String listarOrdenes(Model model, HttpSession session) {
        Inversionista inversionista = (Inversionista) session.getAttribute("inversionista");
        List<OrdenCompraVentaDTO> ordenesDTO = ordenServicio.listarOrdenesConNombres(inversionista.getId());
        model.addAttribute("ordenes", ordenesDTO);
        return "listarOrdenes";
    }

    @GetMapping("/cancelarOrden/{ordenId}")
    public String cancelarOrden(@PathVariable Long ordenId){
        ordenServicio.cancelarOrden(ordenId);
        return "redirect:/listarOrdenes";
    }


}
