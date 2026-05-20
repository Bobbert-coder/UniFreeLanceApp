package com.example.unifreelanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    TextView btnLogin, btnRegister;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            obtenerRolYAbrirHome();
        }

        btnLogin.setOnClickListener(view -> {

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Bienvenido 😎", Toast.LENGTH_SHORT).show();
                            obtenerRolYAbrirHome();
                        } else {
                            Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        btnRegister.setOnClickListener(view -> {
            Intent intentRegistrar = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intentRegistrar);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void obtenerRolYAbrirHome() {

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    String role = documentSnapshot.getString("rol");

                    System.out.println(role);

                    Intent intentlogin = new Intent(MainActivity.this, HomeActivity.class);
                    intentlogin.putExtra("rol", role);
                    startActivity(intentlogin);
                    finish();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error al obtener rol", Toast.LENGTH_SHORT).show();
                });
    }
}