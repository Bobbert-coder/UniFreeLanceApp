package com.example.unifreelanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostulacionAdapter extends RecyclerView.Adapter<PostulacionAdapter.ViewHolder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Postulacion> lista;
    String modo;

    public PostulacionAdapter(List<Postulacion> lista, String modo) {
        this.lista = lista;
        this.modo = modo;
    }
    @NonNull
    @Override
    public PostulacionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_postulacion, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostulacionAdapter.ViewHolder holder, int position) {
        Postulacion p = lista.get(position);
        holder.tvTitulo.setText(p.tituloTrabajo);
        holder.tvDescripcion.setText(p.descripcionTrabajo);
        holder.tvCV.setText("CV: " + p.nombreCV);
        holder.tvEstado.setText("Estado: " + p.estado);

        if (p.estado != null) {
            if (p.estado.equalsIgnoreCase("Aceptada")) {

                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_aceptada);
            }
            else if (p.estado.equalsIgnoreCase("Rechazada")) {
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_rechazada);
            }
            else {
                holder.tvEstado.setBackgroundResource(R.drawable.bg_chip_dark);
            }

        }

        if (modo.equals("empresa")) {
            holder.btnEliminarPostulacion.setText("X");
            holder.btnAceptarPostulacion.setVisibility(View.VISIBLE);
            holder.btnEliminarPostulacion.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Rechazar postulación")
                        .setMessage("¿Seguro que deseas rechazar esta postulación?")
                        .setPositiveButton("Sí", (dialog, which) -> {

                            db.collection("postulaciones")
                                    .document(p.id)
                                    .update("estado", "Rechazada")
                                    .addOnSuccessListener(unused -> {
                                        p.estado = "Rechazada";
                                        notifyItemChanged(position);
                                    });

                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
            });

            holder.btnAceptarPostulacion.setOnClickListener(v -> {

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Aceptar postulación")
                        .setMessage("¿Aceptar esta postulación?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            db.collection("postulaciones")
                                    .document(p.id)
                                    .update("estado", "Aceptada")
                                    .addOnSuccessListener(unused -> {
                                        p.estado = "Aceptada";
                                        notifyItemChanged(position);
                                    });
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });

        } else {
            holder.btnEliminarPostulacion.setText("X");
            holder.btnAceptarPostulacion.setVisibility(View.GONE);
            holder.btnEliminarPostulacion.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Eliminar postulación")
                        .setMessage("¿Seguro que deseas eliminar esta postulación?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            db.collection("postulaciones")
                                    .document(p.id)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        lista.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, lista.size());
                                    });
                        })
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

    }
    @Override
    public int getItemCount() {
        return lista.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion, tvCV, tvEstado, btnEliminarPostulacion, btnAceptarPostulacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnEliminarPostulacion = itemView.findViewById(R.id.btnEliminarPostulacion);
            tvTitulo = itemView.findViewById(R.id.tvTituloPostulacion);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPostulacion);
            tvCV = itemView.findViewById(R.id.tvCVPostulacion);
            tvEstado = itemView.findViewById(R.id.tvEstadoPostulacion);
            btnAceptarPostulacion = itemView.findViewById(R.id.btnAceptarPostulacion);
        }
    }
}