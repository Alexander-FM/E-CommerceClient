package com.alexandertutoriales.cliente.ecommerce.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.appcompat.widget.Toolbar;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.BadgeDrawableCommunication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetallePlatilloActivity extends MenuBaseActivity implements BadgeDrawableCommunication {
    private ImageView imgPlatilloDetalle;
    private Button btnAgregarCarrito, btnComprar;
    private TextView tvNamePlatilloDetalle, tvPrecioPlatilloDetalle, tvDescripcionPlatilloDetalle;
    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();
    private Platillo platillo;
    private BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_platillo);
        init();
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
        this.imgPlatilloDetalle = findViewById(R.id.imgPlatilloDetalle);
        this.btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        this.btnComprar = findViewById(R.id.btnComprar);
        this.tvNamePlatilloDetalle = findViewById(R.id.tvNamePlatilloDetalle);
        this.tvPrecioPlatilloDetalle = findViewById(R.id.tvPrecioPlatilloDetalle);
        this.tvDescripcionPlatilloDetalle = findViewById(R.id.tvDescripcionPlatilloDetalle);
        badgeDrawable = BadgeDrawable.create(this);

    }

    private void loadData() {
        final String detalleString = this.getIntent().getStringExtra("detallePlatillo");
        if (detalleString != null) {
            platillo = g.fromJson(detalleString, Platillo.class);
            this.tvNamePlatilloDetalle.setText(platillo.getNombre());
            this.tvPrecioPlatilloDetalle.setText(String.format(Locale.ENGLISH, "S/%.2f", platillo.getPrecio()));
            this.tvDescripcionPlatilloDetalle.setText(platillo.getDescripcionPlatillo());
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + platillo.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(this.imgPlatilloDetalle);
        } else {
            System.out.println("Error al obtener los detalles dEl producto");
        }

        this.btnAgregarCarrito.setOnClickListener(v -> {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPlatillo(platillo);
            detallePedido.setCantidad(1);
            detallePedido.setPrecio(platillo.getPrecio());
            successMessage(Carrito.agregarPlatillos(detallePedido));
            updateBadge();
        });

        this.btnComprar.setOnClickListener(v -> {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPlatillo(platillo);
            detallePedido.setCantidad(1);
            detallePedido.setPrecio(platillo.getPrecio());
            successMessageWithConfirmation(Carrito.agregarPlatillos(detallePedido));
            updateBadge();
        });

    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText(R.string.aviso_sistema)
                .setContentText(message).show();
    }

    public void successMessageWithConfirmation(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(R.string.aviso_sistema)
                .setContentText(message).setConfirmText("¡Vamos!")
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    this.startActivity(new Intent(this, PlatillosCarritoActivity.class));
                }).show();
    }

    @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
    @Override
    public void updateBadge() {
        badgeDrawable.setNumber(Math.max(Carrito.getDetallePedidos().size(), 0));
        Log.d("Badge", "Número en el distintivo: " + badgeDrawable.getNumber());
        BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.toolbar), R.id.bolsaCompras);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }
}