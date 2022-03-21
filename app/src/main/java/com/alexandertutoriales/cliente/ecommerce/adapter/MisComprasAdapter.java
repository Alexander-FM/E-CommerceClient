package com.alexandertutoriales.cliente.ecommerce.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.activity.ui.compras.DetalleMisComprasActivity;
import com.alexandertutoriales.cliente.ecommerce.communication.AnularPedidoComunication;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.PedidoConDetallesDTO;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {
    private final AnularPedidoComunication anularPedidoComunication;
    private final Communication communication;
    private final List<PedidoConDetallesDTO> pedidos;

    public MisComprasAdapter(List<PedidoConDetallesDTO> pedidos, Communication communication, AnularPedidoComunication anularPedidoComunication) {
        this.pedidos = pedidos;
        this.communication = communication;
        this.anularPedidoComunication = anularPedidoComunication;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mis_compras, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setItem(this.pedidos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.pedidos.size();//Para saber cuantos items tiene.
    }

    public void updateItems(List<PedidoConDetallesDTO> body) {
        this.pedidos.clear();
        this.pedidos.addAll(body);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setItem(final PedidoConDetallesDTO dto) {
            final TextView txtValueCodPurchases = this.itemView.findViewById(R.id.txtValueCodPurchases),
                    txtValueDatePurchases = this.itemView.findViewById(R.id.txtValueDatePurchases),
                    txtValueAmount = this.itemView.findViewById(R.id.txtValueAmount),
                    txtValueOrder = this.itemView.findViewById(R.id.txtValueOrder);
            txtValueCodPurchases.setText("C000" + Integer.toString(dto.getPedido().getId()));
            final Button btnExportInvoice = this.itemView.findViewById(R.id.btnExportInvoice);
            txtValueDatePurchases.setText((dto.getPedido().getFechaCompra()).toString());
            txtValueAmount.setText(String.format(Locale.ENGLISH, "S/%.2f", dto.getPedido().getMonto()));
            txtValueOrder.setText(dto.getPedido().isAnularPedido() ? "Pedido cancelado" : "Despachado, en proceso de envio...");

            itemView.setOnClickListener(v -> {
                final Intent i = new Intent(itemView.getContext(), DetalleMisComprasActivity.class);
                final Gson g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detailsPurchases", g.toJson(dto.getDetallePedidos()));
                communication.showDetails(i);//Esto es solo para dar una animación.
            });
            itemView.setOnLongClickListener(v -> {
                anularPedido(dto.getPedido().getId());
                return true;
            });
            btnExportInvoice.setOnClickListener(v -> {
                int idCli = dto.getPedido().getCliente().getId();
                int idOrden = dto.getPedido().getId();
                String fileName = "invoice" + "00" + dto.getPedido().getId() + ".pdf";
                communication.exportInvoice(idCli, idOrden, fileName);
            });
        }

        private void anularPedido(int id) {
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Aviso del sistema !")
                    .setContentText("¿Estás seguro de cancelar el pedido solicitado?, Una vez cancelado no podrás deshacer los cambios")
                    .setCancelText("No, Cancelar!").setConfirmText("Sí, Confirmar")
                    .showCancelButton(true)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo")
                                .setContentText(anularPedidoComunication.anularPedido(id))
                                .show();
                    }).setCancelClickListener(sDialog -> {
                sDialog.dismissWithAnimation();
                new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Operación Cancelada !")
                        .setContentText("No cancelaste ningún pedido")
                        .show();
            }).show();
        }
    }
}
