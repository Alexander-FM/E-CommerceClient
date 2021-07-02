package com.alexandertutoriales.cliente.ecommerce.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.api.DocumentoAlmacenadoApi;
import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentoAlmacenadoRepository {
    private final DocumentoAlmacenadoApi api;
    private static DocumentoAlmacenadoRepository repository;

    private DocumentoAlmacenadoRepository() {
        this.api = ConfigApi.getDocumentoAlmacenadoApi();
    }

    public static DocumentoAlmacenadoRepository getInstance() {
        if (repository == null) {
            repository = new DocumentoAlmacenadoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> savePhoto(MultipartBody.Part part, RequestBody requestBody) {
        final MutableLiveData<GenericResponse<DocumentoAlmacenado>> mld = new MutableLiveData();
        this.api.save(part, requestBody).enqueue(new Callback<GenericResponse<DocumentoAlmacenado>>() {
            @Override
            public void onResponse(Call<GenericResponse<DocumentoAlmacenado>> call, Response<GenericResponse<DocumentoAlmacenado>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<DocumentoAlmacenado>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.err.println("Ocurrio un error al intentar guardar la foto del usuario : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
