package com.example.unifreelanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PostularmeActivity extends AppCompatActivity {

    LinearLayout btnSeleccionarCV;
    TextView tvNombreCV, tvPesoCV, tvFlecha2;
    TextView tvTituloTrabajo, tvDescripcionTrabajo;

    private static final int REQUEST_CV = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postularme2);

        tvTituloTrabajo = findViewById(R.id.tvTituloTrabajo);
        tvFlecha2 = findViewById(R.id.tvFlecha2);
        tvDescripcionTrabajo = findViewById(R.id.tvDescripcionTrabajo);

        btnSeleccionarCV = findViewById(R.id.btnSeleccionarCV);
        tvNombreCV = findViewById(R.id.tvNombreCV);
        tvPesoCV = findViewById(R.id.tvPesoCV);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");

        tvTituloTrabajo.setText(title);
        tvDescripcionTrabajo.setText(description);

        btnSeleccionarCV.setOnClickListener(v -> abrirSelectorCV());

        tvFlecha2.setOnClickListener(v->{
            finish();
        });

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
            Uri archivoUri = data.getData();

            String nombre = obtenerNombreArchivo(archivoUri);

            tvNombreCV.setText(nombre);
            tvPesoCV.setText("Archivo seleccionado");

            Toast.makeText(this, "CV seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombre = "CV seleccionado";

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            int indiceNombre = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            if (indiceNombre >= 0 && cursor.moveToFirst()) {
                nombre = cursor.getString(indiceNombre);
            }

            cursor.close();
        }

        return nombre;
    }
}