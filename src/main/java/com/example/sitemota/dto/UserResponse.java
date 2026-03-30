package com.example.sitemota.dto;

// saida de dados
public record UserResponse(
    Long id,
    String email,
    String nome,
    String sexo,
    String role
) {
    
}
