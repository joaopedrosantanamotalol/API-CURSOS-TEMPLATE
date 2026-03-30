package com.example.sitemota.dto;

// entrada de dados
public record UserRequest(
    Long id,
    String email,
    String nome,
    String senha,
    String sexo,
    String role
) {
    
}
