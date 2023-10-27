package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Dispositivo;
import com.alexandertutoriales.cliente.ecommerce.repository.DispositivoRepository;

public class DispositivoViewModel extends AndroidViewModel {
    private final DispositivoRepository repository;

    public DispositivoViewModel(@NonNull Application application) {
        super(application);
        this.repository = DispositivoRepository.getInstance();
    }

    public LiveData<GenericResponse<Dispositivo>> registerDevice(Dispositivo dispositivo) {
        return repository.registerDevice(dispositivo);
    }
}
