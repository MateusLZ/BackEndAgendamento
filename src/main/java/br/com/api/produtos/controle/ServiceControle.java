// package br.com.api.produtos.controle;



// import br.com.api.produtos.model.ProdutoModelo;
// import br.com.api.produtos.model.CadastrarProdutoRequest;
// import br.com.api.produtos.servico.ProdutoServico;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/produtos")
// public class ServiceControle {

//     private final ProdutoServico produtoServico;

//     public ServiceControle(ProdutoServico produtoServico) {
//         this.produtoServico = produtoServico;
//     }

//     @PostMapping("/cadastrar")
// public ResponseEntity<?> cadastrarProduto(@RequestBody CadastrarProdutoRequest request) {
//     try {
//         ProdutoModelo produto = request.getProduto();
//         List<String> userIds = request.getUserIds();
 
//         // Adicione instruções de impressão para verificar os dados recebidos
//         System.out.println("Produto recebido: " + produto);
//         System.out.println("UserIds recebidos: " + userIds);

//         ProdutoModelo produtoCadastrado = produtoServico.cadastrarProduto(produto, userIds);
//         return ResponseEntity.ok(produtoCadastrado);
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar o produto: " + e.getMessage());
//     }
// }


//     @GetMapping("/{produtoId}")
//     public ResponseEntity<?> getProdutoById(@PathVariable long produtoId) {
//         ProdutoModelo produto = produtoServico.getProdutoById(produtoId);
//         if (produto != null) {
//             return ResponseEntity.ok(produto);
//         } else {
//             return ResponseEntity.notFound().build();
//         }
//     }

//     // Adicione outros métodos conforme necessário, como atualizar, excluir, listar todos os produtos, etc.
// }
