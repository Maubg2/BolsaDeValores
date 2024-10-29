package co.unbosque.bolsavalores.bolsadevalores.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.unbosque.bolsavalores.bolsadevalores.entidades.Empresa;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, Long>{

}
