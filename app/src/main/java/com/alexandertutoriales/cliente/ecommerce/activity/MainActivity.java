package com.alexandertutoriales.cliente.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.UsuarioViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private EditText edtMail, edtPassword;
    private Button btnIniciarSesion;
    private UsuarioViewModel viewModel;
    private TextView txtRegistrarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();
    }

    private void init() {
        txtRegistrarUsuario = findViewById(R.id.txtRegistrarUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        edtMail = findViewById(R.id.edtMail);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(v -> {
            viewModel.login(edtMail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
                if(response.getRpta() == 1){
                    toastCorrecto(response.getMessage());
                    Usuario u = response.getBody();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    final Gson g = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {
                    }.getType()));
                    editor.apply();
                    edtMail.setText("");
                    edtPassword.setText("");
                    startActivity(new Intent(this, InicioActivity.class));
                }else{
                    toastIncorrecto(response.getMessage());
                }
            });
        });
        txtRegistrarUsuario.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarUsuarioActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
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
    //DETECTAR SI HAY UNA SESION ACTIVA
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson", "");
        if (!pref.equals("")) {
            toastCorrecto("Se detectó una sesion activa, el login será omitido!");
            //Toast.makeText(this, "se detectó una sesion activa,login omitido", Toast.LENGTH_SHORT).show();
            this.startActivity(new Intent(this, InicioActivity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }
    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("has oprimido el botón atrás")
                .setContentText("¿Quieres cerrar la aplicación?")
                .setCancelText("No, Cancelar!").setConfirmText("Sí,cerrar sesión")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
            sDialog.dismissWithAnimation();
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                    .setContentText("No saliste de la app")
                    .show();
        }).setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            System.exit(0);
        }).show();
    }
}