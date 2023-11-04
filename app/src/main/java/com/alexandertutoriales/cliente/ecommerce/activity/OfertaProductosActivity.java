package com.alexandertutoriales.cliente.ecommerce.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.OfertaProductosAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.communication.MostrarBadge;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.OfertaProductoViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OfertaProductosActivity extends AppCompatActivity implements Communication, MostrarBadge {
    private OfertaProductoViewModel ofertaProductoViewModel;
    private OfertaProductosAdapter ofertaProductosAdapter;
    private RecyclerView rcvOfertaProductos;
    private SearchView searchView;
    private BadgeDrawable badgeDrawable;
    private SwipeRefreshLayout swipeOfertaProductosActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta_productos);
        init();
        initViewModel();
        initAdapter();
        loadData();
        filterProducts();
    }

    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        rcvOfertaProductos = findViewById(R.id.rcvOfertaProductos);
        rcvOfertaProductos.setLayoutManager(new GridLayoutManager(this, 2));
        TypedValue tv = new TypedValue();
        swipeOfertaProductosActivity = findViewById(R.id.swipeOfertaProductosActivity);
        getApplicationContext().getTheme().resolveAttribute(R.color.pink_700, tv, false);
        swipeOfertaProductosActivity.setColorSchemeColors(tv.data);
        swipeOfertaProductosActivity.setOnRefreshListener(this::loadData);
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.ofertaProductoViewModel = vmp.get(OfertaProductoViewModel.class);
    }

    private void initAdapter() {
        ofertaProductosAdapter = new OfertaProductosAdapter(new ArrayList<>(), this, this);
        rcvOfertaProductos.setAdapter(ofertaProductosAdapter);
    }

    private void loadData() {
        swipeOfertaProductosActivity.setRefreshing(true);
        int idOferta = getIntent().getIntExtra("idOferta", 0);//Recibimos el idOferta.
        ofertaProductoViewModel.listarOfertasProductos(idOferta).observe(this, response -> {
            ofertaProductosAdapter.updateItems(response.getBody());
            swipeOfertaProductosActivity.setRefreshing(false);
        });
    }

    private void filterProducts() {
        searchView = findViewById(R.id.textinputSearch);
        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        if (closeButton != null) {
            closeButton.setOnClickListener(v -> {
                ofertaProductosAdapter.getFilter().filter("");
                searchView.setQuery("", false);
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                ofertaProductosAdapter.getFilter().filter(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                ofertaProductosAdapter.getFilter().filter(text);
                return false;
            }
        });
    }

    @Override
    public void showDetails(Intent i) {
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void exportInvoice(int idCli, int idOrden, String fileName) {

    }

    @SuppressLint({"UnsafeOptInUsageError"})
    @Override
    public void add(DetallePedido dp) {
        successMessage(Carrito.agregarPlatillos(dp));
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setNumber(Carrito.getDetallePedidos().size());
        BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.toolbar), R.id.bolsaCompras);
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Aviso del sistema!")
                .setContentText(message).show();
    }
}