package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.repository.PlatilloRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlatilloViewModel extends AndroidViewModel {
    private final PlatilloRepository repository;

    public PlatilloViewModel(@NotNull Application application) {
        super(application);
        repository = PlatilloRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Platillo>>> listarPlatillosRecomendados() {
        return this.repository.listarPlatillosRecomendados();
    }

    public LiveData<GenericResponse<List<Platillo>>> listarPlatillosPorCategoria(int idC) {
        return this.repository.listarPlatillosPorCategoria(idC);
    }
}
