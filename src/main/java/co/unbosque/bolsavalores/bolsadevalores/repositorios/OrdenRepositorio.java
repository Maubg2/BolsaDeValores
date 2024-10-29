package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import co.unbosque.bolsavalores.bolsadevalores.entidades.OrdenCompraVenta;

@Repository
public interface OrdenRepositorio extends JpaRepository<OrdenCompraVenta, Long>{

    @Query("SELECT o FROM OrdenCompraVenta o WHERE o.fkInversionista=?1")
    public List<OrdenCompraVenta> encontrarOrdenPorInversionista(Long idInversionista);

    @Query("SELECT o FROM OrdenCompraVenta o WHERE o.fkComisionista=?1")
    public List<OrdenCompraVenta> encontrarOrdenPorComisionista(Long idComisionista);

}
