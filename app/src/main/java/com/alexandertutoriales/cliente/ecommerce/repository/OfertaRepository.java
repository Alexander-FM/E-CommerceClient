package com.alexandertutoriales.cliente.ecommerce.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandertutoriales.cliente.ecommerce.api.OfertaApi;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Oferta;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfertaRepository {
    private final OfertaApi api;
    private static OfertaRepository repository;

    private OfertaRepository() {
        this.api = ConfigApi.getOfertaApi();
    }

    public static OfertaRepository getInstance() {
        if (repository == null) {
            repository = new OfertaRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<Oferta>>> listarOfertasActivas() {
        final MutableLiveData<GenericResponse<List<Oferta>>> mld = new MutableLiveData<>();
        this.api.listarOfertas().enqueue(new Callback<GenericResponse<List<Oferta>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Oferta>>> call, Response<GenericResponse<List<Oferta>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Oferta>>> call, Throwable t) {
                System.out.println("Error al obtener las ofertas: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
