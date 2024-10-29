package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Comisionista;

@Repository
public interface ComisionistaRepositorio extends JpaRepository<Comisionista, Long>{

    public Optional<Comisionista> findByEmail(String email);

}
