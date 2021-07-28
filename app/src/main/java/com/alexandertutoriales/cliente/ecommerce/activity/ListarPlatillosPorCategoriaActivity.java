package com.alexandertutoriales.cliente.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatillosPorCategoriaAdapter;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.ClienteViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PlatilloViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListarPlatillosPorCategoriaActivity extends AppCompatActivity {
    private PlatilloViewModel platilloViewModel;
    private PlatillosPorCategoriaAdapter adapter;
    private RecyclerView rcvPlatillosPorCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_platillos_por_categoria);
        /*Toast.makeText(this, "idCategoria: " + getIntent().getIntExtra("idC", 0), Toast.LENGTH_SHORT).show();*/
        init();
        initViewModel();
        initAdapter();
        loadData();
    }
    private void init(){
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        rcvPlatillosPorCategoria = findViewById(R.id.rcvPlatillosPorCategoria);
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.platilloViewModel = vmp.get(PlatilloViewModel.class);
    }

    private void initAdapter() {
        adapter = new PlatillosPorCategoriaAdapter(new ArrayList<>());
        rcvPlatillosPorCategoria.setAdapter(adapter);
    }

    private void loadData() {
        int idC = getIntent().getIntExtra("idC", 0);//Recibimos el idCategoría.
        platilloViewModel.listarPlatillosPorCategoria(idC).observe(this, response -> {
            adapter.updateItems(response.getBody());
        });
    }
}