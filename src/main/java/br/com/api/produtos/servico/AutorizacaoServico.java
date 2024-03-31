package br.com.api.produtos.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.api.produtos.repositorio.UserRepositorio;

@Service
public class AutorizacaoServico implements UserDetailsService {

    @Autowired
    UserRepositorio repositorio;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repositorio.findByLogin(username);
    }
    
}
