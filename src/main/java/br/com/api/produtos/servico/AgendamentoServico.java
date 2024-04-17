package br.com.api.produtos.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.produtos.model.AgendamentoModelo;
import br.com.api.produtos.repositorio.AgendamentoRepositorio;


import java.util.List;


@Service
public class AgendamentoServico {
    
    @Autowired
    private AgendamentoRepositorio agendamentoRepositorio;

        public AgendamentoModelo cadastrarAgendamento(AgendamentoModelo agendamento) {
            return agendamentoRepositorio.save(agendamento);
        }

    public List<AgendamentoModelo> listarAgendamentosPorUsuario(String idUsuario) {
        return agendamentoRepositorio.findByUsuarioId(idUsuario); 
    }

    
    public List<AgendamentoModelo> listarAgendamentosPorData(String data) {
        return agendamentoRepositorio.findByDataAgendamento(data); 
    }

    public void excluirAgendamento(Long id) {
        agendamentoRepositorio.deleteById(id); 
    }
}
