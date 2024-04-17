package br.com.api.produtos.controle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.api.produtos.model.AdministradorModelo;
import br.com.api.produtos.model.RespostaModelo;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole;
import br.com.api.produtos.servico.AdministradorServico;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdministradorControle {

    @Autowired
    private AdministradorServico administradorServico;

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<RespostaModelo> remover(@PathVariable Long id) {
        return administradorServico.remover(id);
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody AdministradorModelo administrador) {
        return administradorServico.cadastrarAlterar(administrador, "alterar");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody AdministradorModelo administrador) {
        return administradorServico.cadastrarAlterar(administrador, "cadastrar");
    }

    @GetMapping("/listar")
    public List<UserModelo> listar() {
        return administradorServico.listar();
    }

    @GetMapping("/listarPorRole/{role}")
public ResponseEntity<List<UserModelo>> listarPorRole(@PathVariable String role) {
    UserRole userRole = UserRole.valueOf(role.toUpperCase()); // Convertendo a string da role para enum
    List<UserModelo> usuariosPorRole = administradorServico.listarPorRole(userRole);
    return ResponseEntity.ok(usuariosPorRole);
}


    @GetMapping("/")
    public String rota() {
        return "API de administradores funcionando";
    }

    

    @GetMapping("/userData")
public ResponseEntity<Map<String, Object>> getUserData(@RequestHeader("Authorization") String token) {
    String cleanToken = token.replace("Bearer ", "");
    Map<String, Object> userData = administradorServico.getUserData(cleanToken);
    return ResponseEntity.ok(userData);
}

    
    

}
