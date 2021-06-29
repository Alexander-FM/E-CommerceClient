package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Cliente;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ClienteApi {
    //RUTA DEL SERVICIO
    String base = "api/cliente";
    //RUTA DEL SERVICIO + LA RUTA DEL MÉTODO
    @POST(base)
    Call<GenericResponse<Cliente>> guardarCliente(@Body Cliente c);

}
