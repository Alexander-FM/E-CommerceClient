package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.activity.DetallePlatilloActivity;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.communication.MostrarBadge;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.OfertaProducto;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OfertaProductosAdapter extends RecyclerView.Adapter<OfertaProductosAdapter.ViewHolder> implements Filterable {
    private final Communication communication;
    private final MostrarBadge mostrarBadgeComunication;
    private final List<OfertaProducto> ofertaProductos;
    private List<OfertaProducto> ofertaProductosFiltrados = new ArrayList<>();

    public OfertaProductosAdapter(List<OfertaProducto> ofertaProductos, Communication communication, MostrarBadge mostrarBadgeComunication) {
        this.ofertaProductos = ofertaProductos;
        this.communication = communication;
        this.mostrarBadgeComunication = mostrarBadgeComunication;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ofertas_productos, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        holder.setItem(this.ofertaProductosFiltrados.get(position));
    }

    @Override
    public int getItemCount() {
        return this.ofertaProductosFiltrados.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<OfertaProducto> producto) {
        this.ofertaProductos.clear();
        this.ofertaProductos.addAll(producto);

        this.ofertaProductosFiltrados.clear();
        this.ofertaProductosFiltrados.addAll(producto);
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSearch = constraint != null ? constraint.toString() : "";
                List<OfertaProducto> resultList;

                if (charSearch.isEmpty()) {
                    ofertaProductosFiltrados = ofertaProductos;
                } else {
                    resultList = new ArrayList<>();
                    for (OfertaProducto p : ofertaProductos) {
                        if (p.getIdPlatillo().getNombre().toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(p);
                        }
                    }
                    ofertaProductosFiltrados = resultList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ofertaProductosFiltrados;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.values != null) {
                    ofertaProductosFiltrados = (ArrayList<OfertaProducto>) results.values;
                } else {
                    ofertaProductosFiltrados = new ArrayList<>();
                }
                notifyDataSetChanged();
            }
        };
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
        }

        public void setItem(final OfertaProducto p) {
            ImageView imgProductoOferta = itemView.findViewById(R.id.imgProductoOferta);
            TextView tvProductoOferta = itemView.findViewById(R.id.tvProductoOferta);
            TextView tvPrecioOferta = itemView.findViewById(R.id.tvPrecioOferta);
            TextView tvDescuento = itemView.findViewById(R.id.tvDescuento);
            TextView tvPrecioAntes = itemView.findViewById(R.id.tvPrecioAntes);
            Button btnComprarProductoOferta = itemView.findViewById(R.id.btnComprarProductoOferta);

            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + p.getIdPlatillo().getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .error(R.drawable.image_not_found)
                    .into(imgProductoOferta);
            tvProductoOferta.setText(p.getIdPlatillo().getNombre());
            tvPrecioOferta.setText(String.format(Locale.ENGLISH, "S/%.2f", p.getPrecioAhora()));
            //tvDescuento.setText(Integer.toString(p.getDescuento()).concat("%"));
            tvDescuento.setText(String.format(Locale.ENGLISH, "%d%%", p.getDescuento()));
            tvPrecioAntes.setText(String.format(Locale.ENGLISH, "S/%.2f", p.getIdPlatillo().getPrecio()));

            btnComprarProductoOferta.setOnClickListener(v -> {
                DetallePedido detallePedido = new DetallePedido();
                detallePedido.setPlatillo(p.getIdPlatillo());
                detallePedido.setCantidad(1);
                detallePedido.setPrecio(p.getPrecioAhora());
                mostrarBadgeComunication.add(detallePedido);
            });
            //Cuando pulses sobre la tarjeta te aparecerá el detalle de ese producto
            itemView.setOnClickListener(v -> {
                final Intent i = new Intent(itemView.getContext(), DetallePlatilloActivity.class);
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detallePlatillo", g.toJson(p.getIdPlatillo()));
                communication.showDetails(i);//Esto es solo para dar una animación.
            });
        }
    }

}
