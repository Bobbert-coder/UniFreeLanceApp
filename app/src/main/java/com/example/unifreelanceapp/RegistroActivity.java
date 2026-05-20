package com.example.unifreelanceapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {

    EditText etNombre, etEmail, etPass, etCarrera;
    Spinner spRol;
    TextView btnRegistrar;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        etNombre = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        etCarrera = findViewById(R.id.etCareer);
        spRol = findViewById(R.id.spRole);
        btnRegistrar = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String[] roles = {"Estudiante", "Empresa"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        spRol.setAdapter(adapterSpinner);

        btnRegistrar.setOnClickListener(v -> registrarUsuario(v));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lyregistro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registrarUsuario(View v) {
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String carrera = etCarrera.getText().toString();
        String rol = spRol.getSelectedItem().toString();

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || (carrera.isEmpty()&&rol.equals("Estudiante"))){
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Error")
                    .setMessage("Por favor llene todos los campos")
                    .setPositiveButton("Ok", (dialog, which) -> {
                    })
                    .show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String userId = mAuth.getCurrentUser().getUid();

                        Map<String, Object>user = new HashMap<>();
                        user.put("nombre", nombre);
                        user.put("email", email);
                        user.put("carrera", carrera);
                        user.put("rol", rol);

                        db.collection("usuarios")
                                .document(userId)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                                    finish();
                                });
                    }
                    else
                        Toast.makeText(this, "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();

                });
    }
}