package com.example.facturaspractica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.facturaspractica.IO.ApiAdapter;
import com.example.facturaspractica.IO.response.FacturasVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<FacturasVO> {
    private RecyclerView rv1;
    private FacturasAdapter adaptadorFacturas;
    private List<FacturasVO.Factura> facturasList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Call<FacturasVO> llamada = ApiAdapter.getApiService().getObjetoFacturasVO();
        llamada.enqueue(this);
        rv1 = findViewById(R.id.rv1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onResponse(Call<FacturasVO> call, Response<FacturasVO> response) {
        if(response.isSuccessful()) {
            FacturasVO facturas= response.body();
            facturasList = facturas.getFacturas();
            adaptadorFacturas = new FacturasAdapter(facturasList);
            rv1.setAdapter(adaptadorFacturas);
            Log.d("onResponse facturas", "Tamaño del arreglo: " + facturas.getFacturas().size());

        }
    }

    @Override
    public void onFailure(Call<FacturasVO> call, Throwable t) {

    }
}