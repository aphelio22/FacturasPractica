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

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;

public class FiltrosActivity extends AppCompatActivity {
    private SeekBar importeSeekBar;
    DatePickerDialog datePickerDialog;
    private int valorActualSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //Volver a la actividad principal usando la "X" del Layout
        MenuHost menu = this;
        menu.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_filtrillos, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.vuelta:
                        Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        //Construcción del SeekBar
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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Construcción de los botones de fechaDesde / fechaHasta
        //fechaDesde
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
        //fechaHasta
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
        Button botonFiltrar = findViewById(R.id.aplicar);
        //Construcción de los checkBox
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.anuladas);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.cuotaFija);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.pendientesPago);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.planPago);
        Button botonDesde = (Button) findViewById(R.id.fechaDesde);

        //Botón aplicar para aplicar filtros y llevar todos los filtros como un objeto

        botonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(FiltrosActivity.this, MainActivity.class);
                double maxValueSlider = Double.parseDouble(valorSeekBar.getText().toString());
                HashMap<String, Boolean> estado = new HashMap<>();
                estado.put("pagada", checkBox1.isChecked());
                estado.put("anulada", checkBox2.isChecked());
                estado.put("cuotaFija", checkBox3.isChecked());
                estado.put("pendientePago", checkBox4.isChecked());
                estado.put("planPago", checkBox5.isChecked());
                String fechaMin = fechaDesde.getText().toString();
                String fechaMax = fechaHasta.getText().toString();
                Filtrar miFiltro = new Filtrar(fechaMax, fechaMin, maxValueSlider, estado);
                intent.putExtra("filtro", gson.toJson(miFiltro));
                Log.d("myIntent", intent.toString());
                startActivity(intent);
            }
        });
    }
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
        CheckBox pagadas = findViewById(R.id.checkBox1);
        pagadas.setChecked(false);
        CheckBox anuladas = findViewById(R.id.planPago);
        anuladas.setChecked(false);
        CheckBox cuotaFija = findViewById(R.id.pendientesPago);
        cuotaFija.setChecked(false);
        CheckBox pendientesPago = findViewById(R.id.anuladas);
        pendientesPago.setChecked(false);
        CheckBox planPago = findViewById(R.id.cuotaFija);
        planPago.setChecked(false);
    }
}