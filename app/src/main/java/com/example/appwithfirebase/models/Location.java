package com.example.appwithfirebase.models;

public class Location {
    String id;

    String titulo;
    String descripcion;
    String imagen;

    public Location() {}

    public Location(String id, String titulo, String descripcion, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getId() {
        return id;
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
