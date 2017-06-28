package com.example.dennis.divoi.model;

/**
 * Created by dennis on 10/04/17.
 */

public class Palavra {

    private long id;
    private String palavra;
    private String traducao;
    private String aplicacaoFrase;
    private String traducaoFrase;
    private Usuario usuario;
    private Lingua lingua;
    private String imagemUrl;
    private String audioUrl;

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Palavra() {
    }

    public Palavra(String palavra, String traducao, String aplicacaoFrase, String traducaoFrase, Lingua lingua, Usuario usuario) {
        this.palavra = palavra;
        this.traducao = traducao;
        this.aplicacaoFrase = aplicacaoFrase;
        this.traducaoFrase = traducaoFrase;
        this.usuario = usuario;
        this.lingua = lingua;
    }

    public Palavra(long id, String palavra, String traducao, String aplicacaoFrase, String traducaoFrase, Lingua lingua, Usuario usuario) {
        this.id = id;
        this.palavra = palavra;
        this.traducao = traducao;
        this.aplicacaoFrase = aplicacaoFrase;
        this.traducaoFrase = traducaoFrase;
        this.usuario = usuario;
        this.lingua = lingua;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getPalavra() {
        return palavra;
    }


    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getTraducao() {
        return traducao;
    }

    public void setTraducao(String traducao) {
        this.traducao = traducao;
    }

    public String getAplicacaoFrase() {
        return aplicacaoFrase;
    }

    public void setAplicacaoFrase(String aplicacaoFrase) {
        this.aplicacaoFrase = aplicacaoFrase;
    }

    public String getTraducaoFrase() {
        return traducaoFrase;
    }

    public void setTraducaoFrase(String traducaoFrase) {
        this.traducaoFrase = traducaoFrase;
    }
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Lingua getLingua() {
        return lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    @Override
    public String toString() {
        return "Palavra{" +
                "id=" + id +
                ", palavra='" + palavra + '\'' +
                ", traducao='" + traducao + '\'' +
                ", aplicacaoFrase='" + aplicacaoFrase + '\'' +
                ", traducaoFrase='" + traducaoFrase + '\'' +
                ", usuario=" + usuario +
                ", lingua=" + lingua +
                ", imagemUrl='" + imagemUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }
}
