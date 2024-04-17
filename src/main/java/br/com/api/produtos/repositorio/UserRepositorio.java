package br.com.api.produtos.repositorio;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole; 


public interface UserRepositorio extends JpaRepository<UserModelo, String> {
    
    UserModelo  findByLogin(String login);
    List<UserModelo>  findByRole(UserRole role);
}
