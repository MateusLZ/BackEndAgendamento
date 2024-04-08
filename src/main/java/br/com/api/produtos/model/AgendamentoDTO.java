package br.com.api.produtos.model;

public class AgendamentoDTO {
    private String usuarioId;
    private Long produtoId;
    private Long horarioId;

    // Construtores, getters e setters

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(String usuarioId, Long produtoId, Long horarioId) {
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
        this.horarioId = horarioId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Long getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Long horarioId) {
        this.horarioId = horarioId;
    }
}