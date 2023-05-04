package com.example.facturaspractica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.facturaspractica.IO.ApiAdapter;
import com.example.facturaspractica.IO.response.FacturasVO;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<FacturasVO> {
    private RecyclerView rv1;
    private FacturasAdapter adaptadorFacturas;
    private List<FacturasVO.Factura> facturasList;
    public static Double maxImporte = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_ida:
                        Intent intent = new Intent(MainActivity.this, FiltrosActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        rv1 = findViewById(R.id.rv1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);
        facturasList = new ArrayList<>();
        adaptadorFacturas = new FacturasAdapter(facturasList);
        rv1.setAdapter(adaptadorFacturas);
        Call<FacturasVO> llamada = ApiAdapter.getApiService().getObjetoFacturasVO();
        llamada.enqueue(this);
    }

    @Override
    public void onResponse(Call<FacturasVO> call, Response<FacturasVO> response) {
        if (response.isSuccessful()) {
            FacturasVO facturas = response.body();
            facturasList = facturas.getFacturas();
            maxImporte = Double.valueOf(facturasList.stream().max(Comparator.comparing(FacturasVO.Factura::getImporteOrdenacion)).get().getImporteOrdenacion());

            //Recibir los datos desde la otra actividad en forma de objeto
            String recibirDatos = getIntent().getStringExtra("filtro");
            if (recibirDatos != null) {
                Filtrar filtrar = new Gson().fromJson(recibirDatos, Filtrar.class);
                List<FacturasVO.Factura> listFiltro = facturasList;

                //Se usan los métodos creados para filtrar por fecha, importe y por checkBox
                listFiltro = comprobrarFiltroFechas(filtrar.getFechaMax(), filtrar.getFechaMin(), listFiltro);
                listFiltro = comprobarBarraImporte(filtrar.getMaxValuesSlider(), listFiltro);
                listFiltro = ccomprobarChekBox(filtrar.getEstado(), listFiltro);

                //Se declara que si la lista está vacía se llame al método "mostrarMensajeVacio()"
                if (listFiltro.isEmpty()){
                    mostrarMensajeFiltroVacio();
                } //Si no está vacía lo que contenga "listFiltro" se carga en "facturasList"
                facturasList = listFiltro;
            }
            adaptadorFacturas = new FacturasAdapter(facturasList);
            rv1.setAdapter(adaptadorFacturas);
        }
    }

    @Override
    public void onFailure(Call<FacturasVO> call, Throwable t) {
    // No OP
    }

    //Método para filtrar por fechas
    private List<FacturasVO.Factura> comprobrarFiltroFechas(String fechaMax, String fechaMin, List<FacturasVO.Factura> listFiltro) {
        ArrayList<FacturasVO.Factura> facturasFiltradas = new ArrayList<>();
        if (!fechaMin.equals("Dia/Mes/Año") && !fechaMax.equals("Dia/Mes/Año")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
            Date fechaMinDate = null;
            Date fechaMaxDate = null;

            try {
                fechaMinDate = sdf.parse(fechaMin);
                fechaMaxDate = sdf.parse(fechaMax);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            for (FacturasVO.Factura factura : listFiltro) {
                Date fechaFactura = null;
                try {
                    fechaFactura = sdf.parse(factura.getFecha());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (fechaFactura.after(fechaMinDate) && fechaFactura.before(fechaMaxDate)) {
                    facturasFiltradas.add(factura);
                }
            }
            listFiltro = facturasFiltradas;
        }
        return listFiltro;
    }

    //Método para para filtrar por barra de importe
    private List<FacturasVO.Factura> comprobarBarraImporte(Double importeFiltro, List<FacturasVO.Factura>listFiltro){
        ArrayList<FacturasVO.Factura> listFiltroSeekBar = new ArrayList<>();
        for (FacturasVO.Factura factura : listFiltro) {
            if (factura.getImporteOrdenacion() < importeFiltro) {
                listFiltroSeekBar.add(factura);
            }
            listFiltro=listFiltroSeekBar;
        }
        return listFiltro;
    }
    //Método para filtrar por CheckBox
    private List<FacturasVO.Factura> ccomprobarChekBox(HashMap<String, Boolean> estado, List<FacturasVO.Factura> listFiltro) {
        //Comprobar si están seleccionados los checkBoxes
        boolean checkBoxPagadas = estado.get("pagadas");
        boolean checkBoxAnuladas = estado.get("anuladas");
        boolean checkBoxCuotaFija = estado.get("cuotaFija");
        boolean checkBoxPendientesPago = estado.get("pendientesPago");
        boolean checkBoxPlanPago = estado.get("planPago");

        //Lista de checkBoxes
        ArrayList<FacturasVO.Factura> listFiltro2 = new ArrayList<>();
        if (checkBoxPagadas || checkBoxAnuladas || checkBoxCuotaFija || checkBoxPendientesPago || checkBoxPlanPago) {
            for (FacturasVO.Factura factura : listFiltro) {
                if (factura.getDescEstado().equals("Pagada") && checkBoxPagadas) {
                    listFiltro2.add(factura);
                }
                if (factura.getDescEstado().equals("Anuladas") && checkBoxAnuladas) {
                    listFiltro2.add(factura);
                }
                if (factura.getDescEstado().equals("cuotaFija") && checkBoxCuotaFija) {
                    listFiltro2.add(factura);
                }
                if (factura.getDescEstado().equals("Pendiente de pago") && checkBoxPendientesPago) {
                    listFiltro2.add(factura);
                }
                if (factura.getDescEstado().equals("planPago") && checkBoxPlanPago) {
                    listFiltro2.add(factura);
                }
            }
            listFiltro = listFiltro2;
        }
        return listFiltro;
    }

    //Método para mostrar mensaje de "Aquí no hay nada" cuando no hay facturas cargadas en la lista
    private void mostrarMensajeFiltroVacio(){

    //Se crea el Relative Layout y se hace visible el TextView cuando la lista está vacía
        RelativeLayout layout = new RelativeLayout(MainActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        //Creación del TextView que mostrará el mensaje de "Aquí no hay nada" si la lista de facturas está vacía
        TextView textView = new TextView(MainActivity.this);
        textView.setText("Aqui no hay nada");
        textView.setTextSize(30);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(textView, params);
        setContentView(layout);





    }
}