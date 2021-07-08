package com.alexandertutoriales.cliente.ecommerce.entity.service.dto;


import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Pedido;

import java.util.ArrayList;
import java.util.List;

public class PedidoConDetallesDTO {
    private Pedido pedido;
    private List<DetallePedido> detallePedidos;

    public PedidoConDetallesDTO() {
        this.pedido = new Pedido();
        this.detallePedidos = new ArrayList<>();
    }

    public PedidoConDetallesDTO(Pedido pedido, List<DetallePedido> detallePedidos) {
        this.pedido = pedido;
        this.detallePedidos = detallePedidos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public void setDetallePedidos(List<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }
}
