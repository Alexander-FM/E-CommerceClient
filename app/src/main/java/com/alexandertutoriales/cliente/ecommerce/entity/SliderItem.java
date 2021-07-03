package com.alexandertutoriales.cliente.ecommerce.entity;

public class SliderItem {
    private String titulo;
    private int imagen;

    public SliderItem() {
    }

    public SliderItem(int imagen, String titulo ) {
        this.imagen = imagen;
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
