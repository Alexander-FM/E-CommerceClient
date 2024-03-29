package com.alexandertutoriales.cliente.ecommerce.activity.ui.inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.adapter.CategoriaAdapter;
import com.alexandertutoriales.cliente.ecommerce.adapter.OfertasAdapter;
import com.alexandertutoriales.cliente.ecommerce.adapter.PlatillosRecomendadosAdapter;
import com.alexandertutoriales.cliente.ecommerce.adapter.SliderAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.BadgeDrawableCommunication;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.communication.MostrarBadge;
import com.alexandertutoriales.cliente.ecommerce.entity.SliderItem;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Oferta;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.utils.Carrito;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.CategoriaViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.OfertaViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PlatilloViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class InicioFragment extends Fragment implements Communication, MostrarBadge, BadgeDrawableCommunication {
    private PlatilloViewModel platilloViewModel;
    private CategoriaViewModel categoriaViewModel;
    private OfertaViewModel ofertaViewModel;
    private CategoriaAdapter categoriaAdapter;
    private OfertasAdapter ofertasAdapter;
    private GridView gvCategorias;
    private RecyclerView rcvPlatillosRecomendados;
    private RecyclerView rcvOfertas;
    private PlatillosRecomendadosAdapter adapter;
    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    private final List<Platillo> platillos = new ArrayList<>();
    private final List<Oferta> ofertas = new ArrayList<>();
    private LinearLayout llCategorias;
    private SwipeRefreshLayout swipeFragmentInicio;
    private BadgeDrawable badgeDrawable;
    private TextView tvOfertas;
    private ImageCarousel carruselOfertas;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }

    private void init(View v) {
        ViewModelProvider vmp = new ViewModelProvider(this);
        rcvPlatillosRecomendados = v.findViewById(R.id.rcvPlatillosRecomendados);
        rcvPlatillosRecomendados.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvOfertas = v.findViewById(R.id.rcvOfertas);
        //rcvOfertas.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvOfertas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        platilloViewModel = vmp.get(PlatilloViewModel.class);
        categoriaViewModel = vmp.get(CategoriaViewModel.class);
        ofertaViewModel = vmp.get(OfertaViewModel.class);
        svCarrusel = v.findViewById(R.id.svCarrusel);
        gvCategorias = v.findViewById(R.id.gvCategorias);
        llCategorias = v.findViewById(R.id.llCategorias);
        TypedValue tv = new TypedValue();
        swipeFragmentInicio = v.findViewById(R.id.swipeFragmentInicio);
        requireContext().getTheme().resolveAttribute(R.color.pink_700, tv, false);
        swipeFragmentInicio.setColorSchemeColors(tv.data);
        swipeFragmentInicio.setOnRefreshListener(this::loadData);
        badgeDrawable = BadgeDrawable.create(this.getContext());
        tvOfertas = v.findViewById(R.id.tvOfertas);
        carruselOfertas = v.findViewById(R.id.carruselOfertas);
    }

    private void initAdapter() {
        adapter = new PlatillosRecomendadosAdapter(platillos, this, this, this);
        rcvPlatillosRecomendados.setAdapter(adapter);
        ofertasAdapter = new OfertasAdapter(ofertas, this);
        rcvOfertas.setAdapter(ofertasAdapter);
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

    private void loadData() {
        swipeFragmentInicio.setRefreshing(true);
        ofertaViewModel.listarOfertasActivas().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                ofertasAdapter.updateItems(response.getBody());
                tvOfertas.setVisibility(View.VISIBLE);
            } else {
                ofertasAdapter.updateItems(response.getBody());
                tvOfertas.setVisibility(View.GONE);
            }
            swipeFragmentInicio.setRefreshing(false);
        });
        categoriaViewModel.listarCategorias().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
                int numColumnsCategorias = categoriaAdapter.getCount();
                gvCategorias.setNumColumns(numColumnsCategorias);
                int dynamicWidth = 285 * numColumnsCategorias;
                ViewGroup.LayoutParams params = llCategorias.getLayoutParams();
                params.width = dynamicWidth;
                llCategorias.setLayoutParams(params);
            } else {
                System.out.println("Error al obtener las categorias activas");
            }
            swipeFragmentInicio.setRefreshing(false);
        });
        platilloViewModel.listarPlatillosRecomendados().observe(getViewLifecycleOwner(), response ->
                adapter.updateItems(response.getBody()));

        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.bisuteria, "El complemento perfecto para ella"));
        lista.add(new SliderItem(R.drawable.relojes_ellas, "Relojes para ellas"));
        lista.add(new SliderItem(R.drawable.anillos, "Los mejores anillos"));
        lista.add(new SliderItem(R.drawable.bisuteria2, "El complemento perfecto para ella"));
        lista.add(new SliderItem(R.drawable.anillos2, "Revisa nuestras ofertas en anillos"));
        sliderAdapter.updateItem(lista);

        carruselOfertas.registerLifecycle(getViewLifecycleOwner().getLifecycle());
        List<CarouselItem> list = new ArrayList<>();
        list.add(new CarouselItem(R.drawable.bisuteriahombres, "El complemento perfecto para ellos"));
        list.add(new CarouselItem(R.drawable.relojes_ellos, "Relojes para ellos"));
        list.add(new CarouselItem(R.drawable.bisuteriahombres2, "Pulsera para ellos"));
        list.add(new CarouselItem(R.drawable.bisuteriahombres3, "Brazalelete de oro para él"));
        list.add(new CarouselItem(R.drawable.bisuteriahombre4, "Todo lo que él necesita está aquí"));
        carruselOfertas.setData(list);

    }


    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void exportInvoice(int idCli, int idOrden, String fileName) {

    }

    @Override
    public void add(DetallePedido dp) {
        successMessage(Carrito.agregarPlatillos(dp));
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this.getContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Aviso del sistema!")
                .setContentText(message).show();
    }

    @OptIn(markerClass = com.google.android.material.badge.ExperimentalBadgeUtils.class)
    @Override
    public void updateBadge() {
        badgeDrawable.setNumber(Math.max(Carrito.getDetallePedidos().size(), 0));
        BadgeUtils.attachBadgeDrawable(badgeDrawable, getActivity().findViewById(R.id.toolbar), R.id.bolsaCompras);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBadge();
    }
}