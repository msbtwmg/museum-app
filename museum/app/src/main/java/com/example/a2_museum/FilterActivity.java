package com.example.a2_museum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FilterActivity extends AppCompatActivity {
Button applyFilterButton, resetFilterButton;

ImageButton leaveFilterButton;
EditText inputTitle, inputArtist, inputMinYear, inputMaxYear;
String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FILTER
        inputTitle = findViewById(R.id.inputTitle);
        inputArtist = findViewById(R.id.inputArtist);
        inputMinYear = findViewById(R.id.inputMinYear);
        inputMaxYear = findViewById(R.id.inputMaxYear);
        applyFilterButton = findViewById(R.id.applyFilterButton);
        resetFilterButton = findViewById(R.id.resetFilterButton);
        leaveFilterButton = findViewById(R.id.leaveFilter);

        //searchbar term
        searchTerm = getIntent().getStringExtra("searchTerm");

        // FILTER APPLY FUNKTIONEN
        applyFilterButton.setOnClickListener(v -> {

            //   Filter user eingabe
            String title = inputTitle.getText().toString();
            String artist = inputArtist.getText().toString();
            String minYear = inputMinYear.getText().toString();
            String maxYear = inputMaxYear.getText().toString();

            Intent intent = new Intent(FilterActivity.this, ResultActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("artist", artist);
            intent.putExtra("minYear", minYear);
            intent.putExtra("maxYear", maxYear);
            intent.putExtra("searchTerm", searchTerm);
            startActivity(intent);
        });

        resetFilterButton.setOnClickListener(v -> {
            inputTitle.setText("");
            inputArtist.setText("");
            inputMinYear.setText("");
            inputMaxYear.setText("");
        });

        leaveFilterButton.setOnClickListener(view -> {
            finish();
        });
    }
}