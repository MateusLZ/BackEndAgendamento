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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.produtos.Seguranca.TokenServico;
import br.com.api.produtos.model.AutenticacaoDTO;
import br.com.api.produtos.model.LoginResponseDTO;
import br.com.api.produtos.model.RegistroDTO;
import br.com.api.produtos.model.SenhaDTO;
import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.repositorio.UserRepositorio;
import jakarta.validation.Valid;

import java.util.Optional;
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

        if (data.login() == null || data.login().isEmpty()) {
            return ResponseEntity.badRequest().body("O campo Email não pode ser nulo ou vazio.");
        }
        
        if (data.userName() == null || data.userName().isEmpty()) {
            return ResponseEntity.badRequest().body("O campo Nome não pode ser nulo ou vazio.");
        }
        
        if (data.password() == null || data.password().isEmpty()) {
            return ResponseEntity.badRequest().body("O campo Senha não pode ser nulo ou vazio.");
        }
    
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
        UserModelo newUser = new UserModelo(data.login(), data.userName(), senhaEncriptada, data.role(), false, senhaEncriptada,data.phone());
    
        // Salva o novo usuário no banco de dados
        this.repositorio.save(newUser);
    
        return ResponseEntity.ok().build();
    }


    @PutMapping("/editar")
public ResponseEntity<?> editarUsuario(@RequestHeader("Authorization") String token, 
                                       @RequestBody @Valid RegistroDTO data) {
    try {
        String login = tokenServico.validateToken(token.replace("Bearer ", ""));
        UserModelo usuario = repositorio.findByLogin(login);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        // Atualiza os dados do usuário
        if (data.userName() != null && !data.userName().equals(usuario.getUserName())) {
            usuario.setUserName(data.userName());
        }
        if (data.phone() != null && !data.phone().equals(usuario.getPhone())) {
            usuario.setPhone(data.phone());
        }
        
        if (data.login() != null && !data.login().equals(usuario.getLogin())) {
            // Verifica se o novo email já está em uso
            if (repositorio.findByLogin(data.login()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("O email já está em uso.");
            }
            usuario.setLogin(data.login());
        }

        repositorio.save(usuario);
        String novoToken = tokenServico.generateToken(usuario);
        // Retorne o novo token ao cliente
        return ResponseEntity.ok(novoToken);
    } catch (Exception e) {
        logger.error("Erro ao atualizar usuário: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário.");
    }
}

    @PutMapping("/editar/senha")
    public ResponseEntity<?> editarSenha(@RequestHeader("Authorization") String token, 
                                         @RequestBody @Valid SenhaDTO novaSenha) {
        try {
            String login = tokenServico.validateToken(token.replace("Bearer ", ""));
            UserModelo usuario = repositorio.findByLogin(login);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }

            // Verifica se a senha atual fornecida pelo usuário está correta
            if (new BCryptPasswordEncoder().matches(novaSenha.getSenhaAtual(), usuario.getPassword())) {
                // Aplica a nova senha
                usuario.setPassword(new BCryptPasswordEncoder().encode(novaSenha.getNovaSenha()));
                repositorio.save(usuario);

                return ResponseEntity.ok("Senha atualizada com sucesso");
            } else {
                // Senha atual incorreta
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar a senha", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar senha do usuário");
        }
    }

    @DeleteMapping("/excluir/{funcionarioId}")
public ResponseEntity<String> excluirUsuario(@PathVariable String funcionarioId) {
    try {
        // Encontre o funcionário pelo ID
        Optional<UserModelo> usuarioOpt = repositorio.findById(funcionarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }

        // Exclua o funcionário do banco de dados
        repositorio.delete(usuarioOpt.get());
        return ResponseEntity.ok("Funcionário excluído com sucesso.");
    } catch (Exception e) {
        logger.error("Erro ao excluir funcionário: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir funcionário.");
    }
}
private boolean isEmailValid(String email) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
}
}
