package com.alexandertutoriales.cliente.ecommerce.activity.ui.compras;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.activity.VerInvoiceActivity;
import com.alexandertutoriales.cliente.ecommerce.adapter.MisComprasAdapter;
import com.alexandertutoriales.cliente.ecommerce.communication.AnularPedidoComunication;
import com.alexandertutoriales.cliente.ecommerce.communication.Communication;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.PedidoViewModel;
import com.google.android.gms.common.internal.ResourceUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MisComprasFragment extends androidx.fragment.app.Fragment implements Communication, AnularPedidoComunication {
    private ActivityResultLauncher<String> perReqLuncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            successMessage("Gracias por concedernos el permiso,oprime el boton nuevamente");
        } else {
            errorMessage("Permiso denegado,no podemos continuar");
        }
    });
    private PedidoViewModel viewModel;
    private RecyclerView rcvPedidos;
    private MisComprasAdapter adapter;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mis_compras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        init(view);
        initAdapater();
        loadData();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
    }

    private void init(View v) {
        rcvPedidos = v.findViewById(R.id.rcvMisCompras);

    }

    private void initAdapater() {
        adapter = new MisComprasAdapter(new ArrayList<>(), this, this);
        rcvPedidos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvPedidos.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            this.viewModel.listarComprasPorCliente(u.getCliente().getId()).observe(getViewLifecycleOwner(), response -> {
                adapter.updateItems(response.getBody());
            });
        }
    }

    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.above_in, R.anim.above_out);
        //Para obtener la actividad que encapsula al fragment.
    }

    @Override
    public void exportInvoice(int idCli, int idOrden, String fileName) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            viewModel.exportComplaint(idCli, idOrden).observe(getViewLifecycleOwner(), response -> {
                if (response.getRpta() == 1) {
                    try {
                        boolean folderCreated = true;
                        File path = requireContext().getExternalFilesDir("/pedidos");
                        if (!path.exists()) {
                            if (!path.mkdir()) {
                                folderCreated = false;
                                Toast.makeText(requireContext(), "No se pudo crear la carpeta para guardar los archivos, intentalo más tarde", Toast.LENGTH_LONG);
                            }
                        }
                        if (folderCreated) {
                            byte[] bytes = response.getBody().bytes();
                            File file = new File(path, fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                            Toast.makeText(requireContext(), "Archivo guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            //Previsualizar el PDF
                            new MaterialAlertDialogBuilder(requireContext()).setTitle("Exportar Factura")
                                    .setMessage("Factura guardada correctamente en la siguiente ubicación: "
                                            + file.getAbsolutePath() + " ¿Deseas visualizarlo ahora?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sí", (dialog, which) -> {
                                        dialog.dismiss();
                                        Intent i = new Intent(requireContext(), VerInvoiceActivity.class);
                                        i.putExtra("pdf", bytes);
                                        startActivity(i);
                                    }).setNegativeButton("Quizás más tarde", (dialog, which) -> {
                                dialog.dismiss();
                            }).show();
                        }

                    } catch (Exception e) {
                        errorMessage("No se pudo guardar el archivo en el dispositivo");
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Concedemos el permiso")
                    .setContentText("Debido a que vamos a descargar un archivo,requerimos del acceso al almacenamiento interno para poder guardarlo")
                    .setConfirmButton("Vale", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        perReqLuncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }).setCancelButton("Quizas mas tarde", SweetAlertDialog::dismissWithAnimation).show();
        }
    }

    @Override
    public String anularPedido(int id) {
        this.viewModel.anularPedido(id).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                loadData();
            }
        });
        return "El pedido ha sido cancelado";
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void successMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Buen Trabajo!").setContentText(message).show();
    }
}