package com.alexandertutoriales.cliente.ecommerce.communication;

import android.content.Intent;

import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;

public interface Communication {
    void showDetails(Intent i);
    void exportInvoice(int idCli, int idOrden, String fileName);
}
