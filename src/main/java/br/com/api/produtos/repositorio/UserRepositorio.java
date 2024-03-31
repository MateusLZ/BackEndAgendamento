package br.com.api.produtos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.produtos.model.UserModelo;


public interface UserRepositorio extends JpaRepository<UserModelo, String> {
    
    UserModelo  findByLogin(String login);
}
