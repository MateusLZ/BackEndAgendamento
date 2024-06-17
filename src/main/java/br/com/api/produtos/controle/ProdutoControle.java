package br.com.api.produtos.controle;

import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.model.CadastrarProdutoRequest;
import br.com.api.produtos.model.ProdutoModelo;
import br.com.api.produtos.model.RespostaModelo;
import br.com.api.produtos.servico.ProdutoServico;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@CrossOrigin(origins = "*")
public class ProdutoControle {

    
    @Autowired
    private ProdutoServico ps;

    @DeleteMapping("/remover/{codigo}")
    public ResponseEntity<RespostaModelo> remover(@PathVariable long codigo){
        return ps.remover(codigo);
    }
    
    @Transactional
    @DeleteMapping("/remover/{productId}/usuarios")
    public ResponseEntity<?> removerProdutoDoUsuario(@PathVariable String productId, @RequestBody List<String> userIds) {
        return ps.removerProdutoDoUsuario(productId, userIds);
    }
    
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProduto(@PathVariable long id, @RequestBody ProdutoModelo novoProduto) {
        System.out.println("Recebido do frontend: " + novoProduto);  // Loga o objeto recebido
        System.out.println("Nome: " + novoProduto.getNome());
        System.out.println("Descrição: " + novoProduto.getDescricao());
        if (novoProduto.getUsuarios() != null) {
            System.out.println("Usuários: ");
            novoProduto.getUsuarios().forEach(usuario -> System.out.println("ID: " + usuario.getId()));
        }
        return ps.editarProduto(id, novoProduto);
    }

@PostMapping("/cadastrar")
public ResponseEntity<?> cadastrarProduto(@RequestBody CadastrarProdutoRequest request) {
    if (ProdutoModelo.isLimiteExcedido()) {
        RespostaModelo resposta = new RespostaModelo();
        resposta.setMensagem("Limite de produtos atingido. Não é possível cadastrar mais produtos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    } else {
        return ps.cadastrarAlterar(request.getProduto(), request.getUserIds(), "cadastrar");
    }
}

    @GetMapping("/listar")
    public Iterable<ProdutoModelo> listar() {
        return ps.listar();
    }
    

    @GetMapping("/")
    public String rota(){
        return "API de produtos funcionando";
    }

    
   
}
