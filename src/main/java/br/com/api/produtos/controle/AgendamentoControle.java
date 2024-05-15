package br.com.api.produtos.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.model.AgendamentoModelo;
import br.com.api.produtos.model.HorarioModelo;
import br.com.api.produtos.servico.AgendamentoServico;
import br.com.api.produtos.servico.HorarioServico;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/agendamentos")
public class AgendamentoControle {
    
        @Autowired
    private AgendamentoServico agendamentoServico;

        @Autowired
    private HorarioServico horarioServico;

    @PostMapping("/cadastrar")
    public ResponseEntity<AgendamentoModelo> cadastrarAgendamento(@RequestBody AgendamentoModelo agendamento) {

        HorarioModelo horario = horarioServico.buscarPorId(agendamento.getHorario().getId());
        AgendamentoModelo agendamentoCadastrado = agendamentoServico.cadastrarAgendamento(agendamento);
        agendamento.setHorario(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoCadastrado);
    }
 
    @GetMapping("/listarPorData/{data}")
    public ResponseEntity<List<AgendamentoModelo>> listarAgendamentosPorData(@PathVariable("data") String data) {

    List<AgendamentoModelo> agendamentos = agendamentoServico.listarAgendamentosPorData(data);
   
        return ResponseEntity.ok(agendamentos);
    }
    
    @GetMapping("/listarPorUsuario/{idUsuario}")
    public ResponseEntity<List<AgendamentoModelo>> listarAgendamentosPorUsuario(@PathVariable("idUsuario") String idUsuario) {
        List<AgendamentoModelo> agendamentos = agendamentoServico.listarAgendamentosPorUsuario(idUsuario);
        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerAgendamento(@PathVariable Long id) {
        agendamentoServico.excluirAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public String rota() {
        return "API de agendamentos funcionando";
    }

}
