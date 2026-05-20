package com.example.unifreelanceapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    Button btnLogout;
    TextView tvNombre, tvEmail, tvCarrera, tvRol, tvNombreSmall;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnCerrarSesion);
        tvNombre = findViewById(R.id.tvName);
        tvNombreSmall = findViewById(R.id.tvNameSmall);
        tvEmail = findViewById(R.id.tvEmail);
        tvCarrera = findViewById(R.id.tvCareer);
        tvRol = findViewById(R.id.tvRole);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadUserData();

        btnLogout.setOnClickListener(v ->{
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Cerrar Sesion")
                    .setMessage("Esta seguro de que desea cerrar su sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        FirebaseAuth.getInstance().signOut();
                        Intent intentLogin = new Intent(Profile.this, MainActivity.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentLogin);

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.perfil), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUserData() {

        String userId = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser() == null) {
            new AlertDialog.Builder(getBaseContext())
                    .setTitle("Sesión No Iniciada")
                    .setMessage("Por favor inicie sesión antes de ver su perfil")
                    .setPositiveButton("Ok", (dialog, which) -> {
                    })
                    .show();
            finish();
            return;
        }

        db.collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if(documentSnapshot.exists()){

                        String name = documentSnapshot.getString("nombre");
                        String email = documentSnapshot.getString("email");
                        String career = documentSnapshot.getString("carrera");
                        String role = documentSnapshot.getString("rol");

                        tvNombre.setText(name);
                        tvNombreSmall.setText(name);
                        tvEmail.setText(email);
                        tvRol.setText(role);



                        // Mostrar carrera solo si existe
                        if(career.length() > 1){
                            tvCarrera.setText(career);
                        } else {
                            tvCarrera.setText("No aplica");
                        }
                    }
                });
    }

}