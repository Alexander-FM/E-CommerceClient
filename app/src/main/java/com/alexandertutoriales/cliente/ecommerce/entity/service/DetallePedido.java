package com.alexandertutoriales.cliente.ecommerce.entity.service;

public class DetallePedido {
    private int id;
    private int cantidad;
    private Double precio;
    private Platillo platillo;
    private Pedido pedido;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Platillo getPlatillo() {
        return platillo;
    }

    public void setPlatillo(Platillo platillo) {
        this.platillo = platillo;
    }

    public double getTotal() {
        return this.cantidad * this.precio;
    }

    public void addOne() {
        this.cantidad++;
    }

    public void removeOne() {
        this.cantidad--;
    }
}
