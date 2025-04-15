package com.alexandertutoriales.cliente.ecommerce.entity.service.dto;

import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Pedido;

import java.util.ArrayList;

public class GenerarPedidoDTO {
    private Pedido pedido;
    private ArrayList<DetallePedido> detallePedido;

    //Generar un constructor vacío e instanciar los objetos para no inicializarlos después.
    public GenerarPedidoDTO() {
        this.pedido = new Pedido();
        this.detallePedido = new ArrayList<>();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ArrayList<DetallePedido> getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(ArrayList<DetallePedido> detallePedido) {
        this.detallePedido = detallePedido;
    }
}
