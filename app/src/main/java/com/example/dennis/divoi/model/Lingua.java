package com.example.dennis.divoi.model;

import java.util.List;

/**
 * Created by dennis on 10/04/17.
 */

public class Lingua {

    private long id;
    private String nome;
    private String povo;
    private String localizacao;
    private String descricao;
    private Usuario usuario;
    private List<Palavra> palavras;

    public Lingua(){

    }

    public Lingua(String nome, String povo, String localizacao, String descricao, Usuario usuario, List<Palavra> palavras) {
        this.nome = nome;
        this.povo = povo;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.usuario = usuario;
        this.palavras = palavras;
    }

    public Lingua(String nome, String povo, String localizacao, String descricao, Usuario usuario) {
        this.nome = nome;
        this.povo = povo;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public Lingua(long id, String nome, String povo, String localizacao, String descricao, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.povo = povo;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }
    public void setId(long id){this.id = id;}
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getPovo() {
        return povo;
    }
    public void setPovo(String povo) {
        this.povo = povo;
    }
    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Palavra> getPalavras() {
        return palavras;
    }
    public void setPalavras(List<Palavra> palavras) {
        this.palavras = palavras;
    }

    @Override
    public String toString() {
        return "Lingua [id=" + id + ", nome=" + nome + ", povo=" + povo + ", localizacao=" + localizacao
                + ", descricao=" + descricao + ", usuario=" + usuario + ", palavras=" + palavras + "]";
    }


}
