package com.example.facturaspractica.IO.response;

import java.util.List;

//Esta clase contiene los elementos del JSON
public class FacturasVO {
    private int numFacturas;
    private List<Factura> facturas;

    public FacturasVO(int numFacturas, List<Factura> facturas) {
        this.numFacturas = numFacturas;
        this.facturas = facturas;
    }

    public int getNumFacturas() {
        return numFacturas;
    }

    public void setNumFacturas(int numFacturas) {
        this.numFacturas = numFacturas;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
    public class Factura {
        private String descEstado;
        private float importeOrdenacion;
        private String fecha;

        public Factura(String descEstado, float importeOrdenacion, String fecha) {
            this.descEstado = descEstado;
            this.importeOrdenacion = importeOrdenacion;
            this.fecha = fecha;
        }

        public String getDescEstado() {
            return descEstado;
        }

        public void setDescEstado(String descEstado) {
            this.descEstado = descEstado;
        }

        public float getImporteOrdenacion() {
            return importeOrdenacion;
        }

        public void setImporteOrdenacion(float importeOrdenacion) {
            this.importeOrdenacion = importeOrdenacion;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }
    }
}

