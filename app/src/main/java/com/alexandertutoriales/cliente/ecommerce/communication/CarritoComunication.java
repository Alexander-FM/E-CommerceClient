package com.alexandertutoriales.cliente.ecommerce.communication;


import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;

import java.util.List;

public interface CarritoComunication {
    void eliminarDetalle(int idP);
    void mostrarTotalPagar(List<DetallePedido> detallePedido);
}
