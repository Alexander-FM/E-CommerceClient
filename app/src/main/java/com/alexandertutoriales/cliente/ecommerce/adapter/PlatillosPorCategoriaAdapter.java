package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.MostrarBadge;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class PlatillosPorCategoriaAdapter extends RecyclerView.Adapter<PlatillosPorCategoriaAdapter.ViewHolder> {
    private final MostrarBadge mostrarBadgeCommunication;
    private List<Platillo> listadoPlatillosPorCategoria;

    public PlatillosPorCategoriaAdapter(List<Platillo> listadoPlatillosPorCategoria, MostrarBadge mostrarBadgeCommunication) {
        this.listadoPlatillosPorCategoria = listadoPlatillosPorCategoria;
        this.mostrarBadgeCommunication = mostrarBadgeCommunication;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos_por_categoria,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setItem(this.listadoPlatillosPorCategoria.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listadoPlatillosPorCategoria.size();
    }

    public void updateItems(List<Platillo> platillosByCategoria) {
        this.listadoPlatillosPorCategoria.clear();
        this.listadoPlatillosPorCategoria.addAll(platillosByCategoria);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlatilloC;
        private final TextView namePlatilloC, txtPricePlatilloC;
        private final Button btnOrdenarPC;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.imgPlatilloC = itemView.findViewById(R.id.imgPlatilloC);
            this.namePlatilloC = itemView.findViewById(R.id.namePlatilloC);
            this.txtPricePlatilloC = itemView.findViewById(R.id.txtPricePlatilloC);
            this.btnOrdenarPC = itemView.findViewById(R.id.btnOrdenarPC);
        }

        public void setItem(final Platillo p) {
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + p.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) //No lo almacena el la caché ni en el disco
                    .error(R.drawable.image_not_found)
                    .into(imgPlatilloC);
            namePlatilloC.setText(p.getNombre());
            txtPricePlatilloC.setText(String.format(Locale.ENGLISH, "S/%.2f", p.getPrecio()));
            btnOrdenarPC.setOnClickListener(v -> {
                DetallePedido detallePedido = new DetallePedido();
                detallePedido.setPlatillo(p);
                detallePedido.setCantidad(1);
                detallePedido.setPrecio(p.getPrecio());
                mostrarBadgeCommunication.add(detallePedido);
            });
        }
    }

}
