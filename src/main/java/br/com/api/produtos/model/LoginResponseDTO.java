package br.com.api.produtos.model;

public record LoginResponseDTO(String token, String userName, UserRole  role) {

    public LoginResponseDTO(String token, UserRole role) {
        this(token, null, role);
    }
    public LoginResponseDTO(String token, String userName) {
        this(token, userName, null);
    }
}
