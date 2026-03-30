package com.example.sitemota.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sitemota.dto.CursoRequest;
import com.example.sitemota.dto.CursoResponse;
import com.example.sitemota.dto.UcResponse;
import com.example.sitemota.model.Curso;
import com.example.sitemota.model.User;
import com.example.sitemota.repository.CursoRepository;
import com.example.sitemota.repository.UserRepository;

@Service
public class CursoService {
    private CursoRepository repository;
    private UserRepository repositoryUser;

    public CursoService(CursoRepository repository, UserRepository repositoryUser) {
        this.repository = repository;
        this.repositoryUser = repositoryUser;
    }

    // passando a entidade para DTO em captação para segurança de dados
    private Curso toEntity(CursoRequest request) {
        Curso curso = new Curso();
        curso.setNome(request.nome());
        curso.setTipocurso(request.tipo());
        curso.setAno_inicio(request.ano_inicio());
        curso.setAno_conclusao(request.ano_conclusao());
        curso.setInstituicao(request.instituicao());
        curso.setDesc(request.desc());
        curso.setHorasCurso(request.horasCurso());
        return curso;
    }

    // passando a entidade para DTO-RESPONSE para a exibição dos dados da entidade
    // filtrados pelo entites
    public CursoResponse toResponse(Curso curso) {
        return new CursoResponse(
                curso.getId(),
                curso.getNome(),
                curso.getTipocurso(),
                curso.getAno_inicio(),
                curso.getAno_conclusao(),
                curso.getInstituicao(),
                curso.getDesc(),
                curso.getHorasCurso());
    }

    // método que cria o curso baseado no filtro CURSORESPONSE
    public CursoResponse criar(Long usuarioid, CursoRequest request) {
        validarAno(request.ano_inicio(), request.ano_conclusao());
        User usuario = repositoryUser.findById(usuarioid).orElseThrow();

        Curso curso = toEntity(request);
        curso.setUsuario(usuario);
        Curso salvo = repository.save(curso);
        return toResponse(salvo);
    }

    // Listagem dos cursos do usuario
    public List<Curso> listarCursosDoUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    // Busca de usuário baseada em seu email
    public User buscarUsuarioPorEmail(String email) {
        return repositoryUser.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    // UcResponse = User Curso Response, resposta dos cursos do usuario que incluí
    // os cursos listado + id e nome do usuário
    public UcResponse BuscarUsuariosComCursos(Long id) {
        User usuario = repositoryUser.findById(id).orElseThrow();

        List<CursoResponse> cursos = usuario.getCursos()
                .stream()
                .map(this::toResponse)
                .toList();

        return new UcResponse(
                usuario.getId(),
                usuario.getNome(),
                cursos);
    }

    // método criado para atualização generalizada do curso, onde você atualiza o
    // curso Inteiro
    public CursoResponse atualizarCurso(Long cursoId, Long usuarioId, CursoRequest request) {
        validarAno(request.ano_inicio(), request.ano_conclusao());
        Curso curso = repository.findById(cursoId).orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (!curso.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Curso não pertence ao usuário");
        }

        curso.setAno_conclusao(request.ano_conclusao());
        curso.setAno_inicio(request.ano_inicio());
        curso.setDesc(request.desc());
        curso.setHorasCurso(request.horasCurso());
        curso.setInstituicao(request.instituicao());
        curso.setNome(request.nome());
        curso.setTipocurso(request.tipo());
        Curso atualizado = repository.save(curso);

        return toResponse(atualizado);
    }

    // Método de validação dos anos de inicio e fim
    private void validarAno(Integer ano_inicio, Integer ano_final) {
        if (ano_inicio != null && ano_final != null && ano_inicio > ano_final) {
            throw new RuntimeException("Ano de início não pode ser maior que o ano de conclusão");
        }
    }
}
