package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Categoria;
import com.alexandertutoriales.cliente.ecommerce.repository.CategoriaRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriaViewModel extends AndroidViewModel {
    private final CategoriaRepository repository;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.repository = CategoriaRepository.getInstance();
    }
    public LiveData<GenericResponse<List<Categoria>>> listarCategorias(){
        return this.repository.listarCategoriasActivas();
    }
}
