package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Dispositivo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DispositivoApi {
    String base = "api/dispositivo";

    @POST(base + "/saveDevice")
    Call<GenericResponse<Dispositivo>> registerDevice(@Body Dispositivo dispositivo);
}
