package com.dam.appmarvel.model;

public class Personagem {

//     MODEL: Nessa camada, temos as classes que serão manipuladas. O Model disponibiliza os campos que o presenter atualizará

    private String id;
    private String nome;
    private String url;
    private String description;

    public Personagem(String id, String nome, String url, String resultDescription){
        this.id = id;
        this.nome = nome;
        this.url = url;
        this.description = resultDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
