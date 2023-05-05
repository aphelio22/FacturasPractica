package com.example.facturaspractica.IO;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Esta clase es un adaptador que realiza solicitudes HTTP a un servicio web mediante la biblioteca Retrofit y OkHttp.
public class ApiAdapter {
    //Se define una variable de instancia API_SERVICE que se utiliza para almacenar una instancia de ApiService que se crea solo una vez y se reutiliza en todas las solicitudes subsecuentes.
    private static ApiService API_SERVICE;

    //El método getApiService() devuelve una instancia de la interfaz ApiService que se utiliza para hacer solicitudes HTTP a la API.
    public static ApiService getApiService() {
        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        String baseUrl = "https://viewnextandroid2.wiremockapi.cloud/";

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    //Se utiliza la biblioteca Gson para analizar la respuesta JSON en objetos Java.
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- set log level
                    .build();

            API_SERVICE = retrofit.create(ApiService.class);
        }

        return API_SERVICE;
    }

}

