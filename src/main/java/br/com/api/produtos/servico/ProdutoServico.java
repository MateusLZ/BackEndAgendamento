package br.com.api.produtos.servico;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service; 

import br.com.api.produtos.model.ProdutoModelo;
import br.com.api.produtos.model.RespostaModelo;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.repositorio.ProdutoRepositorio;
import br.com.api.produtos.repositorio.UserRepositorio;

 
@Service
public class ProdutoServico {
    
    @Autowired 
    private ProdutoRepositorio pr;

    @Autowired
    private RespostaModelo rm;

    @Autowired
    private UserRepositorio ur;


    //Método para listar todos os produtos
    public Iterable<ProdutoModelo> listar(){
        Iterable<ProdutoModelo> produtos = pr.findAll();
        
        // Inicializar a relação muitos para muitos explicitamente
        for (ProdutoModelo produto : produtos) {
            produto.getUsuarios().size(); // Isso força a inicialização da relação
        }

        return produtos;
    }
    
    // No seu serviço ProdutoServico 

public ResponseEntity<?> editarProduto(long codigo, ProdutoModelo novoProduto) {
    Optional<ProdutoModelo> produtoOptional = pr.findById(codigo);

    if (produtoOptional.isPresent()) {
        ProdutoModelo produtoExistente = produtoOptional.get();
        produtoExistente.setNome(novoProduto.getNome());
        produtoExistente.setDescricao(novoProduto.getDescricao());

        return new ResponseEntity<>(pr.save(produtoExistente), HttpStatus.OK);
    } else {
        // Produto não encontrado
        return new ResponseEntity<>("Produto não encontrado", HttpStatus.NOT_FOUND);
    }
}


public ResponseEntity<?> cadastrarAlterar(ProdutoModelo pm, List<String> userIds, String acao) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Usuário é administrador, pode cadastrar produtos

            // Verificar se o limite de produtos foi excedido
            if (ProdutoModelo.isLimiteExcedido()) {
                rm.setMensagem("Limite de produtos atingido. Não é possível cadastrar mais produtos.");
                return new ResponseEntity<RespostaModelo>(rm, HttpStatus.BAD_REQUEST);
            }

            if (pm.getNome().equals("")) {
                rm.setMensagem("O nome do produto é obrigatório!");
                return new ResponseEntity<RespostaModelo>(rm, HttpStatus.BAD_REQUEST);
            } else if (pm.getDescricao().equals("")) {
                rm.setMensagem("A descrição do produto é obrigatória");
                return new ResponseEntity<RespostaModelo>(rm, HttpStatus.BAD_REQUEST);
            } else {
                // Buscar usuários com base nos IDs fornecidos
                List<UserModelo> usuarios = ur.findAllById(userIds);

                // Associar os usuários ao produto
                pm.setUsuarios(usuarios);

                // Associar o produto aos usuários
                for (UserModelo usuario : usuarios) {
                    usuario.getProdutos().add(pm);
                }

                if (acao.equals("cadastrar")) {
                    return new ResponseEntity<ProdutoModelo>(pr.save(pm), HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<ProdutoModelo>(pr.save(pm), HttpStatus.OK);
                }
            }
        } else {
            // Usuário não é administrador, não pode cadastrar produtos
            rm.setMensagem("Você não tem permissão para cadastrar produtos!");
            return new ResponseEntity<RespostaModelo>(rm, HttpStatus.FORBIDDEN);
        }
    } else {
        // Usuário não está autenticado, retorna status UNAUTHORIZED
        rm.setMensagem("Você precisa estar autenticado para cadastrar produtos!");
        return new ResponseEntity<RespostaModelo>(rm, HttpStatus.UNAUTHORIZED);
    }
}

    //Método para remover produtos
    public ResponseEntity<RespostaModelo> remover(long codigo){

        pr.deleteById(codigo);

        rm.setMensagem("O produto foi removido com sucesso!");
        return new ResponseEntity<RespostaModelo>(rm,HttpStatus.OK);
    }
}


