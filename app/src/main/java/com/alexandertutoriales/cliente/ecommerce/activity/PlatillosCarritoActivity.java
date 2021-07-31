package com.alexandertutoriales.cliente.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatilloCarritoAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.CarritoComunication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Carrito;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;

import java.util.List;

public class PlatillosCarritoActivity extends AppCompatActivity implements CarritoComunication {

    private PlatilloCarritoAdapter adapter;
    private Button btnFinalizarCompra;
    private RecyclerView rcvBolsaCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platillos_carrito);
        init();
        initViewModel();
        initAdapter();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        rcvBolsaCompras = findViewById(R.id.rcvBolsaCompras);
    }

    private void initViewModel() {
    }

    private void initAdapter() {
        adapter = new PlatilloCarritoAdapter(Carrito.getDetallePedidos(), this);
        rcvBolsaCompras.setLayoutManager(new LinearLayoutManager(this));
        rcvBolsaCompras.setAdapter(adapter);
    }

    private double getTotalV(List<DetallePedido> detalles) {
        float total = 0;
        for (DetallePedido dp : detalles) {
            total += dp.getTotal();
        }
        return total;
    }

    @Override
    public void eliminarDetalle(int idP) {
        Carrito.eliminar(idP);
        this.adapter.notifyDataSetChanged();
    }
}