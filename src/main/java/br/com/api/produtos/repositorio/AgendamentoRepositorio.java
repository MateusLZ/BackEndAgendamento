package br.com.api.produtos.repositorio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.produtos.model.AgendamentoModelo;

import java.util.List;

@Repository
public interface AgendamentoRepositorio extends CrudRepository<AgendamentoModelo, Long> {
    
    @Query("SELECT a FROM AgendamentoModelo a WHERE REPLACE(a.dataAgendamento, '/', '') = ?1")
    List<AgendamentoModelo> findByDataAgendamento(String data);

    List<AgendamentoModelo> findByUsuarioId(String idUsuario);

}
  