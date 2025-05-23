package com.alexandertutoriales.cliente.ecommerce.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatilloCarritoAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.CarritoComunication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.GenerarPedidoDTO;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PedidoViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlatillosCarritoActivity extends AppCompatActivity implements CarritoComunication {
    private PedidoViewModel pedidoViewModel;
    private PlatilloCarritoAdapter adapter;
    private Button btnFinalizarCompra;
    private RecyclerView rcvBolsaCompras;
    private TextView tvMontoTotal;
    private ProgressBar progressBar;
    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

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
            Intent i = new Intent();
            i.putExtra("redirectToMisCompras", false);
            setResult(Activity.RESULT_OK, i);
            finish();
            overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        rcvBolsaCompras = findViewById(R.id.rcvBolsaCompras);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        tvMontoTotal = findViewById(R.id.tvMontoTotal);
        progressBar = findViewById(R.id.progress_horizontal);
        mostrarTotalPagar(Carrito.getDetallePedidos());
        btnFinalizarCompra.setOnClickListener(v -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pref = preferences.getString("UsuarioJson", "");
            Usuario u = g.fromJson(pref, Usuario.class);
            int idC = u.getCliente().getId();
            if (idC != 0) {
                if (Carrito.getDetallePedidos().isEmpty()) {
                    toastIncorrecto("¡Ups!, La bolsa de compras está vacia.");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registrarPedido(idC);
                }
            } else {
                toastIncorrecto("No ha iniciado sesión, se le redirigirá al login");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.pedidoViewModel = vmp.get(PedidoViewModel.class);
    }

    private void initAdapter() {
        adapter = new PlatilloCarritoAdapter(Carrito.getDetallePedidos(), this);
        rcvBolsaCompras.setLayoutManager(new LinearLayoutManager(this));
        rcvBolsaCompras.setAdapter(adapter);
    }

    private void registrarPedido(int idC) {
        toastCorrecto("Procesando pedido...");
        btnFinalizarCompra.setEnabled(false);
        btnFinalizarCompra.setAlpha(0.3f);
        ArrayList<DetallePedido> detallePedidos = Carrito.getDetallePedidos();
        GenerarPedidoDTO dto = new GenerarPedidoDTO();
        java.util.Date date = new java.util.Date();
        dto.getPedido().setFechaCompra(new Date(date.getTime()));
        dto.getPedido().setAnularPedido(false);
        dto.getPedido().setMonto(detallePedidos.stream().mapToDouble(dp ->
                dp.getPlatillo().getPrecio() * dp.getCantidad()).sum());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String usuarioJson = sp.getString("UsuarioJson", null);
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            dto.getPedido().setCliente(u.getCliente());
        } else {
            dto.getPedido().getCliente().setId(idC);
        }
        dto.setDetallePedido(detallePedidos);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> toastCorrecto("¡Aguarda...! Estamos enviando la factura a tu correo electrónico. La paciencia siempre trae recompensas."), 4000); // 4000 milisegundos = 4 segundos
        handler.postDelayed(() -> toastCorrecto("La paciencia es una virtud, y tú estás a punto de ser recompensado. ¡Mantén la esperanza!"), 2000); // 3000 milisegundos = 2 segundos
        handler.postDelayed(() -> toastCorrecto("¡Espera un momento más! Grandes cosas están en camino, y lo mejor siempre tarda un poco más"), 1000); // 1000 milisegundos =  segundo
        this.pedidoViewModel.guardarPedido(dto).observe(this, response -> {
            toastCorrecto("Gracias por la espera. La factura ha sido enviada a tu correo electrónico");
            if (response.getRpta() == 1) {
                progressBar.setVisibility(View.GONE);
                toastCorrecto("Compra registrada con éxito");
                Carrito.limpiar();
                Intent i = new Intent();
                i.putExtra("redirectToMisCompras", true);
                setResult(Activity.RESULT_OK, i);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            } else {
                toastIncorrecto("Demonios!, No se pudo registrar el pedido");
            }
        });
    }

    @Override
    public void eliminarDetalle(int idP) {
        Carrito.eliminar(idP);
        mostrarTotalPagar(Carrito.getDetallePedidos());
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void mostrarTotalPagar(List<DetallePedido> detallePedido) {
        Double montoPagar = detallePedido.stream().mapToDouble(dp -> dp.getPlatillo().getPrecio() * dp.getCantidad()).sum();
        Locale locale = new Locale("es", "PE"); // Especifica el idioma y país según tu preferencia
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formattedTotal = currencyFormatter.format(montoPagar);
        tvMontoTotal.setText(formattedTotal);
    }

    public void toastIncorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layout_base_1));
        TextView textView = layouView.findViewById(R.id.textoinfo);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    public void toastCorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_check, (ViewGroup) findViewById(R.id.layout_base_2));
        TextView textView = layouView.findViewById(R.id.textoinfo2);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();
    }
}