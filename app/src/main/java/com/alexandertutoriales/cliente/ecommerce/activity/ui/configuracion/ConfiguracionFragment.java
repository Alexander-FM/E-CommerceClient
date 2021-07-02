package com.alexandertutoriales.cliente.ecommerce.activity.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alexandertutoriales.cliente.ecommerce.R;


public class ConfiguracionFragment extends Fragment {

    public View onCreateView( LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracion, container, false);
    }
}