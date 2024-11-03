package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenSoloVenta;

@Repository
public interface OrdenVentaRepositorio extends JpaRepository<OrdenSoloVenta, Long>{

    @Query("SELECT o FROM OrdenSoloVenta o WHERE o.fkInversionista=?1")
    public List<OrdenSoloVenta> encontrarOrdenPorInversionista(Long idInversionista);

    @Query("SELECT o FROM OrdenSoloVenta o WHERE o.fkComisionista=?1")
    public List<OrdenSoloVenta> encontrarOrdenPorComisionista(Long idComisionista);

    @Query("SELECT COUNT(o) > 0 FROM OrdenSoloVenta o WHERE o.fkEmpresa = :empresaId AND o.fkInversionista = :inversionistaId AND o.estado = 'pendiente'")
    boolean existsByFkEmpresaAndFkInversionistaAndEstado(@Param("empresaId") Long empresaId, @Param("inversionistaId") Long inversionistaId);

}
