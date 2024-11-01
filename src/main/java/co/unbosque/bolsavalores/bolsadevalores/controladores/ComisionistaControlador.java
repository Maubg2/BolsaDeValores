package co.unbosque.bolsavalores.bolsadevalores.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;
import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompletaDTO;
import co.unbosque.bolsavalores.bolsadevalores.entidades.dto.OrdenCompraVentaDTO;
import co.unbosque.bolsavalores.bolsadevalores.servicios.AccionServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.ComisionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.EmpresaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.InversionistaServicio;
import co.unbosque.bolsavalores.bolsadevalores.servicios.OrdenServicio;
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
        model.addAttribute("ordenes", ordenesDTO);

        return "listarOrdenesCom";
    }

    @GetMapping("/listarOrdenesCom/{idOrden}")
    public String aceptarOrden(@PathVariable Long idOrden, HttpSession session){

        OrdenCompraVenta orden = ordenServicio.obtenerPorId(idOrden);
        
        Empresa empresa = empresaServicio.obtenerPorId(orden.getFkEmpresa());
        Inversionista inversionista = inversionistaServicio.obtenerPorId(orden.getFkInversionista());
        Comisionista comisionista = (Comisionista)session.getAttribute("comisionista");

        if(inversionista.getSaldo() >= empresa.getValorAccion() && orden.getEstado().equals("pendiente")){
            System.out.println("ENTROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            orden.setEstado("ejecutada");
            orden.setFechaCreacion(new Date());
            ordenServicio.guardarOrden(orden);

            inversionista.setSaldo(inversionista.getSaldo() - empresa.getValorAccion());
            inversionistaServicio.guardarInversionista(inversionista);

            comisionista.setSaldo(empresa.getValorAccion() * 0.10);
            comisionistaServicio.guardarComisionista(comisionista);

            Accion accion = new Accion();
            accion.setCantAcciones(1);
            accion.setFkEmpresa(orden.getFkEmpresa());
            accion.setFkInversionista(orden.getFkComisionista());
            accionServicio.guardarAccion(accion);
            
        }

        return "redirect:/portalComisionista";
    }

}
