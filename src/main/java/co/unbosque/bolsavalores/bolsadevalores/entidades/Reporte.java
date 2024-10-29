package co.unbosque.bolsavalores.bolsadevalores.entidades;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orden_compra_venta")
@Getter
@Setter
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;

    @Column(name = "fecha_generacion")
    private Date fechaGeneracion;

    @Column(name = "fk_inversionista")
    private Long fkInversionista;

    public Reporte(Long id, String contenido, Date fechaGeneracion, Long fkInversionista) {
        this.id = id;
        this.contenido = contenido;
        this.fechaGeneracion = fechaGeneracion;
        this.fkInversionista = fkInversionista;
    }

    public Reporte() {
    }
    
}
