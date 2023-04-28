package com.example.facturaspractica;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturaspractica.IO.response.FacturasVO;

import java.util.List;

public class FacturasAdapter extends RecyclerView.Adapter<FacturasAdapter.FacturasViewHolder> {
    private List<FacturasVO.Factura> facturas;

    public FacturasAdapter(List<FacturasVO.Factura> facturas) {
        this.facturas = facturas;
    }

    @NonNull
    @Override
    public FacturasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new FacturasViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull FacturasViewHolder facturasViewHolder, int position) {
        FacturasVO.Factura facturaVariable = facturas.get(position);
        facturasViewHolder.getTvFecha().setText(facturaVariable.getFecha());
        facturasViewHolder.getTvEstado().setText(facturaVariable.getDescEstado());;
        float importeOrdenacion = facturaVariable.getImporteOrdenacion();
        facturasViewHolder.getTvImporte().setText(String.format("%.2f", importeOrdenacion));
        if (facturaVariable.getDescEstado().equals("Pendiente de pago")) {
            facturasViewHolder.getTvEstado().setTextColor(Color.RED);
        } else {
            facturasViewHolder.getTvEstado().setTextColor(Color.BLUE);
        }
    }


    @Override
    public int getItemCount() {
        return facturas.size();
    }

    public void setFacturas(List<FacturasVO.Factura> facturas) {
        this.facturas = facturas;
        notifyDataSetChanged();
    }

    public class FacturasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvEstado;
        private TextView tvImporte;
        private TextView tvFecha;
        Dialog mDialog;

        public TextView getTvEstado() {
            return tvEstado;
        }

        public void setTvEstado(TextView tvEstado) {
            this.tvEstado = tvEstado;
        }

        public TextView getTvImporte() {
            return tvImporte;
        }

        public void setTvImporte(TextView tvImporte) {
            this.tvImporte = tvImporte;
        }

        public TextView getTvFecha() {
            return tvFecha;
        }

        public void setTvFecha(TextView tvFecha) {
            this.tvFecha = tvFecha;
        }

        public FacturasViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvEstado = (TextView) itemView.findViewById(R.id.item_estado);
            tvImporte = (TextView) itemView.findViewById(R.id.item_importe);
            tvFecha = (TextView) itemView.findViewById(R.id.item_fecha);
            mDialog = new Dialog(itemView.getContext());

        }

        @Override
        public void onClick(View v) {
            mDialog.setContentView(R.layout.popup);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView mensajePopup = mDialog.findViewById(R.id.mensajePopup);
            mensajePopup.setText("Esta funcionalidad aún no está disponible");
            mDialog.show();
            Button cerrarButton = mDialog.findViewById(R.id.botón5);
            cerrarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss(); // Cierra el diálogo al pulsar el botón "Cerrar"
                }

            });
        }
    }
}
