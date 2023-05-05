package com.example.facturaspractica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.facturaspractica.constantes.Constantes;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

/*En esta clase se costruyen los Button de la fecha, el SeekBar y los CheckBox, se meten todos en un objeto y se envían en un paquete a la MainActivity.
También se crean los botones de aplicar y eliminar filtros, así como el menu en la ToolBar con una X para volver a la MainActivity.*/
public class FiltrosActivity extends AppCompatActivity {
    private SeekBar importeSeekBar;
    private int valorActualSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //Volver a la actividad principal pulsando la "X" de la ToolBar.
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_filtrillos, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.vuelta) {
                    Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        //Construcción de los botones de fechaDesde / fechaHasta.
        //fechaDesde.
        Button fechaDesde = (Button) findViewById(R.id.fechaDesde);
        fechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaDesde.setText(dayofmonth + "/" + (monthofyear+1) + "/" + year1), year, month, day);
                dpd.show();
            }
        });

        //fechaHasta.
        Button fechaHasta = (Button) findViewById(R.id.fechaHasta);
        fechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(FiltrosActivity.this, (view, year1, monthofyear, dayofmonth) ->
                        fechaHasta.setText(dayofmonth + "/" + (monthofyear+1) + "/" + year1), year, month, day);
                dpd.show();
            }
        });

        //Declaración del SeekBar
        TextView valorSeekBar = (TextView) findViewById(R.id.valorSeekBar);
        int valorMax = MainActivity.maxImporte.intValue()+1;
        importeSeekBar = findViewById(R.id.seekBar);
        importeSeekBar.setMax(valorMax);
        importeSeekBar.setProgress(valorMax);
        valorSeekBar.setText(String.valueOf(valorMax));
        valorActualSeekBar = valorMax;
        importeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView importeTextView = findViewById(R.id.valorSeekBar);
                importeTextView.setText(String.valueOf(progress));
                valorActualSeekBar = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            //Sin función
                Log.d("onStartTrackingTouch()", "onStartTrackingTouch: el método ha fallado. ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            //Sin función
                Log.d("onStopTrackingTouch()", "onStopTrackingTouch: el método ha fallado. ");
            }
        });

        //Construcción de los checkBox.
        CheckBox pagadas = (CheckBox) findViewById(R.id.cbPagadas); //1
        CheckBox anuladas = (CheckBox) findViewById(R.id.cbAnuladas); //2
        CheckBox cuotaFija = (CheckBox) findViewById(R.id.cbCuotaFija); //3
        CheckBox pendientesPago = (CheckBox) findViewById(R.id.cbPendientesPago); //4
        CheckBox planPago = (CheckBox) findViewById(R.id.cbPlanPago); //5

        //Botón para aplicar filtros y llevarlos todos como un objeto a la MainActivity.
        Button botonFiltrar = findViewById(R.id.aplicar);
        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                double maxValueSlider = Double.parseDouble(valorSeekBar.getText().toString());
                HashMap<String, Boolean> estado = new HashMap<>();
                estado.put(Constantes.PAGADAS_STRING, pagadas.isChecked());
                estado.put(Constantes.ANULADAS_STRING, anuladas.isChecked());
                estado.put(Constantes.CUOTA_FIJA_STRING, cuotaFija.isChecked());
                estado.put(Constantes.PENDIENTES_PAGO_STRING, pendientesPago.isChecked());
                estado.put(Constantes.PLAN_PAGO_STRING, planPago.isChecked());
                String fechaMin = fechaDesde.getText().toString();
                String fechaMax = fechaHasta.getText().toString();
                Filtrar miFiltro = new Filtrar(fechaMax, fechaMin, maxValueSlider, estado);
                intent.putExtra("filtro", gson.toJson(miFiltro));
                startActivity(intent);
            }
        });

        //Funcionamiento del botón de resetear filtros
        Button resetFiltrosButton = findViewById(R.id.eliminar);
        resetFiltrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearFiltros();
            }
        });
    }

    //Método para restablecer los filtros con el botón de resetear filtros
    private void resetearFiltros() {
    // Restablecer valores de fecha
        Button fechaDesde = findViewById(R.id.fechaDesde);
        fechaDesde.setText("Dia/Mes/Año");
        Button fechaHasta = findViewById(R.id.fechaHasta);
        fechaHasta.setText("Dia/Mes/Año");

    // Restablecer valor de seekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        int maxImporte = MainActivity.maxImporte.intValue() + 1;
        seekBar.setMax(maxImporte);
        seekBar.setProgress(maxImporte);
        TextView tvValorImporte = findViewById(R.id.valorSeekBar);
        tvValorImporte.setText(String.valueOf(maxImporte));

    // Restablecer valores de checkboxes
        CheckBox pagadas = findViewById(R.id.cbPagadas);
        pagadas.setChecked(false);
        CheckBox anuladas = findViewById(R.id.cbPlanPago);
        anuladas.setChecked(false);
        CheckBox cuotaFija = findViewById(R.id.cbPendientesPago);
        cuotaFija.setChecked(false);
        CheckBox pendientesPago = findViewById(R.id.cbAnuladas);
        pendientesPago.setChecked(false);
        CheckBox planPago = findViewById(R.id.cbCuotaFija);
        planPago.setChecked(false);
    }
}