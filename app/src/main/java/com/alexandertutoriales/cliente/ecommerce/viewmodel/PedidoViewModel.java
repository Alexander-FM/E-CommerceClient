package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.PedidoConDetallesDTO;
import com.alexandertutoriales.cliente.ecommerce.repository.PedidoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PedidoViewModel extends AndroidViewModel {

    private final PedidoRepository repository;

    public PedidoViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = PedidoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarComprasPorCliente(int idCli){
        return this.repository.listarPedidosPorCliente(idCli);
    }

}
