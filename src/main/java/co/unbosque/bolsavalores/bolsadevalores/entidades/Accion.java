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
@Table(name = "accion")
@Getter
@Setter
public class Accion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cant_acciones")
    private int cantAcciones;

    @Column(name = "fk_inversionista")
    private Long fkInversionista;

    @Column(name = "fk_empresa")
    private Long fkEmpresa;

    public Accion(Long id, int cantAcciones, Long fkInversionista, Long fkEmpresa) {
        this.id = id;
        this.cantAcciones = cantAcciones;
        this.fkInversionista = fkInversionista;
        this.fkEmpresa = fkEmpresa;
    }

    public Accion() {
    }

}
