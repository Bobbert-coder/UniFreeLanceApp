package com.example.unifreelanceapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    TextView btnMenu, menuCerrarSesion;
    TextView menuPostulaciones, menuPublicaciones;
    TextView btnRefresh, btnPerfil, btnBuscar, btnPosts, btnActualizar;
    Button btnCreatePost;
    PostAdapter adapter;
    List<Post> postList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        String role = getIntent().getStringExtra("rol");
        adapter = new PostAdapter(postList, role);

        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu = findViewById(R.id.btnMenu);
        menuCerrarSesion = findViewById(R.id.menuCerrarSesion);
        menuPostulaciones = findViewById(R.id.menuPostulaciones);
        menuPublicaciones = findViewById(R.id.menuPublicaciones);

        btnMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
        });

        menuCerrarSesion.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Cerrar Sesion")
                    .setMessage("Esta seguro de que desea cerrar su sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        FirebaseAuth.getInstance().signOut();
                        Intent intentLogin = new Intent(HomeActivity.this, MainActivity.class);
                        intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentLogin);

                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnActualizar = findViewById(R.id.btnRefresh);
        btnPerfil = findViewById(R.id.btnPerfil);

        db = FirebaseFirestore.getInstance();

        if (role == null || !role.equalsIgnoreCase("empresa")) {
            btnCreatePost.setVisibility(View.GONE);
        } else {
            btnCreatePost.setVisibility(View.VISIBLE);
        }
        if (role != null && role.equalsIgnoreCase("empresa")) {
            menuPostulaciones.setVisibility(View.GONE);
            menuPublicaciones.setVisibility(View.VISIBLE);
        }
        else {
            menuPostulaciones.setVisibility(View.VISIBLE);
            menuPublicaciones.setVisibility(View.GONE);
        }

        loadPosts();

        btnPerfil.setOnClickListener(v -> {
            Intent intentPerfil = new Intent(HomeActivity.this, Profile.class);
            startActivity(intentPerfil);
        });

        btnCreatePost.setOnClickListener(v -> {
            Intent intentCrear = new Intent(HomeActivity.this, CreatePostActivity.class);
            startActivity(intentCrear);
        });

        btnActualizar.setOnClickListener(v -> loadPosts());

        menuPostulaciones = findViewById(R.id.menuPostulaciones);

        menuPostulaciones.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MisPostulacionesActivity.class);
            startActivity(intent);
        });

        menuPublicaciones.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MisPublicacionesActivity.class);
            startActivity(intent);
        });
    }

    private void loadPosts() {
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();

                    for (var doc : queryDocumentSnapshots) {
                        Post post = doc.toObject(Post.class);
                        post.id = doc.getId();
                        postList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}