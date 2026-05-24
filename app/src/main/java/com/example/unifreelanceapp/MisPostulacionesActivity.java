package com.example.unifreelanceapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MisPostulacionesActivity extends AppCompatActivity {

    RecyclerView recyclerPostulaciones;
    TextView btnRegresar;

    FirebaseAuth auth;
    FirebaseFirestore db;

    List<Postulacion> postulacionList;
    PostulacionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_postulaciones);

        recyclerPostulaciones = findViewById(R.id.recyclerPostulaciones);
        btnRegresar = findViewById(R.id.btnRegresarPostulaciones);

        recyclerPostulaciones.setLayoutManager(new LinearLayoutManager(this));

        postulacionList = new ArrayList<>();
        adapter = new PostulacionAdapter(postulacionList, "estudiante");
        recyclerPostulaciones.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegresar.setOnClickListener(v -> finish());

        cargarPostulaciones();
    }
    private void cargarPostulaciones() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = auth.getCurrentUser().getUid();

        db.collection("postulaciones")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postulacionList.clear();

                    for (var doc : queryDocumentSnapshots) {
                        Postulacion postulacion = doc.toObject(Postulacion.class);
                        postulacion.id = doc.getId();
                        postulacionList.add(postulacion);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}