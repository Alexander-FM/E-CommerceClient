package com.alexandertutoriales.cliente.ecommerce.api;

import com.alexandertutoriales.cliente.ecommerce.entity.service.Pedido;
import com.alexandertutoriales.cliente.ecommerce.entity.service.dto.PedidoConDetallesDTO;
import com.alexandertutoriales.cliente.ecommerce.utils.DateSerializer;
import com.alexandertutoriales.cliente.ecommerce.utils.TimeSerializer;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {
    public static final String baseUrlE = "http://10.0.2.2:9090";
    public static final String ipAlexander = "http://192.168.101.17:9090";//Ip de mi emulador
    private static Retrofit retrofit;
    private static String token = "";

    private static UsuarioApi usuarioApi;
    private static ClienteApi clienteApi;
    private static PlatilloApi platilloApi;
    private static DocumentoAlmacenadoApi daApi;
    private static CategoriaApi categoriaApi;
    private static PedidoApi pedidoApi;

    static {
        initClient();
    }

    private static void initClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
    }

    public static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //Interceptor authInterceptor = chain -> chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build());


        builder.addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                //.addInterceptor(authInterceptor)
                .addNetworkInterceptor(stetho);

        return builder.build();
    }

    public static void setToken(String value) {
        token = value;
        initClient();
    }

    public static UsuarioApi getUsuarioApi() {
        if (usuarioApi == null) {
            usuarioApi = retrofit.create(UsuarioApi.class);
        }
        return usuarioApi;
    }
    public static ClienteApi getClienteApi(){
        if(clienteApi == null){
            clienteApi = retrofit.create(ClienteApi.class);
        }
        return clienteApi;
    }
    public static PlatilloApi getPlatilloApi(){
        if(platilloApi == null){
            platilloApi = retrofit.create(PlatilloApi.class);
        }
        return platilloApi;
    }
    public static DocumentoAlmacenadoApi getDocumentoAlmacenadoApi() {
        if (daApi == null) {
            daApi = retrofit.create(DocumentoAlmacenadoApi.class);
        }
        return daApi;
    }
    public static CategoriaApi getCategoriaApi(){
        if(categoriaApi == null){
            categoriaApi = retrofit.create(CategoriaApi.class);
        }
        return categoriaApi;
    }
    public static PedidoApi getPedidoApi(){
        if(pedidoApi == null){
            pedidoApi = retrofit.create(PedidoApi.class);
        }
        return pedidoApi;
    }
}

