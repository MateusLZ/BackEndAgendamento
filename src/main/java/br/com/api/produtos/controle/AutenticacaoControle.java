package br.com.api.produtos.controle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.Seguranca.TokenServico;
import br.com.api.produtos.model.AutenticacaoDTO;
import br.com.api.produtos.model.LoginResponseDTO;
import br.com.api.produtos.model.RegistroDTO;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.repositorio.UserRepositorio;
import jakarta.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("auth")
public class AutenticacaoControle {
    
    private static final Logger logger = LoggerFactory.getLogger(AutenticacaoControle.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepositorio repositorio;

    @Autowired
    private TokenServico tokenServico;


    @PostMapping("/login")
public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AutenticacaoDTO data) {
    logger.info("Recebido pedido de login para o usuário: {}", data.login());

    try {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        UserModelo userModelo = (UserModelo) auth.getPrincipal();
        userModelo.setIsActive(true);
        logger.info("Usuário autenticado com sucesso: {}", userModelo.getUsername());
        this.repositorio.save(userModelo);
        var token = tokenServico.generateToken((UserModelo) auth.getPrincipal());
        var userName = userModelo.getUserName();
        var userRole = ((UserModelo) auth.getPrincipal()).getRole();

        return ResponseEntity.ok(new LoginResponseDTO(token, userName, userRole));
    } catch (AuthenticationException e) {
        logger.error("Falha na autenticação para o usuário: {}", data.login());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

@PostMapping("/logout")
public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    try {
        System.out.println("Token recebido do front-end: " + token);
        UserModelo usuario = repositorio.findByToken(token.replace("Bearer ", ""));
        if (usuario != null) {
            usuario.setToken(null);
            usuario.setIsActive(false); // Opcional: Desativar o usuário ao fazer logout
            repositorio.save(usuario);
            return ResponseEntity.ok("Logout realizado com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Erro ao fazer logout.");
    }
}

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody @Valid RegistroDTO data) {
        // Verifica se o email fornecido é válido
        if (!isEmailValid(data.login())) {
            return ResponseEntity.badRequest().body("O email fornecido não é válido.");
        }
    
        // Verifica se o email já está em uso
        if (this.repositorio.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().body("O email já está em uso.");
        }
    
        
        // Criptografa a senha
        String senhaEncriptada = new BCryptPasswordEncoder().encode(data.password());
    
        // Cria um novo usuário com os dados fornecidos
        UserModelo newUser = new UserModelo(data.login(), data.userName(), senhaEncriptada, data.role(), false, senhaEncriptada);
    
        // Salva o novo usuário no banco de dados
        this.repositorio.save(newUser);
    
        return ResponseEntity.ok().build();
    }
    

// Método para verificar se o email é válido usando expressões regulares
private boolean isEmailValid(String email) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
}
}
