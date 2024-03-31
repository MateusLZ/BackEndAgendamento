package br.com.api.produtos.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.produtos.model.FuncionarioModelo;

@Repository
public interface FuncionarioRepositorio extends CrudRepository<FuncionarioModelo, Long> {
    
}
