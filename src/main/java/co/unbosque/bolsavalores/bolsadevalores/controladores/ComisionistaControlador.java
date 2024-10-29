package co.unbosque.bolsavalores.bolsadevalores.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ComisionistaControlador {

    @GetMapping("/portalComisionista")
    public String portalComisionista(){
        return "portalComisionista";
    }

}
