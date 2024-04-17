package br.com.api.produtos.servico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.api.produtos.Seguranca.TokenServico;
import br.com.api.produtos.model.AdministradorModelo;
import br.com.api.produtos.model.RespostaModelo;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole;
import br.com.api.produtos.repositorio.AdministradorRepositorio;
import br.com.api.produtos.repositorio.UserRepositorio;

@Service
public class AdministradorServico {

    @Autowired 
    private AdministradorRepositorio administradorRepositorio;

    @Autowired
    private UserRepositorio userRepositorio;

    @Autowired
    private TokenServico tokenServico;

    @Autowired
    private RespostaModelo rm;

    public boolean isUserAdmin(String token) {
        String username = tokenServico.validateToken(token);
        // Buscar o usuário pelo nome de usuário
        UserModelo user = userRepositorio.findByLogin(username);
    
        // Verificar se o usuário é um administrador
        return user != null && user.getRole() == UserRole.ADMIN;
    }
    
     public Map<String, Object> getUserData(String token) {
        String username = tokenServico.validateToken(token);
        // Buscar o usuário pelo nome de usuário
        UserModelo user = userRepositorio.findByLogin(username);
    
        // Verificar se o usuário é um administrador
        boolean isAdmin = isUserAdmin(token);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getId());
        userData.put("userName", user.getUserName());
        userData.put("name", user.getUsername());
        userData.put("Role", user.getRole());
        userData.put("isAdmin", isAdmin);

        return userData;
    }

    // Método para listar todos os administradores
    public List<UserModelo> listar() {
        return userRepositorio.findAll();
    }

    
    // Método para cadastrar ou alterar administradores
    public ResponseEntity<?> cadastrarAlterar(AdministradorModelo administrador, String acao) {
        if (administrador.getNome().equals("")) {
            rm.setMensagem("O nome do administrador é obrigatório!");
            return new ResponseEntity<RespostaModelo>(rm, HttpStatus.BAD_REQUEST);
        } else if (administrador.getEmail().equals("")) {
            rm.setMensagem("O e-mail do administrador é obrigatório");
            return new ResponseEntity<RespostaModelo>(rm, HttpStatus.BAD_REQUEST);
        } else {
            if (acao.equals("cadastrar")) {
                return new ResponseEntity<AdministradorModelo>(administradorRepositorio.save(administrador), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<AdministradorModelo>(administradorRepositorio.save(administrador), HttpStatus.OK);
            }
        }
    }

    // Método para remover administradores
    public ResponseEntity<RespostaModelo> remover(long id) {
        administradorRepositorio.deleteById(id);
        rm.setMensagem("O administrador foi removido com sucesso!");
        return new ResponseEntity<RespostaModelo>(rm, HttpStatus.OK);
    }

    public List<UserModelo> listarPorRole(UserRole role) {
        return userRepositorio.findByRole(role);
    }
    
}
