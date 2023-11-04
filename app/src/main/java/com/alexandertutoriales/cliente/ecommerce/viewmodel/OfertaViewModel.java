package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Oferta;
import com.alexandertutoriales.cliente.ecommerce.repository.OfertaRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfertaViewModel extends AndroidViewModel {
    private final OfertaRepository repository;

    public OfertaViewModel(@NotNull Application application) {
        super(application);
        this.repository = OfertaRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Oferta>>> listarOfertasActivas() {
        return this.repository.listarOfertasActivas();
    }
}
