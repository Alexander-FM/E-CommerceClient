package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentoAlmacenadoApi {
    String baseUrl = "api/documento-almacenado";
    @Multipart
    @POST(baseUrl)
    Call<GenericResponse<DocumentoAlmacenado>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}
