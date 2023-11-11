package com.alexandertutoriales.cliente.ecommerce.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.databinding.ActivityInicioBinding;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;

import de.hdodenhof.circleimageview.CircleImageView;


public class InicioActivity extends MenuBaseActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;
    private String usuarioJson = null;
    private NavController navController;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData().getBooleanExtra("redirectToMisCompras", false)) {
                    navController.navigate(R.id.nav_mis_compras);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInicio.toolbar);
        binding.appBarInicio.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_mis_compras, R.id.nav_configuracion)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void hideItemsNavigationView(NavigationView navigationView, Usuario usuario) {
        Menu nav_menu = navigationView.getMenu();
        nav_menu.findItem(R.id.titleAdmin).setVisible(false);
        System.out.println(usuario.getId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }
    @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
    @SuppressLint("UnsafeExperimentalUsageError")
    private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            final View vistaheader = binding.navView.getHeaderView(0);
            final TextView tvNombre = vistaheader.findViewById(R.id.tvNombre), tvCorreo = vistaheader.findViewById(R.id.tvCorreo);
            final CircleImageView imgFoto = vistaheader.findViewById(R.id.imgFotoPerfil);
            tvNombre.setText(u.getCliente().getNombreCompleto());
            tvCorreo.setText(u.getEmail());
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + u.getCliente().getFoto().getFileName();
            final Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.drawable.foto_rota)
                    .into(imgFoto);
            hideItemsNavigationView(binding.navView, u);
        }
        BadgeDrawable badgeDrawable = BadgeDrawable.create(this);
        badgeDrawable.setNumber(Carrito.getDetallePedidos().size());
        BadgeUtils.attachBadgeDrawable(badgeDrawable, findViewById(R.id.toolbar), R.id.bolsaCompras);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
    }
}