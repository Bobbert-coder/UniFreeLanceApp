package com.example.unifreelanceapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostulacionesPorPublicacionActivity extends AppCompatActivity {

    RecyclerView recyclerPostulacionesPublicacion;
    TextView btnRegresar, tvTituloPublicacion;

    FirebaseFirestore db;

    List<Postulacion> lista;
    PostulacionAdapter adapter;

    String postId, titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postulaciones_por_publicacion);
        recyclerPostulacionesPublicacion = findViewById(R.id.recyclerPostulacionesPublicacion);
        btnRegresar = findViewById(R.id.btnRegresarPostulacionesPublicacion);
        tvTituloPublicacion = findViewById(R.id.tvTituloPublicacionSeleccionada);

        recyclerPostulacionesPublicacion.setLayoutManager(new LinearLayoutManager(this));
        lista = new ArrayList<>();
        adapter = new PostulacionAdapter(lista, "empresa");
        recyclerPostulacionesPublicacion.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
        postId = getIntent().getStringExtra("postId");
        titulo = getIntent().getStringExtra("titulo");
        tvTituloPublicacion.setText(titulo);
        btnRegresar.setOnClickListener(v -> finish());
        cargarPostulaciones();
    }
    private void cargarPostulaciones() {
        db.collection("postulaciones")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    lista.clear();
                    for (var doc : queryDocumentSnapshots) {
                        Postulacion p = doc.toObject(Postulacion.class);
                        p.id = doc.getId();
                        lista.add(p);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}