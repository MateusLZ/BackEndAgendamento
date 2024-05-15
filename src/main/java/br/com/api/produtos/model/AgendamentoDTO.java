package br.com.api.produtos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgendamentoDTO {
    private String usuarioId;
    private Long produtoId;
    private Long horarioId;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(String usuarioId, Long produtoId, Long horarioId, String funcionarioId) {
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
        this.horarioId = horarioId;
        // this.funcionarioId = funcionarioId;
    }
}
