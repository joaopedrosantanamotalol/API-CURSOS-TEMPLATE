package com.example.sitemota.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sitemota.model.Curso;
import com.example.sitemota.model.TipoCurso;

public interface CursoRepository extends JpaRepository <Curso, Long> {
    Optional<Curso> findByNome(String nome);
    
    List<Curso> findByUsuarioId(Long id);
    List<Curso> findByTipocurso(TipoCurso tipocurso);
}
