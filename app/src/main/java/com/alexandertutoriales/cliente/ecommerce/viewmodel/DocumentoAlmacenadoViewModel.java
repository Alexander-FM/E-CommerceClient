package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DocumentoAlmacenado;
import com.alexandertutoriales.cliente.ecommerce.repository.DocumentoAlmacenadoRepository;

import org.jetbrains.annotations.NotNull;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentoAlmacenadoViewModel extends AndroidViewModel {
    private final DocumentoAlmacenadoRepository repository;

    public DocumentoAlmacenadoViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = DocumentoAlmacenadoRepository.getInstance();
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> save(MultipartBody.Part part, RequestBody requestBody) {
        return this.repository.savePhoto(part, requestBody);
    }
}
