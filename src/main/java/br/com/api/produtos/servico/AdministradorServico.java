package br.com.api.produtos.servico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.api.produtos.Seguranca.TokenServico;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole;
import br.com.api.produtos.repositorio.UserRepositorio;

@Service
public class AdministradorServico {


    @Autowired
    private UserRepositorio userRepositorio;

    @Autowired
    private TokenServico tokenServico;


    public boolean isUserAdmin(String token) {
        String username = tokenServico.validateToken(token);
        UserModelo user = userRepositorio.findByLogin(username);
        return user != null && user.getRole() == UserRole.ADMIN;
    }
    
     public Map<String, Object> getUserData(String token) {
        String username = tokenServico.validateToken(token);
        UserModelo user = userRepositorio.findByLogin(username);
        boolean isAdmin = isUserAdmin(token);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getId());
        userData.put("userName", user.getUserName());
        userData.put("name", user.getUsername());
        userData.put("Role", user.getRole());
        userData.put("isAdmin", isAdmin);
        userData.put("is_active", user.isActive());

        return userData;
    }

    public List<UserModelo> listar() {
        return userRepositorio.findAll(); 
    }

    public Page<UserModelo> listarPorRole(UserRole role, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return userRepositorio.findByRole(role, pageable);
}

}
