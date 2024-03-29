package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.api.ConfigApi;
import com.alexandertutoriales.cliente.ecommerce.communication.CarritoComunication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DetallePedido;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PlatilloCarritoAdapter extends RecyclerView.Adapter<PlatilloCarritoAdapter.ViewHolder> {

    private final List<DetallePedido> detalles;
    private final CarritoComunication c;

    public PlatilloCarritoAdapter(List<DetallePedido> detalles, CarritoComunication c) {
        this.detalles = detalles;
        this.c = c;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_platillos_carrito, parent, false);
        return new ViewHolder(v, this.c);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        holder.setItem(this.detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return this.detalles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(ArrayList<DetallePedido> detallePedidos) {
        this.detalles.clear();
        this.detalles.addAll(detallePedidos);
        this.notifyDataSetChanged();

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlatilloDC, btnDecrease, btnAdd, btnEliminarPCarrito;
        private final EditText edtCantidad;
        private final TextView tvNombrePlatilloDC, tvPrecioPDC;
        private final CarritoComunication c;

        public ViewHolder(@NotNull View itemView, CarritoComunication c) {
            super(itemView);
            this.c = c;
            this.imgPlatilloDC = itemView.findViewById(R.id.imgPlatilloDC);
            this.btnEliminarPCarrito = itemView.findViewById(R.id.btnEliminarPCarrito);
            this.btnAdd = itemView.findViewById(R.id.btnAdd);
            this.btnDecrease = itemView.findViewById(R.id.btnDecrease);
            this.edtCantidad = itemView.findViewById(R.id.edtCantidad);
            this.tvNombrePlatilloDC = itemView.findViewById(R.id.tvNombrePlatilloDC);
            this.tvPrecioPDC = itemView.findViewById(R.id.tvPrecioPDC);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setItem(final DetallePedido dp) {
            this.tvNombrePlatilloDC.setText(dp.getPlatillo().getNombre());
            this.tvPrecioPDC.setText(String.format(Locale.ENGLISH, "S/%.2f", dp.getPrecio()));
            this.edtCantidad.setText(String.format(Locale.ENGLISH, "%d", dp.getCantidad()));
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + dp.getPlatillo().getFoto().getFileName();
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(this.imgPlatilloDC);

            //------------------Actualizar Cantidad del Carrito-------------------------------//
            /*Aumentar Cantidad*/
            btnAdd.setOnClickListener(v -> {
                if (dp.getCantidad() != dp.getPlatillo().getStock()) {
                    if (dp.getCantidad() != 10) {
                        dp.addOne();
                        PlatilloCarritoAdapter.this.notifyDataSetChanged();
                        c.mostrarTotalPagar(detalles);
                    } else {
                        toastWarning("No puedes llevar más de 10 productos");
                    }
                } else {
                    toastWarning("¡Ups! No hay stock suficiente");
                }
            });
            /*Disminuir Cantidad*/
            btnDecrease.setOnClickListener(v -> {
                if (dp.getCantidad() != 1) {
                    dp.removeOne();
                    PlatilloCarritoAdapter.this.notifyDataSetChanged();
                    c.mostrarTotalPagar(detalles);
                } else {
                    toastWarning("Mínimo puede llevar 1 producto");
                }
            });

            //-------------- Eliminar platillo de la bolsa de compras ---------------------//
            this.btnEliminarPCarrito.setOnClickListener(v -> {
                //2º Mostrarmos una alerta antes de eliminar el producto
                showMsg(dp.getPlatillo().getId());
            });
        }

        public void toastWarning(String texto) {
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            View layouView = layoutInflater.inflate(R.layout.custom_toast_warning, (ViewGroup) itemView.findViewById(R.id.layout_base_1));
            TextView textView = layouView.findViewById(R.id.textoinfo);
            textView.setText(texto);

            Toast toast = new Toast(itemView.getContext());
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layouView);
            toast.show();

        }

        private void showMsg(int idPlatillo) {
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Aviso del sistema !")
                    .setContentText("¿Estás seguro de eliminar el producto de tu bolsa de compras?")
                    .setCancelText("No, Cancelar!").setConfirmText("Sí, Confirmar")
                    .showCancelButton(true).setCancelClickListener(sDialog -> {
                sDialog.dismissWithAnimation();
                new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                        .setContentText("No eliminaste ningún producto de tu bolsa de compras")
                        .show();
            }).setConfirmClickListener(sweetAlertDialog -> {
                c.eliminarDetalle(idPlatillo);
                sweetAlertDialog.dismissWithAnimation();
                new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Aviso del sistema !")
                        .setContentText("Excelente, el producto acaba de ser eliminado de tu bolsa de compras")
                        .show();
            }).show();
        }
    }
}
