package com.alexandertutoriales.cliente.ecommerce.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.api.DispositivoApi;
import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Dispositivo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DispositivoRepository {
    private final DispositivoApi api;
    private static DispositivoRepository repository;

    private DispositivoRepository() {
        this.api = ConfigApi.getDispositivoApi();
    }

    public static DispositivoRepository getInstance() {
        if (repository == null) {
            repository = new DispositivoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Dispositivo>> registerDevice(Dispositivo d) {
        final MutableLiveData<GenericResponse<Dispositivo>> mld = new MutableLiveData<>();
        this.api.registerDevice(d).enqueue(new Callback<GenericResponse<Dispositivo>>() {
            @Override
            public void onResponse(Call<GenericResponse<Dispositivo>> call, Response<GenericResponse<Dispositivo>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Dispositivo>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.err.println("Ocurrio un error al intentar registrar el dispositivo : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
