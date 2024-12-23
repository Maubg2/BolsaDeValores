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
public class OrdenCompraVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private String estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "precio_compra")
    private Double precioCompra;

    @Column(name = "fk_empresa")
    private Long fkEmpresa;

    @Column(name = "fk_inversionista")
    private Long fkInversionista;

    @Column(name = "fk_comisionista")
    private Long fkComisionista;

    public OrdenCompraVenta(Long id, String tipo, String estado, Date fechaCreacion, Double precioCompra, Long fkEmpresa,
            Long fkInversionista, Long fkComisionista) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.precioCompra = precioCompra;
        this.fkEmpresa = fkEmpresa;
        this.fkInversionista = fkInversionista;
        this.fkComisionista = fkComisionista;
    }

    public OrdenCompraVenta() {
    }

    

}
