package br.com.api.produtos.controle;

    

import br.com.api.produtos.model.HorarioModelo;
import br.com.api.produtos.servico.HorarioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/horarios")
public class HorarioControle {


    @Autowired
    private HorarioServico hs;

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerHorario(@PathVariable Long id) {
        hs.excluirHorario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<HorarioModelo> editarHorario(@PathVariable Long id, @RequestBody HorarioModelo horario) {
        HorarioModelo horarioEditado = hs.editarHorario(id, horario);
        if (horarioEditado != null) {
            return ResponseEntity.ok(horarioEditado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<HorarioModelo> cadastrarHorario(@RequestBody HorarioModelo horario) {
        HorarioModelo horarioCadastrado = hs.cadastrarHorario(horario);
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioCadastrado);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<HorarioModelo>> listarHorarios() {
        List<HorarioModelo> horarios = hs.listarHorarios();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/")
    public String rota() {
        return "API de horários funcionando";
    }
}
