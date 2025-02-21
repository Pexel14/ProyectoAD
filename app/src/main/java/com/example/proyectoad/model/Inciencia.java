package com.example.proyectoad.model;

public class Inciencia {

    private int id;
    private String comentario;
    private String descripcion;
    private String imageUrl;
    private String titulo;
    private String estado;

    public Inciencia() {}

    public Inciencia(int id, String comentario, String descripcion, String imageUrl, String titulo, String estado) {
        this.id = id;
        this.comentario = comentario;
        this.descripcion = descripcion;
        this.imageUrl = imageUrl;
        this.titulo = titulo;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
