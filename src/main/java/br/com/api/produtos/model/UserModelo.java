package br.com.api.produtos.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(name ="users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModelo implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String userName;
 
    @Column(nullable = false)
    private String password;

    private String phone;

    private UserRole role;

    private boolean isActive;

    private String token;

    @ManyToMany
    @JoinTable(
                name = "user_servicos",
                joinColumns = @JoinColumn(name="users_id"),
                inverseJoinColumns = @JoinColumn(name="produtos_id")
    )
    @JsonIgnoreProperties("usuarios") 
    private List<ProdutoModelo> produtos;



    public UserModelo(String login, String userName, String password, UserRole role, boolean isActive, String token, String phone){
        this.login = login;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.token = token;
        this.phone = phone;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public String getUserName() {
        return userName;
    }
    
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        } else if (this.role == UserRole.FUNCIONARIO) {
            return List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
    
    // Dentro da classe UserModelo
public void removerProduto(ProdutoModelo produto) {
    produtos.remove(produto);
    produto.getUsuarios().remove(this);
}

public void adicionarProduto(ProdutoModelo produto) {
    this.produtos.add(produto);
    produto.getUsuarios().add(this);
}


}
