package com.example.unifreelanceapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MisPublicacionesAdapter extends RecyclerView.Adapter<MisPublicacionesAdapter.ViewHolder> {

    List<Post> lista;

    public MisPublicacionesAdapter(List<Post> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MisPublicacionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mis_publicaciones, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisPublicacionesAdapter.ViewHolder holder, int position) {
        Post post = lista.get(position);

        holder.tvTitulo.setText(post.titulo);
        holder.tvDescripcion.setText(post.descripcion);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PostulacionesPorPublicacionActivity.class);
            intent.putExtra("postId", post.id);
            intent.putExtra("titulo", post.titulo);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tvTituloMiPublicacion);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionMiPublicacion);
        }
    }
}