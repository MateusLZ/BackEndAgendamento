package br.com.api.produtos.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.produtos.model.HorarioModelo;

@Repository
public interface HorarioRepositorio extends CrudRepository<HorarioModelo, Long> {
    List<HorarioModelo> findByAtivo(boolean ativo);
}
