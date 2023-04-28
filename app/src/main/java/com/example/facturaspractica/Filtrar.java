package com.example.facturaspractica;

import java.util.HashMap;

public class Filtrar {
    private String fechaMax;
    private String  fechaMin;
    private double maxValuesSlider;
    private HashMap<String, Boolean> estado = new HashMap<>();

    public Filtrar(String fechaMax, String fechaMin, double maxValuesSlider, HashMap<String, Boolean> estado) {
        this.fechaMax = fechaMax;
        this.fechaMin = fechaMin;
        this.maxValuesSlider = maxValuesSlider;
        this.estado = estado;
    }

    public String getFechaMax() {
        return fechaMax;
    }

    public void setFechaMax(String fechaMax) {
        this.fechaMax = fechaMax;
    }

    public String getFechaMin() {
        return fechaMin;
    }

    public void setFechaMin(String fechaMin) {
        this.fechaMin = fechaMin;
    }

    public double getMaxValuesSlider() {
        return maxValuesSlider;
    }

    public void setMaxValuesSlider(double maxValuesSlider) {
        this.maxValuesSlider = maxValuesSlider;
    }

    public HashMap<String, Boolean> getEstado() {
        return estado;
    }

    public void setEstado(HashMap<String, Boolean> estado) {
        this.estado = estado;
    }
}