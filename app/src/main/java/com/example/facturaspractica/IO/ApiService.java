package com.example.facturaspractica.IO;

import com.example.facturaspractica.IO.response.FacturasVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("facturas")
    Call<FacturasVO> getObjetoFacturasVO();
}
