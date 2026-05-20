package com.example.unifreelanceapp;

public class Post {

    public String id;
    public String userid;
    public String titulo;
    public String descripcion;

    public Post() {
        // necesario para Firebase
    }

    public Post(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }
}
