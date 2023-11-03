package com.alexandertutoriales.cliente.ecommerce.entity.service;

public class OfertaProducto {

    private int id;
    private Oferta idOferta;
    private Platillo idPlatillo;
    private int descuento;
    private Double precioAhora;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Oferta getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Oferta idOferta) {
        this.idOferta = idOferta;
    }

    public Platillo getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(Platillo idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }

    public Double getPrecioAhora() {
        return precioAhora;
    }

    public void setPrecioAhora(Double precioAhora) {
        this.precioAhora = precioAhora;
    }
}
