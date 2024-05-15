package br.com.api.produtos.controle;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole;
import br.com.api.produtos.servico.AdministradorServico;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdministradorControle {

    @Autowired
    private AdministradorServico administradorServico;

  
    @GetMapping("/listar")
    public List<UserModelo> listar() {
        return administradorServico.listar();
    }

//     @GetMapping("/listarPorRole/{role}")
// public ResponseEntity<List<UserModelo>> listarPorRole(@PathVariable String role) {
//     UserRole userRole = UserRole.valueOf(role.toUpperCase()); 
//     List<UserModelo> usuariosPorRole = administradorServico.listarPorRole(userRole);
//     return ResponseEntity.ok(usuariosPorRole);
// }
@GetMapping("/listarPorRole/{role}")
public ResponseEntity<Page<UserModelo>> listarPorRole(@PathVariable String role,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "8") int size) {
    UserRole userRole = UserRole.valueOf(role.toUpperCase()); 
    Page<UserModelo> pageUsuariosPorRole = administradorServico.listarPorRole(userRole, page, size);
    return ResponseEntity.ok(pageUsuariosPorRole);
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
