package com.example.a2_museum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.SearchView;
public class MainActivity extends AppCompatActivity {
Button openResultButton;
ImageButton openFilterButton;
SearchView landingSearchView;
    private void performSearch(String query) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("searchTerm", query); // suchbegriff übergeben
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Search view
        landingSearchView = findViewById(R.id.mainSearchView);
        landingSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query); // suchen
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  X  nochmal schauen ob funkt ??
                return false;
            }
        });

        // button fuer search ergebnisse
        openResultButton = findViewById(R.id.openResultButton);
        openResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = landingSearchView.getQuery().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
            }
        });


        openFilterButton = findViewById(R.id.openFilterButton);
        //such eingabe weitergeben
        String searchTerm = landingSearchView.getQuery().toString().trim();

        openFilterButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FilterActivity.class);
            intent.putExtra("searchTerm", searchTerm);
            startActivity(intent);
        });


    }
}