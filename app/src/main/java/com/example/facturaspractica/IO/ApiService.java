package com.example.facturaspractica.IO;

import com.example.facturaspractica.IO.response.FacturasVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/*Interfaz que define la API REST que se utiliza para solicitar datos de facturas.
Define un método getObjetoFacturasVO() con la anotación @GET que indica que se realizará una solicitud HTTP GET al endpoint "facturas".*/
public interface ApiService {
    @GET("facturas")
    //La llamada devuelve un objeto de tipo Call<FacturasVO>, que es un objeto de llamada de Retrofit que envuelve la solicitud HTTP y la respuesta esperada.
    Call<FacturasVO> getObjetoFacturasVO();
}
