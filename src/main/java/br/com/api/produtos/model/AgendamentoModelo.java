package br.com.api.produtos.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

 
@Entity
@Table(name = "agendamentos")
@Getter
@Setter
public class AgendamentoModelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private UserModelo usuario;

    @ManyToOne
    @JoinColumn(name = "horario_id")
    private HorarioModelo horario;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoModelo produto;

    private String dataAgendamento;


}
