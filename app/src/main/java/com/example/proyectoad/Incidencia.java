package com.example.proyectoad;

public class Incidencia {
    private int id;
    private String titulo;
    private String descripcion;
    private String comentario;
    private String foto;
    private String estado;
    private String usuario;

    public Incidencia(){}

    public Incidencia(int id, String titulo, String descripcion, String comentario, String foto, String estado, String usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comentario = comentario;
        this.foto = foto;
        this.estado = estado;
        this.usuario = usuario;
    }

    public Incidencia(Integer id, String titulo, String foto) {
        this.id = id;
        this.titulo = titulo;
        this.foto = foto;
    }

    public Incidencia(Integer id, String titulo, String foto, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.foto = foto;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
