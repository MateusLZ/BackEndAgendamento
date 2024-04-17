package br.com.api.produtos.controle;

import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.model.CadastrarProdutoRequest;
import br.com.api.produtos.model.ProdutoModelo;
import br.com.api.produtos.model.RespostaModelo;
import br.com.api.produtos.servico.ProdutoServico;

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

    @PutMapping("/editar/{id}")
public ResponseEntity<?> editarProduto(@PathVariable long id, @RequestBody ProdutoModelo novoProduto) {
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
