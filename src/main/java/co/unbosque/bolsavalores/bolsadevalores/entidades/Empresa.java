package co.unbosque.bolsavalores.bolsadevalores.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String sector;

    private String pais;

    @Column(name = "valor_accion")
    private Double valorAccion;

    @Column(name = "variacion_accion")
    private Double variacionAccion;

    public Empresa(Long id, String nombre, String sector, String pais, Double valorAccion, Double variacionAccion) {
        this.id = id;
        this.nombre = nombre;
        this.sector = sector;
        this.pais = pais;
        this.valorAccion = valorAccion;
        this.variacionAccion = variacionAccion;
    }

    public Empresa() {
    }

    

}
