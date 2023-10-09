package com.alexandertutoriales.cliente.ecommerce.utils;

import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;

import java.util.ArrayList;

public class Carrito {
    //Creamos un arrayList de la clase detallePedido
    private static final ArrayList<DetallePedido> detallePedidos = new ArrayList<>();

    //Método para agregar productos al carrito(bolsa)
    public static String agregarPlatillos(DetallePedido detallePedido) {
        for (DetallePedido dp : detallePedidos) {
            if (dp.getPlatillo().getId() == detallePedido.getPlatillo().getId()) {
                dp.setCantidad(dp.getCantidad() + detallePedido.getCantidad());
                return "El producto ha sido agregado al carrito, se actualizará la cantidad";
            }
        }
        detallePedidos.add(detallePedido);
        return "El producto ha sido agregado al carrito con éxito";
    }

    //Método para eliminar un platillo del carrito(bolsa)
    public static void eliminar(final int idp) {
        DetallePedido dpE = null;
        for (DetallePedido dp : detallePedidos) {
            if (dp.getPlatillo().getId() == idp) {
                dpE = dp;
                break;
            }
        }
        if (dpE != null) {
            detallePedidos.remove(dpE);
            System.out.println("Se elimino el producto del detalle de pedido");
        }
    }

    //Método para conseguir los detalles del pedido
    public static ArrayList<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    //Método para limpiar
    public static void limpiar() {
        detallePedidos.clear();
    }

}
