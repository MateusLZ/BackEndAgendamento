package br.com.api.produtos.servico;

import org.springframework.stereotype.Service;


import br.com.api.produtos.model.HorarioModelo;
import br.com.api.produtos.repositorio.HorarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioServico {
    

    @Autowired
    private HorarioRepositorio hr;

    public HorarioModelo cadastrarHorario(HorarioModelo horario) {
        return hr.save(horario);
    }

    
    public List<HorarioModelo> listarHorarios() {
        return (List<HorarioModelo>) hr.findAll();
    }

    public void excluirHorario(Long id) {
        hr.deleteById(id);
    }

    public HorarioModelo editarHorario(Long id, HorarioModelo horario) {
        Optional<HorarioModelo> optionalHorario = hr.findById(id);
        if (optionalHorario.isPresent()) {
            HorarioModelo horarioExistente = optionalHorario.get();
            horarioExistente.setDataHora(horario.getDataHora());
            // Você pode adicionar mais atributos para edição conforme necessário
            return hr.save(horarioExistente);
        }
        return null; // Ou lança uma exceção, dependendo do comportamento desejado
    }
    public HorarioModelo buscarPorId(Long id) {
        return hr.findById(id)
                 .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
    }
}
