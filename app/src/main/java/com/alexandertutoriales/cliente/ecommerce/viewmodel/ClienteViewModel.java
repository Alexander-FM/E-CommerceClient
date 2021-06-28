package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Cliente;
import com.alexandertutoriales.cliente.ecommerce.repository.ClienteRepository;

import org.jetbrains.annotations.NotNull;

public class ClienteViewModel extends AndroidViewModel {
    private final ClienteRepository repository;

    public ClienteViewModel(@NonNull @NotNull Application application, ClienteRepository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente c) {
        return repository.guardarCliente(c);
    }
}
