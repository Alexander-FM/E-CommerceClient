package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.OfertaProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OfertaProductoApi {
    String base = "api/oferta-producto";

    @GET(base + "/listar-ofertas-productos/{idOferta}")
    Call<GenericResponse<List<OfertaProducto>>> listarOfertasProductos(@Path("idOferta") int idOferta);
}
