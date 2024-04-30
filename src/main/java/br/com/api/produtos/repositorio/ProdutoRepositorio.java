package br.com.api.produtos.repositorio;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.api.produtos.model.ProdutoModelo;

@Repository
public interface ProdutoRepositorio extends CrudRepository<ProdutoModelo, Long> {
    
    @Modifying
    @Transactional
    @Query("UPDATE ProdutoModelo p SET p.usuarios = NULL WHERE p.codigo = :produto")
    void removerRelacoesPorProdutoId(@Param("produto") Long produto);
    
}

