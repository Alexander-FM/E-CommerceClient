package com.alexandertutoriales.cliente.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.alexandertutoriales.cliente.ecommerce.R;

public class MenuBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                this.logout();
                break;
            case R.id.bolsaCompras:
                this.showShoppingCart();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showShoppingCart() {
        Intent i = new Intent(this, PlatillosCarritoActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    protected void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        // Cierra todas las actividades y sale de la aplicación
        finishAffinity();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
}
