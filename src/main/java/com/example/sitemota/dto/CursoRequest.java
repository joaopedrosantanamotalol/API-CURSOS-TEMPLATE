package com.example.sitemota.dto;

import com.example.sitemota.model.TipoCurso;

public record CursoRequest(
    String nome,
    TipoCurso tipo,
    Integer ano_inicio,
    Integer ano_conclusao,
    String instituicao,
    String desc,
    Integer horasCurso
) {}
