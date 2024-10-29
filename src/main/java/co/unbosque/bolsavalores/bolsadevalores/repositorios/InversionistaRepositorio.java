package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Inversionista;

@Repository
public interface InversionistaRepositorio extends JpaRepository<Inversionista, Long>{

    public Optional<Inversionista> findByEmail(String email);

}
