package com.example.unifreelanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unifreelanceapp.Post;
import com.example.unifreelanceapp.PostAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
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
        adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnActualizar = findViewById(R.id.btnRefresh);
        btnPerfil = findViewById(R.id.btnPerfil);



        db = FirebaseFirestore.getInstance();

        loadPosts();

        btnPerfil.setOnClickListener(v -> {
            Intent intentPerfil = new Intent(HomeActivity.this, Profile.class);
            startActivity(intentPerfil);
        });

        btnCreatePost.setOnClickListener( v -> {
            Intent intentCrear = new Intent(HomeActivity.this, CreatePostActivity.class);
            startActivity(intentCrear);
        });

        btnActualizar.setOnClickListener(v -> loadPosts());
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