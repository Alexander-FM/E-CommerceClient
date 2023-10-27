package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Categoria;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Dispositivo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DispositivoApi {
    String base = "api/dispositivo";

    @POST(base + "/saveDevice")
    Call<GenericResponse<Dispositivo>> registerDevice(@Body Dispositivo dispositivo);
}
