package com.alexandertutoriales.cliente.ecommerce.activity.ui.inicio;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.CategoriaAdapter;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatillosRecomendadosAdapter;
import com.alexandertutoriales.cliente.ecommerce.adapter.SliderAdapter;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.entity.SliderItem;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Categoria;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.CategoriaViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PlatilloViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {
    private PlatilloViewModel platilloViewModel;
    private CategoriaViewModel categoriaViewModel;
    private CategoriaAdapter categoriaAdapter;
    private GridView gvCategorias;
    private RecyclerView rcvPlatillosRecomendados;
    private PlatillosRecomendadosAdapter adapter;
    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
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
        loadData(view);
    }

    private void init(View v) {
        ViewModelProvider vmp = new ViewModelProvider(this);
        rcvPlatillosRecomendados = v.findViewById(R.id.rcvPlatillosRecomendados);
        rcvPlatillosRecomendados.setLayoutManager(new GridLayoutManager(getContext(), 1));
        platilloViewModel = vmp.get(PlatilloViewModel.class);
        categoriaViewModel = vmp.get(CategoriaViewModel.class);
        svCarrusel = v.findViewById(R.id.svCarrusel);
        gvCategorias = v.findViewById(R.id.gvCategorias);
    }

    private void initAdapter() {
        adapter = new PlatillosRecomendadosAdapter(platillos);
        rcvPlatillosRecomendados.setAdapter(adapter);
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(3);
        svCarrusel.setAutoCycle(true);
        svCarrusel.startAutoCycle();
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.item_categorias, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);
    }

    private void loadData(View view) {
        platilloViewModel.listarPlatillosRecomendados().observe(getViewLifecycleOwner(), response -> {
            adapter.updateItems(response.getBody());
        });
        categoriaViewModel.listarCategorias().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            } else {
                System.out.println("Error al obtener las categorias activas");
            }

        });

        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.platillos_tipicos, "Los Mejores Platillos"));
        lista.add(new SliderItem(R.drawable.postres_ricos, "Los Mejores Postres Peruanos"));
        lista.add(new SliderItem(R.drawable.postres_muysabrosos, ""));
        lista.add(new SliderItem(R.drawable.peru_postres, ""));
        sliderAdapter.updateItem(lista);

    }


}