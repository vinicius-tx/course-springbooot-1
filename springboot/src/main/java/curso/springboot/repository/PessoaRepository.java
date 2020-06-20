package curso.springboot.repository;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;
 
@Repository
@Transactional
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
	
	@Query(value = "select p from Pessoa p where p.nome like %?1%")
	public abstract List<Pessoa> pesquisarPorNome(String nome);
	
	@Query(value = "select p from Pessoa p where p.nome = :paramNome")
	public abstract Pessoa pesquisarPorNomeAbsoluto(@Param("paramNome") String nome);
	

}
