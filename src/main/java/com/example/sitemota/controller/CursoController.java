package com.example.sitemota.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sitemota.dto.CursoRequest;
import com.example.sitemota.dto.CursoResponse;
import com.example.sitemota.dto.UcResponse;
import com.example.sitemota.dto.UserFilter;
import com.example.sitemota.model.Curso;
import com.example.sitemota.service.CursoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class CursoController {

    private final CursoService curso;

    public CursoController(CursoService cursoService) {
        this.curso = cursoService;
    }

    /* ===== INICIO DAS ROTAS DE /ME/CURSOS/ ===== */

    @GetMapping("/me/cursos")
    public List<CursoResponse> meusCursos(Authentication authentication) {
        UserFilter userF = (UserFilter) authentication.getPrincipal();

        List<Curso> cursos = curso.listarCursosDoUsuario(userF.id());

        return cursos.stream()
                .map(curso::toResponse)
                .toList();
    }

    @PostMapping("/me/cursos")
    public CursoResponse criarCurso(
            @RequestBody CursoRequest request,
            Authentication authentication) {

        UserFilter userF = (UserFilter) authentication.getPrincipal();

        return curso.criar(userF.id(), request);
    }
    
    @PutMapping("/me/cursos/{cursoId}")
    public CursoResponse editarCurso(@PathVariable Long cursoId, @RequestBody CursoRequest request,
            Authentication auth) {
        UserFilter userfilter = (UserFilter) auth.getPrincipal();

        return curso.atualizarCurso(cursoId, userfilter.id(), request);
    }

    /* ===== FIM DAS ROTAS DE /ME/CURSOS ===== */

    /* ===== INICIO DAS ROTAS DE /{ID}CURSOS E /{ID}/CURRICULO ===== */
    @GetMapping("/{id}/cursos")
    public List<CursoResponse> CursosDoUsuario(@PathVariable long id) {
        List<Curso> cursos = curso.listarCursosDoUsuario(id);

        return cursos.stream()
                .map(curso::toResponse)
                .toList();

    }

    @GetMapping("/{id}/curriculo")
    public UcResponse curriculo(@PathVariable Long id) {
        return curso.BuscarUsuariosComCursos(id);
    }
    /* ===== FIM DAS ROTAS DE /{ID}CURSOS E /{ID}/CURRICULO ===== */

    /*===== INICIO DA LISTAGEM DE CURSOS DO USUÁRIO AUTENTICADO ===== */
    @GetMapping("/me/curriculo")
    public UcResponse meuCurriculo(Authentication authentication) {
        UserFilter userF = (UserFilter) authentication.getPrincipal();
        return curso.BuscarUsuariosComCursos(userF.id());
    }
    /*===== FIM DA LISTAGEM DE CURSOS DO USUÁRIO AUTENTICADO ===== */

    /*===== INICIO DAS ROTAS DE ADM ===== */
    @PutMapping("/{IdUsuario}/cursos/{cursoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CursoResponse editarCursoAdm(@PathVariable Long cursoId, @RequestBody CursoRequest request,
            @PathVariable Long IdUsuario) {

        return curso.atualizarCurso(cursoId, IdUsuario, request);
    }
    /*===== FIM DAS ROTAS DE ADM ===== */
}
