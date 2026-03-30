package com.example.sitemota.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCurso tipocurso;

    private Integer ano_inicio;

    private Integer ano_conclusao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    private String instituicao;

    private String descricao;

    private Integer horasCurso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoCurso getTipocurso() {
        return tipocurso;
    }

    public void setTipocurso(TipoCurso tipocurso) {
        this.tipocurso = tipocurso;
    }

    public Integer getAno_inicio() {
        return ano_inicio;
    }

    public void setAno_inicio(Integer ano_inicio) {
        this.ano_inicio = ano_inicio;
    }

    public Integer getAno_conclusao() {
        return ano_conclusao;
    }

    public void setAno_conclusao(Integer ano_conclusao) {
        this.ano_conclusao = ano_conclusao;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    public String getDesc() {
        return descricao;
    }

    public void setDesc(String desc) {
        this.descricao = desc;
    }

    public Integer getHorasCurso() {
        return horasCurso;
    }

    public void setHorasCurso(Integer horasCurso) {
        this.horasCurso = horasCurso;
    }
}
