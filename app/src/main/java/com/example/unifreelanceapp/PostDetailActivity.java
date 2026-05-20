package com.example.unifreelanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvDescription, tvFlecha;
    Button btnPostularme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvFlecha = findViewById(R.id.flechaSalir);
        tvDescription = findViewById(R.id.tvDescription);
        btnPostularme = findViewById(R.id.btnPostularme);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String role = getIntent().getStringExtra("rol");

        tvTitle.setText(title);
        tvDescription.setText(description);

        // OCULTAR BOTON SI ES EMPRESA
        if (role != null && role.equalsIgnoreCase("empresa")) {
            btnPostularme.setVisibility(View.GONE);
        }

        tvFlecha.setOnClickListener(v->{
            finish();
        });

        btnPostularme.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PostDetailActivity.this,
                    PostularmeActivity.class
            );

            intent.putExtra("title", title);
            intent.putExtra("description", description);

            startActivity(intent);

        });

    }
}