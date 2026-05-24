package com.example.unifreelanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostularmeActivity extends AppCompatActivity {

    LinearLayout btnSeleccionarCV;
    TextView tvNombreCV, tvPesoCV, tvTituloTrabajo, tvDescripcionTrabajo, btnRegresar;
    EditText etCarta, etPortafolio, etLinkedin;
    Button btnEnviarPostulacion;

    Uri archivoUri;

    FirebaseAuth auth;
    FirebaseFirestore db;

    String title, description, postId;
    String nombreCV = "";

    private static final int REQUEST_CV = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postularme2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvTituloTrabajo = findViewById(R.id.tvTituloTrabajo);
        tvDescripcionTrabajo = findViewById(R.id.tvDescripcionTrabajo);
        btnSeleccionarCV = findViewById(R.id.btnSeleccionarCV);
        tvNombreCV = findViewById(R.id.tvNombreCV);
        tvPesoCV = findViewById(R.id.tvPesoCV);
        etCarta = findViewById(R.id.etCarta);
        etPortafolio = findViewById(R.id.etPortafolio);
        etLinkedin = findViewById(R.id.etLinkedin);
        btnEnviarPostulacion = findViewById(R.id.btnEnviarPostulacion);
        btnRegresar = findViewById(R.id.tvFlecha2);

        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        postId = getIntent().getStringExtra("postId");

        tvTituloTrabajo.setText(title);
        tvDescripcionTrabajo.setText(description);

        btnSeleccionarCV.setOnClickListener(v -> abrirSelectorCV());

        btnEnviarPostulacion.setOnClickListener(v -> enviarPostulacion());

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void abrirSelectorCV() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        String[] tipos = {
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        };

        intent.putExtra(Intent.EXTRA_MIME_TYPES, tipos);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, REQUEST_CV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CV && resultCode == RESULT_OK && data != null) {
            archivoUri = data.getData();
            nombreCV = obtenerNombreArchivo(archivoUri);
            tvNombreCV.setText(nombreCV);
            tvPesoCV.setText("Archivo seleccionado");
            Toast.makeText(this, "CV seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarPostulacion() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }
        if (archivoUri == null) {
            Toast.makeText(this, "Selecciona tu CV", Toast.LENGTH_SHORT).show();
            return;
        }
        String carta = etCarta.getText().toString().trim();
        String portafolio = etPortafolio.getText().toString().trim();
        String linkedin = etLinkedin.getText().toString().trim();
        if (carta.isEmpty()) {
            Toast.makeText(this, "Escribe tu carta de presentación", Toast.LENGTH_SHORT).show();
            return;
        }
        btnEnviarPostulacion.setEnabled(false);
        btnEnviarPostulacion.setText("ENVIANDO...");
        String userId = auth.getCurrentUser().getUid();
        Map<String, Object> postulacion = new HashMap<>();
        postulacion.put("userId", userId);
        postulacion.put("postId", postId);
        postulacion.put("tituloTrabajo", title);
        postulacion.put("descripcionTrabajo", description);
        postulacion.put("nombreCV", nombreCV);
        postulacion.put("carta", carta);
        postulacion.put("portafolio", portafolio);
        postulacion.put("linkedin", linkedin);
        postulacion.put("estado", "Enviada");
        postulacion.put("fecha", Timestamp.now());
        db.collection("postulaciones")
                .add(postulacion)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            this,
                            "Postulación enviada correctamente",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {
                    btnEnviarPostulacion.setEnabled(true);
                    btnEnviarPostulacion.setText("ENVIAR POSTULACIÓN");
                    Toast.makeText(
                            this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
    private String obtenerNombreArchivo(Uri uri) {
        String nombre = "CV seleccionado";
        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            int indiceNombre = cursor.getColumnIndex(
                    OpenableColumns.DISPLAY_NAME
            );
            if (indiceNombre >= 0 && cursor.moveToFirst()) {
                nombre = cursor.getString(indiceNombre);
            }

            cursor.close();
        }

        return nombre;
    }
}