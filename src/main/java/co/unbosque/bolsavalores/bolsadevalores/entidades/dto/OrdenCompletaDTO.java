package co.unbosque.bolsavalores.bolsadevalores.entidades.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenCompletaDTO {

    private Long id;
    private String tipo;
    private String estado;
    private Date fechaCreacion;
    private String nombreEmpresa;
    private Double valorAccion;
    private Double variacionAccion;
    private String nombreInversionista;
    private Double saldo;
    
    public OrdenCompletaDTO(Long id, String tipo, String estado, Date fechaCreacion, String nombreEmpresa,
            Double valorAccion, Double variacionAccion, String nombreInversionista, Double saldo) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.nombreEmpresa = nombreEmpresa;
        this.valorAccion = valorAccion;
        this.variacionAccion = variacionAccion;
        this.nombreInversionista = nombreInversionista;
        this.saldo = saldo;
    }
    public OrdenCompletaDTO() {
    }

    

}
