package br.com.api.produtos.servico;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;

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

    @Autowired
    private EntityManager em;


    public Iterable<ProdutoModelo> listar(){
        Iterable<ProdutoModelo> produtos = pr.findAll();
        
        for (ProdutoModelo produto : produtos) {
            produto.getUsuarios().size(); 
        }

        return produtos;
    }
    
public ResponseEntity<?> editarProduto(long codigo, ProdutoModelo novoProduto) {
    Optional<ProdutoModelo> produtoOptional = pr.findById(codigo);

    if (produtoOptional.isPresent()) {
        ProdutoModelo produtoExistente = produtoOptional.get();
        produtoExistente.setNome(novoProduto.getNome());
        produtoExistente.setDescricao(novoProduto.getDescricao());

        return new ResponseEntity<>(pr.save(produtoExistente), HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Produto não encontrado", HttpStatus.NOT_FOUND);
    }
}


public ResponseEntity<?> cadastrarAlterar(ProdutoModelo pm, List<String> userIds, String acao) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

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
                List<UserModelo> usuarios = ur.findAllById(userIds);

                pm.setUsuarios(usuarios);

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
            rm.setMensagem("Você não tem permissão para cadastrar produtos!");
            return new ResponseEntity<RespostaModelo>(rm, HttpStatus.FORBIDDEN);
        }
    } else {
        rm.setMensagem("Você precisa estar autenticado para cadastrar produtos!");
        return new ResponseEntity<RespostaModelo>(rm, HttpStatus.UNAUTHORIZED);
    }
}

    public ResponseEntity<RespostaModelo> remover(long codigo){

        pr.deleteById(codigo);

        rm.setMensagem("O produto foi removido com sucesso!");
        return new ResponseEntity<RespostaModelo>(rm,HttpStatus.OK);
    }
    public ResponseEntity<?> removerProdutoDoUsuario(String productId, List<String> userIds) {
        try {
            Optional<ProdutoModelo> produtoOptional = pr.findById(Long.parseLong(productId));
            if (!produtoOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }
            ProdutoModelo produto = produtoOptional.get();
    
            for (String userId : userIds) {
                Optional<UserModelo> userOptional = ur.findById(userId);
                if (userOptional.isPresent()) {
                    UserModelo usuario = userOptional.get();
                    produto.removerUsuario(usuario);
                    usuario.removerProduto(produto);
                    em.merge(usuario);
                } else {
                    continue;
                }
            }
    
    
            return ResponseEntity.ok("Associações do produto removidas com sucesso.");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID do produto inválido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover associações do produto: " + e.getMessage());
        }
    }
    

}


