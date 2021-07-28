package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.activity.DetallePlatilloActivity;
import com.alexandertutoriales.cliente.ecommerce.activity.ListarPlatillosPorCategoriaActivity;
import com.alexandertutoriales.cliente.ecommerce.activity.ui.compras.DetalleMisComprasActivity;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Platillo;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class PlatillosRecomendadosAdapter extends RecyclerView.Adapter<PlatillosRecomendadosAdapter.ViewHolder> {
    private final Communication communication;
    private List<Platillo> platillos;

    public PlatillosRecomendadosAdapter(List<Platillo> platillos, Communication communication) {
        this.platillos = platillos;
        this.communication = communication;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setItem(this.platillos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.platillos.size();
    }

    public void updateItems(List<Platillo> platillo) {
        this.platillos.clear();
        this.platillos.addAll(platillo);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setItem(final Platillo p) {
            ImageView imgPlatillo = itemView.findViewById(R.id.imgPlatillo);
            TextView namePlatillo = itemView.findViewById(R.id.namePlatillo);
            Button btnOrdenar = itemView.findViewById(R.id.btnOrdenar);

            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + p.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .error(R.drawable.image_not_found)
                    .into(imgPlatillo);
            namePlatillo.setText(p.getNombre());
            btnOrdenar.setOnClickListener(v -> {
                Toast.makeText(this.itemView.getContext(), "Hola, hijo de puta", Toast.LENGTH_SHORT).show();
            });
            itemView.setOnClickListener(v -> {
                final Intent i = new Intent(itemView.getContext(), DetallePlatilloActivity.class);
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detallePlatillo", g.toJson(p));
                communication.showDetails(i);//Esto es solo para dar una animación.
            });
        }
    }
}
