package com.example.proyectoad;

public class User {
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasenia;
    private String incidencias;
    private String telefono;
    private String piso_letra;
    private String foto_perfil;
    private boolean es_admin;

    public User(String nombre, String correo){
        this.correo = correo;
        this.nombre = nombre;
        this.foto_perfil = String.valueOf(R.drawable.userprofile);
        this.es_admin = false;
    }


    public User(String nombre, String correo, String contrasenia){
        this.nombre = nombre;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.foto_perfil = String.valueOf(R.drawable.userprofile);
        this.incidencias = "";
        this.es_admin = false;
    }

    public User(String nombre, String apellidos, String correo, String contrasenia, String incidencias, String telefono, String piso_letra, String foto_perfil, boolean es_admin) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.incidencias = incidencias;
        this.telefono = telefono;
        this.piso_letra = piso_letra;
        this.foto_perfil = foto_perfil;
        this.es_admin = es_admin;
    }

    public User(){}

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(String incidencias) {
        this.incidencias = incidencias;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPiso_letra() {
        return piso_letra;
    }

    public void setPiso_letra(String piso_letra) {
        this.piso_letra = piso_letra;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public boolean isEs_admin() {
        return es_admin;
    }

    public void setEs_admin(boolean es_admin) {
        this.es_admin = es_admin;
    }
}
