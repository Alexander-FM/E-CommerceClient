package com.alexandertutoriales.cliente.ecommerce.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.api.OfertaProductoApi;
import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.OfertaProducto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfertaProductoRepository {
    private final OfertaProductoApi api;
    private static OfertaProductoRepository repository;

    private OfertaProductoRepository() {
        this.api = ConfigApi.getOfertaProductoApi();
    }

    public static OfertaProductoRepository getInstance() {
        if (repository == null) {
            repository = new OfertaProductoRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<List<OfertaProducto>>> listarOfertasProducto(int idOferta) {
        final MutableLiveData<GenericResponse<List<OfertaProducto>>> mld = new MutableLiveData<>();
        this.api.listarOfertasProductos(idOferta).enqueue(new Callback<GenericResponse<List<OfertaProducto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<OfertaProducto>>> call, Response<GenericResponse<List<OfertaProducto>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<OfertaProducto>>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.err.println("Se ha producido un error al listar las ofertas-productos:" + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
