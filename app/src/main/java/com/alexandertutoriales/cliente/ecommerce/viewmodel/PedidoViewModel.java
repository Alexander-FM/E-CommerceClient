package com.alexandertutoriales.cliente.ecommerce.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexandertutoriales.cliente.ecommerce.entity.GenericResponse;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Pedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.GenerarPedidoDTO;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.PedidoConDetallesDTO;
import com.alexandertutoriales.cliente.ecommerce.repository.PedidoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;

public class PedidoViewModel extends AndroidViewModel {

    private final PedidoRepository repository;

    public PedidoViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = PedidoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarComprasPorCliente(int idCli) {
        return this.repository.listarPedidosPorCliente(idCli);
    }

    public LiveData<GenericResponse<GenerarPedidoDTO>> guardarPedido(GenerarPedidoDTO dto) {
        return repository.save(dto);
    }

    public LiveData<GenericResponse<Pedido>> anularPedido(int id){
        return repository.anularPedido(id);
    }

    /**
     * export complaint
     * @param idCli
     * @param idOrden
     * @return a file pdf
     */
    public LiveData<GenericResponse<ResponseBody>> exportComplaint(int idCli, int idOrden){
        return repository.exportComplaint(idCli, idOrden);
    }

}
