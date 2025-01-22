package com.example.appwithfirebase.models;

public class Location {
    String titulo;
    String descripcion;
    String imagen;

    public Location() {}

    public Location(String titulo, String descripcion, String imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getImagen() {
        return imagen;
    }
}
