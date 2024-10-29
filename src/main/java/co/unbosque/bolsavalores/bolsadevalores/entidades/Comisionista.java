package co.unbosque.bolsavalores.bolsadevalores.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comisionista")
@Getter
@Setter
public class Comisionista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;

    private String contrasena;

    private Double saldo;

    public Comisionista(Long id, String nombre, String email, String contrasena, Double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.saldo = saldo;
    }

    public Comisionista() {
    }

    

}
