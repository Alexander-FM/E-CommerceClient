package com.alexandertutoriales.cliente.ecommerce.activity.ui.compras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alexandertutoriales.cliente.ecommerce.R;


public class MisComprasFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_compras, container, false);
    }
}