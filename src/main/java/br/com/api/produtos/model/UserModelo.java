package br.com.api.produtos.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String login;

    private String userName;

    private String password;

    private UserRole role;

    public UserModelo(String login, String userName, String password, UserRole role){
        this.login = login;
        this.userName = userName;
        this.password = password;
        this.role = role;
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

   @Override
public Collection<? extends GrantedAuthority> getAuthorities() {
  if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
  else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
}

// @Converter(autoApply = true)
// public class UUIDAttributeConverter implements AttributeConverter<UUID, String> {

//     @Override
//     public String convertToDatabaseColumn(UUID attribute) {
//         return attribute.toString();
//     }

//     @Override
//     public UUID convertToEntityAttribute(String dbData) {
//         return UUID.fromString(dbData);
//     }
// }
}
