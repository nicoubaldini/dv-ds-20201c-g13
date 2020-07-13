package ar.edu.davinci.dvds20201cg13.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.davinci.dvds20201cg13.modelo.OrdenItem;

@Repository("ordenItemRepositorio")
public interface OrdenItemRepositorio extends JpaRepository<OrdenItem, Long>{

	//public List<Orden> searchByName(@Param("nombre") String name) throws DataAccessException;

}
