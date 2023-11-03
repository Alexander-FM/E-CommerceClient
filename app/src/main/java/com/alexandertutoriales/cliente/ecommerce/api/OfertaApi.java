package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Dispositivo;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Oferta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface OfertaApi {
    String base = "api/oferta";

    @GET(base + "/listar-ofertas")
    Call<GenericResponse<List<Oferta>>> listarOfertas();
}
