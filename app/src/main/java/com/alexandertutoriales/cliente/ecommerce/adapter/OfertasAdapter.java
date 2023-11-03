package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.activity.ListarPlatillosPorCategoriaActivity;
import com.alexandertutoriales.cliente.ecommerce.activity.OfertaProductosActivity;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Oferta;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OfertasAdapter extends RecyclerView.Adapter<OfertasAdapter.ViewHolder> {
    private List<Oferta> listaOfertas;
    private final Communication communication;

    public OfertasAdapter(List<Oferta> listaOfertas, Communication communication) {
        this.listaOfertas = listaOfertas;
        this.communication = communication;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ofertas,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setItem(this.listaOfertas.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listaOfertas.size();
    }

    public void updateItems(List<Oferta> ofertas) {
        this.listaOfertas.clear();
        this.listaOfertas.addAll(ofertas);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView banner;
        private final TextView descripcionOferta;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.banner = itemView.findViewById(R.id.imageOferta);
            this.descripcionOferta = itemView.findViewById(R.id.descripcionOferta);
        }

        public void setItem(final Oferta o) {
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + o.getBanner().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) //No lo almacena el la caché ni en el disco
                    .error(R.drawable.image_not_found)
                    .into(banner);
            descripcionOferta.setText(o.getDescripcionOferta());
            itemView.setOnClickListener(v -> {
                Intent i = new Intent(itemView.getContext(), OfertaProductosActivity.class);
                i.putExtra("idOferta", o.getId());//Obtenemos el idOferta
                communication.showDetails(i);// Lanzamos una animación
            });
        }
    }

}
