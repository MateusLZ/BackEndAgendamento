package br.com.api.produtos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Persistence;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
public class ProdutoModelo {
    
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    private String nome;
    private String descricao;
    private String nomeImagem;

    public static final int MAX_PRODUTOS = 6;

    public static boolean isLimiteExcedido() {
        System.out.println("Verificando se o limite de produtos foi excedido...");
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("nome_da_sua_unidade_de_persistencia");
            em = emf.createEntityManager();
            
            long quantidadeProdutos = (long) em.createQuery("SELECT COUNT(p) FROM ProdutoModelo p").getSingleResult();
            
            System.out.println("Quantidade de produtos encontrados: " + quantidadeProdutos);
            
            return quantidadeProdutos >= MAX_PRODUTOS;
        } catch (Exception e) {
            System.err.println("Erro ao verificar o limite de produtos: " + e.getMessage());
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }
    
}
