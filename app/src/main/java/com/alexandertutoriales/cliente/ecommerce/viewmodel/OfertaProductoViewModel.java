package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.OfertaProducto;
import com.alexandertutoriales.cliente.ecommerce.repository.OfertaProductoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfertaProductoViewModel extends AndroidViewModel {

    private final OfertaProductoRepository repository;

    public OfertaProductoViewModel(@NotNull Application application) {
        super(application);
        this.repository = OfertaProductoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<OfertaProducto>>> listarOfertasProductos(int idOferta) {
        return this.repository.listarOfertasProducto(idOferta);
    }

}
