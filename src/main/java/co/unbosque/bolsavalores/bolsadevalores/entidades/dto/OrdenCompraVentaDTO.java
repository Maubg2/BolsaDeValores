package co.unbosque.bolsavalores.bolsadevalores.entidades.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenCompraVentaDTO {

    private String tipo;
    private String estado;
    private Date fechaCreacion;
    private String nombreEmpresa;
    private String nombreInversionista;
    private String nombreComisionista;

    public OrdenCompraVentaDTO(String tipo, String estado, Date fechaCreacion,
                               String nombreEmpresa, String nombreInversionista, String nombreComisionista) {
        this.tipo = tipo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreInversionista = nombreInversionista;
        this.nombreComisionista = nombreComisionista;
    }

}
