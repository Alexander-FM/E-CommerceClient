package com.alexandertutoriales.cliente.ecommerce.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatillosPorCategoriaAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.BadgeDrawableCommunication;
import com.alexandertutoriales.cliente.ecommerce.communication.MostrarBadge;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PlatilloViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListarPlatillosPorCategoriaActivity extends MenuBaseActivity implements MostrarBadge, BadgeDrawableCommunication {
    private PlatilloViewModel platilloViewModel;
    private PlatillosPorCategoriaAdapter adapter;
    private List<Platillo> platillos = new ArrayList<>();
    private RecyclerView rcvPlatillosPorCategoria;
    private BadgeDrawable badgeDrawable;

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

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        badgeDrawable = BadgeDrawable.create(this);
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.platilloViewModel = vmp.get(PlatilloViewModel.class);
    }

    private void initAdapter() {
        adapter = new PlatillosPorCategoriaAdapter(platillos, this, this);
        rcvPlatillosPorCategoria = findViewById(R.id.rcvPlatillosPorCategoria);
        rcvPlatillosPorCategoria.setAdapter(adapter);
        rcvPlatillosPorCategoria.setLayoutManager(new LinearLayoutManager(this));
        //Si quieres hacer un lista horizontal comentar la linea 55 y descomenta la linea 57
        //rcvPlatillosPorCategoria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadData() {
        int idC = getIntent().getIntExtra("idC", 0);//Recibimos el idCategoría.
        platilloViewModel.listarPlatillosPorCategoria(idC).observe(this, response -> {
            adapter.updateItems(response.getBody());
            updateBadge();
        });
    }

    @SuppressLint({"UnsafeOptInUsageError"})
    @Override
    public void add(DetallePedido dp) {
        successMessage(Carrito.agregarPlatillos(dp));
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Aviso del sistema!")
                .setContentText(message).show();
    }

    @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
    @Override
    public void updateBadge() {
        badgeDrawable.setNumber(Math.max(Carrito.getDetallePedidos().size(), 0));
        BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.toolbar), R.id.bolsaCompras);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }
}