package co.unbosque.bolsavalores.bolsadevalores.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;

@Controller
public class InversionistaControlador {

    @Autowired
    private InversionistaServicio inversionistaServicio;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("inversionista", new Inversionista());
        return "login";
    }

    @PostMapping("/login")
    public String validarCredenciales(@ModelAttribute("inversionista") Inversionista inversionista, Model model){
        Inversionista inversionistaBD = inversionistaServicio.obtenerPorEmail(inversionista.getEmail());
        if(inversionistaServicio.validarCredenciales(inversionista.getEmail(), inversionista.getContrasena())){

            return "redirect:/portalInversionista";
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

}
