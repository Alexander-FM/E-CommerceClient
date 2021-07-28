package com.alexandertutoriales.cliente.ecommerce.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Cliente;
import com.alexandertutoriales.cliente.ecommerce.entity.service.DocumentoAlmacenado;
import com.alexandertutoriales.cliente.ecommerce.entity.service.Usuario;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.ClienteViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.DocumentoAlmacenadoViewModel;
import com.alexandertutoriales.cliente.ecommerce.viewmodel.UsuarioViewModel;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDateTime;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private File f;
    private ClienteViewModel clienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private DocumentoAlmacenadoViewModel documentoAlmacenadoViewModel;
    private Button btnSubirImagen, btnGuardarDatos;
    private CircleImageView imageUser;
    private AutoCompleteTextView dropdownTipoDoc, dropdownDepartamento, dropdownProvincia, dropdownDistrito;
    private EditText edtNameUser, edtApellidoPaternoU, edtApellidoMaternoU, edtNumDocU, edtTelefonoU,
            edtDireccionU, edtPasswordUser, edtEmailUser;
    private TextInputLayout txtInputNameUser, txtInputApellidoPaternoU, txtInputApellidoMaternoU,
            txtInputTipoDoc, txtInputNumeroDocU, txtInputDepartamento, txtInputProvincia,
            txtInputDistrito, txtInputTelefonoU, txtInputDireccionU, txtInputEmailUser, txtInputPasswordUser;
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        this.init();
        this.initViewModel();
        this.spinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias por conceder los permisos para leer el almacenamiento 😀,estos permisos son necesarios para poder escoger tu foto de perfil", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento 😥,estos permisos son necesarios para poder escoger tu foto de perfil", Toast.LENGTH_LONG).show();
                    this.finish();
                }
                break;
        }
    }

    public void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        btnSubirImagen = findViewById(R.id.btnSubirImagen);
        imageUser = findViewById(R.id.imageUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtApellidoPaternoU = findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = findViewById(R.id.edtApellidoMaternoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtTelefonoU = findViewById(R.id.edtTelefonoU);
        edtDireccionU = findViewById(R.id.edtDireccionU);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);
        dropdownDepartamento = findViewById(R.id.dropdownDepartamento);
        dropdownProvincia = findViewById(R.id.dropdownProvincia);
        dropdownDistrito = findViewById(R.id.dropdownDistrito);
        //TextInputLayout
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputApellidoPaternoU = findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = findViewById(R.id.txtInputApellidoMaternoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputDepartamento = findViewById(R.id.txtInputDepartamento);
        txtInputProvincia = findViewById(R.id.txtInputProvincia);
        txtInputDistrito = findViewById(R.id.txtInputDistrito);
        txtInputTelefonoU = findViewById(R.id.txtInputTelefonoU);
        txtInputDireccionU = findViewById(R.id.txtInputDireccionU);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);
        //Asignar un ONCLICK al boton subir imagen
        btnSubirImagen.setOnClickListener(v -> {
            this.cargarImagen();
        });
        //Asignar un ONCLICK al boton guardarImagen
        btnGuardarDatos.setOnClickListener(v -> {
            this.registrarUsuario();
        });
        ///ONCHANGE LISTENEER A LOS EDITEXT
        edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNameUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoPaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoMaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNumeroDocU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtTelefonoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTelefonoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDireccionU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDireccionU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownTipoDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTipoDoc.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDepartamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDepartamento.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputProvincia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDistrito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDistrito.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void registrarUsuario() {
        Cliente c;
        if (validar()) {
            c = new Cliente();
            try {
                c.setNombres(edtNameUser.getText().toString());
                c.setApellidoPaterno(edtApellidoPaternoU.getText().toString());
                c.setApellidoMaterno(edtApellidoMaternoU.getText().toString());
                c.setTipoDoc(dropdownTipoDoc.getText().toString());
                c.setNumDoc(edtNumDocU.getText().toString());
                c.setDepartamento(dropdownDepartamento.getText().toString());
                c.setProvincia(dropdownProvincia.getText().toString());
                c.setDistrito(dropdownDistrito.getText().toString());
                c.setTelefono(edtTelefonoU.getText().toString());
                c.setDireccionEnvio(edtDireccionU.getText().toString());
                c.setId(0);
                LocalDateTime ldt = LocalDateTime.now();//Para generar un nombre al archivo en base a la fecha, hora, año
                RequestBody rb = RequestBody.create(f, MediaType.parse("multipart/form-data")), somedata;//Les estamos enviando un archivo (imagen) desde el formulario
                String filename = "" + ldt.getDayOfMonth() + (ldt.getMonthValue() + 1) + ldt.getYear() + ldt.getHour() + ldt.getMinute() + ldt.getSecond();//Asignarle un nombre al archivo
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", f.getName(), rb);
                somedata = RequestBody.create("profilePh" + filename, MediaType.parse("text/plain"));//Le estamos enviando un nombre al archivo.
                this.documentoAlmacenadoViewModel.save(part, somedata).observe(this, response -> {
                    if (response.getRpta() == 1) {
                        c.setFoto(new DocumentoAlmacenado());
                        c.getFoto().setId(response.getBody().getId());//Asignamos la foto al cliente
                        this.clienteViewModel.guardarCliente(c).observe(this, cresponse -> {
                            if (cresponse.getRpta() == 1) {
                                //successMessage("Registro realizado con éxito 😀 " + response.getMessage() + " ahora inicia sesión para continuar");
                                Toast.makeText(this, response.getMessage() + ", ahora procederemos a registrar sus credenciales de inciio de sesión", Toast.LENGTH_SHORT).show();
                                int idC = cresponse.getBody().getId();//Obtener el id del cliente registrado
                                Usuario u = new Usuario();
                                u.setEmail(edtEmailUser.getText().toString());
                                u.setClave(edtPasswordUser.getText().toString());
                                u.setVigencia(true);
                                u.setCliente(new Cliente(idC));//Aquí hay un constructor en la entidad Cliente que como parámetro recibe un id.
                                this.usuarioViewModel.save(u).observe(this, uResponse -> {
                                    Toast.makeText(this, uResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (uResponse.getRpta() == 1) {
                                        Toast.makeText(this, "Sus datos y credenciales de incio de sesión fueron creados correctamente", Toast.LENGTH_SHORT).show();
                                        this.finish();//Cerramos la actividad y volvemos al login.
                                    } else {
                                        Toast.makeText(this, "No se han podido registrar los datos, inténtelo nuevamente en unos minutos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(this, "No se han podido registrar los datos,inténtelo nuevamente en unos minutos", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "No se han podido registrar los datos, inténtelo nuevamente en unos minutos", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                warningMessage("Se ha producido un error: " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario.");
        }
    }


    private boolean validar() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, telefono, direccion, correo, clave,
                dropTipoDoc, dropDepartamento, dropProvincia, dropDistrito;
        nombres = edtNameUser.getText().toString();
        apellidoPaterno = edtApellidoPaternoU.getText().toString();
        apellidoMaterno = edtApellidoMaternoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        telefono = edtTelefonoU.getText().toString();
        direccion = edtDireccionU.getText().toString();
        correo = edtEmailUser.getText().toString();
        clave = edtPasswordUser.getText().toString();
        dropTipoDoc = dropdownTipoDoc.getText().toString();
        dropDepartamento = dropdownDepartamento.getText().toString();
        dropProvincia = dropdownProvincia.getText().toString();
        dropDistrito = dropdownDistrito.getText().toString();
        if(this.f==null){
            errorMessage("debe selecionar una foto de perfil");
            retorno=false;
        }
        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingresar número telefónico");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingresar correo electrónico");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Seleccionar Tipo Doc");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }
        if (dropDepartamento.isEmpty()) {
            txtInputDepartamento.setError("Seleccionar Departamento");
            retorno = false;
        } else {
            txtInputDepartamento.setErrorEnabled(false);
        }
        if (dropProvincia.isEmpty()) {
            txtInputProvincia.setError("Seleccionar Provincia");
            retorno = false;
        } else {
            txtInputProvincia.setErrorEnabled(false);
        }
        if (dropDistrito.isEmpty()) {
            txtInputDistrito.setError("Seleccionar Distrito");
            retorno = false;
        } else {
            txtInputDistrito.setErrorEnabled(false);
        }
        return retorno;
    }

    public void spinners() {
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);
        //LISTA DE DEPARTAMENTOS
        String[] departamentos = getResources().getStringArray(R.array.departamentos);
        ArrayAdapter arrayDepartamentos = new ArrayAdapter(this, R.layout.dropdown_item, departamentos);
        dropdownDepartamento.setAdapter(arrayDepartamentos);
        //LISTA DE PROVINCIAS
        String[] provincias = getResources().getStringArray(R.array.provincias);
        ArrayAdapter arrayProvincias = new ArrayAdapter(this, R.layout.dropdown_item, provincias);
        dropdownProvincia.setAdapter(arrayProvincias);
        //LISTA DE DISTRITOS
        String[] distritos = getResources().getStringArray(R.array.distritos);
        ArrayAdapter arrayDistritos = new ArrayAdapter(this, R.layout.dropdown_item, distritos);
        dropdownDistrito.setAdapter(arrayDistritos);
        //ASIGNAR ONCLICK
        dropdownTipoDoc.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, tipoDoc[position], Toast.LENGTH_SHORT).show();
        });
    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.clienteViewModel = vmp.get(ClienteViewModel.class);
        this.usuarioViewModel = vmp.get(UsuarioViewModel.class);
        this.documentoAlmacenadoViewModel = vmp.get(DocumentoAlmacenadoViewModel.class);

    }

    private void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(Intent.createChooser(i, "Seleccione la Aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final String realPath = getRealPathFromURI(uri);
            this.f = new File(realPath);
            this.imageUser.setImageURI(uri);
        }
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}