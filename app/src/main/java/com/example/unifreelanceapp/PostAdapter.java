package com.example.unifreelanceapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    String role;
    List<Post> postList;

    public PostAdapter(List<Post> postList, String role) {
        this.postList = postList;
        this.role = role;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String currentUserId = mAuth.getCurrentUser().getUid();
        Post post = postList.get(position);
        holder.tvTitle.setText(post.titulo);
        holder.tvDescription.setText(post.descripcion);

        holder.itemView.setOnClickListener(v -> {

            Intent intentadapter = new Intent(v.getContext(), PostDetailActivity.class);

            intentadapter.putExtra("title", post.getTitle());
            intentadapter.putExtra("description", post.getDescription());
            intentadapter.putExtra("rol", role);
            intentadapter.putExtra("postId", post.id);
            v.getContext().startActivity(intentadapter);

        });

        if(post.userid != null && post.userid.equals(currentUserId)){
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        else holder.btnDelete.setVisibility(View.INVISIBLE);

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar post")
                    .setMessage("¿Eliminar \"" + post.titulo + "\"?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        db.collection("posts")
                                .document(post.id)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    postList.remove(position);
                                    notifyItemRemoved(position);
                                });

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}