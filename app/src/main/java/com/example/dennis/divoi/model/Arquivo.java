package com.example.dennis.divoi.model;

/**
 * Created by dennis on 29/05/17.
 */

public class Arquivo {

    private Long id;
    private String audio;
    private Long idDialeto;
    private String imagem;

    public Arquivo(){}


    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }


    public Long getIdDialeto() {
        return idDialeto;
    }

    public void setIdDialeto(Long idDialeto) {
        this.idDialeto = idDialeto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return "Arquivo [id=" + id + ", idDialeto=" + idDialeto + ", imagem=" + imagem + "]";
    }
}
