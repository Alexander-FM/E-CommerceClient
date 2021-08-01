package com.alexandertutoriales.cliente.ecommerce.communication;

import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;

public interface CarritoComunication {
    void eliminarDetalle(int idP);
    void actualizarCantidad(DetallePedido dp);
}
