package co.unbosque.bolsavalores.bolsadevalores.entidades.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaConAccionDTO {

    private Long id;
    private String nombre;
    private String sector;
    private String pais;
    private Double valorAccion;
    private Double variacionAccion;
    private Long fkAccion;
    
    public EmpresaConAccionDTO(Long id, String nombre, String sector, String pais, Double valorAccion,
            Double variacionAccion, Long fkAccion) {
        this.id = id;
        this.nombre = nombre;
        this.sector = sector;
        this.pais = pais;
        this.valorAccion = valorAccion;
        this.variacionAccion = variacionAccion;
        this.fkAccion = fkAccion;
    }

    public EmpresaConAccionDTO() {
    }

}
