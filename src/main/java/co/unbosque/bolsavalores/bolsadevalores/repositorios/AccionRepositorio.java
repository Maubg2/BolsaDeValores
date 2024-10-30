package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;

@Repository
public interface AccionRepositorio extends JpaRepository<Accion, Long>{

}
