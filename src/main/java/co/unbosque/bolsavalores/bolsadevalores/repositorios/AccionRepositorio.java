package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;

@Repository
public interface AccionRepositorio extends JpaRepository<Accion, Long>{

    @Query("SELECT a FROM Accion a WHERE a.fkInversionista=?1")
    public List<Accion> listarAccionPorInversionista(Long idInversionista);

}
