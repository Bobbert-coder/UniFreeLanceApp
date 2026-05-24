package com.example.unifreelanceapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MisPublicacionesActivity extends AppCompatActivity {

    RecyclerView recyclerMisPublicaciones;
    TextView btnRegresar;

    FirebaseFirestore db;
    FirebaseAuth auth;

    List<Post> lista;
    MisPublicacionesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        recyclerMisPublicaciones = findViewById(R.id.recyclerMisPublicaciones);
        btnRegresar = findViewById(R.id.btnRegresarMisPublicaciones);

        recyclerMisPublicaciones.setLayoutManager(new LinearLayoutManager(this));

        lista = new ArrayList<>();
        adapter = new MisPublicacionesAdapter(lista);
        recyclerMisPublicaciones.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnRegresar.setOnClickListener(v -> finish());

        cargarMisPublicaciones();
    }

    private void cargarMisPublicaciones() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("posts")
                .whereEqualTo("userid", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lista.clear();

                    for (var doc : queryDocumentSnapshots) {
                        Post post = doc.toObject(Post.class);
                        post.id = doc.getId();
                        lista.add(post);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}