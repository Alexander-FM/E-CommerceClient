package com.alexandertutoriales.cliente.ecommerce.activity.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatillosRecomendadosAdapter;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PlatilloViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {

    private PlatilloViewModel platilloViewModel;
    private RecyclerView rcvPlatillosRecomendados;
    private PlatillosRecomendadosAdapter adapter;
    private List<Platillo> platillos = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }

    private void init(View v){
        rcvPlatillosRecomendados = v.findViewById(R.id.rcvPlatillosRecomendados);
        rcvPlatillosRecomendados.setLayoutManager(new GridLayoutManager(getContext(), 1));
        platilloViewModel = new ViewModelProvider(this).get(PlatilloViewModel.class);
    }
    private void initAdapter() {
        adapter = new PlatillosRecomendadosAdapter(platillos);
        rcvPlatillosRecomendados.setAdapter(adapter);
    }
    private void loadData(){
        platilloViewModel.listarPlatillosRecomendados().observe(getViewLifecycleOwner(), response -> {
            adapter.updateItems(response.getBody());
        });
    }


}