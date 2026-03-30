package com.example.sitemota.dto;

import java.util.List;

public record UcResponse(  
    Long id,
    String nome,
    List<CursoResponse> cursos
 ) {
}