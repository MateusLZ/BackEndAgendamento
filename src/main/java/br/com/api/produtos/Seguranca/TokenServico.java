package br.com.api.produtos.Seguranca;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.api.produtos.model.UserModelo;
import br.com.api.produtos.repositorio.UserRepositorio;

@Service
public class TokenServico {

    @Autowired
    private UserRepositorio userRepository;
    
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModelo user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant expirationDate = genExpirationDate();
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withExpiresAt(Date.from(expirationDate))
                    .sign(algorithm);
                    System.out.println("Token gerado: " + token);  
                    user.setToken(token);
                    userRepository.save(user);
            return token;          
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro enquanto gerava o token", exception);
        }
    }
    public  String validateToken(String token){
        try {
            Algorithm algorithm =Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
            .withIssuer("auth-api")
            .build()
            .verify(token)
            .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    @Scheduled(fixedRate = 60000) // Verifica a cada minuto
    public void checkExpiredTokens() {
        List<UserModelo> users = userRepository.findAll(); 
        for (UserModelo user : users) {
            String token = user.getToken();
            if (token != null && !token.isEmpty()) { // Verifica se o token está presente
                user.setIsActive(true);
            } else {
                user.setIsActive(false);
            }
            userRepository.save(user);
        }
    }

    
}
