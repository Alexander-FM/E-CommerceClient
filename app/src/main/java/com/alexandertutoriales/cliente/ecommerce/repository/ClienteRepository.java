package com.alexandertutoriales.cliente.ecommerce.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandertutoriales.cliente.ecommerce.api.ClienteApi;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {
    private static ClienteRepository repository;
    private final ClienteApi api;

    public static ClienteRepository getInstance() {
        if (repository == null) {
            repository = new ClienteRepository();
        }
        return repository;
    }

    private ClienteRepository() {
        api = ConfigApi.getClienteApi();
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente u) {
        final MutableLiveData<GenericResponse<Cliente>> mld = new MutableLiveData<>();
        this.api.guardarCliente(u).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.err.println("Ocurrio un error al intentar registrarse : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
