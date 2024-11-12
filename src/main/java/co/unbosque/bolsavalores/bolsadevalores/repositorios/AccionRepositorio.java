package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Accion;

@Repository
public interface AccionRepositorio extends JpaRepository<Accion, Long>{

    @Query("SELECT a FROM Accion a WHERE a.fkInversionista=:idInversionista")
    public List<Accion> listarAccionPorInversionista(@Param("idInversionista") Long idInversionista);

   // @Query("SELECT COUNT(o) > 0 FROM OrdenSoloVenta o WHERE o.fkEmpresa = :empresaId AND o.fkInversionista = :inversionistaId AND o.estado = 'pendiente'")

}
