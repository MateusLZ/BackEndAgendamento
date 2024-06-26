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

    public List<HorarioModelo> listarHorariosAtivos() {
        return hr.findByAtivo(true);
    }

    public void excluirHorario(Long id) {
        hr.deleteById(id);
    }

    public HorarioModelo editarHorario(Long id, HorarioModelo horario) {
        Optional<HorarioModelo> optionalHorario = hr.findById(id);
        if (optionalHorario.isPresent()) {
            HorarioModelo horarioExistente = optionalHorario.get();
            horarioExistente.setDataHora(horario.getDataHora());
            return hr.save(horarioExistente);
        }
        return null; 
    }

    public HorarioModelo atualizarStatusAtivo(Long id, boolean ativo) {
        Optional<HorarioModelo> optionalHorario = hr.findById(id);
        if (optionalHorario.isPresent()) {
            HorarioModelo horarioExistente = optionalHorario.get();
            horarioExistente.setAtivo(ativo);
            return hr.save(horarioExistente);
        }
        return null;
    }
    public HorarioModelo buscarPorId(Long id) {
        return hr.findById(id)
                 .orElseThrow(() -> new RuntimeException("Horário não encontrado"));
    }
}
