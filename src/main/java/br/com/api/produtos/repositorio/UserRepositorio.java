package br.com.api.produtos.repositorio;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.model.UserRole; 


public interface UserRepositorio extends JpaRepository<UserModelo, String> {
    
    UserModelo findByLogin(String login);
    Page<UserModelo> findByRole(UserRole role, Pageable pageable); // Atualização do método para retornar Page<UserModelo>
    UserModelo findByToken(String token);
}
