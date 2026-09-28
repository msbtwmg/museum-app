package com.example.a2_museum;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class DetailActivity extends AppCompatActivity {

    PhotoView detailImage;
    ImageView zoomIcon;
    TextView detailTitle, detailArtist, detailDate, detailMedium, detailSize, detailDescription, detailPlace;
    Button openResultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // detail views
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailArtist = findViewById(R.id.detailArtist);
        detailDate = findViewById(R.id.detailDate);
        detailMedium = findViewById(R.id.detailMedium);
        detailSize = findViewById(R.id.detailSize);
        detailPlace = findViewById(R.id.detailPlace);
        detailDescription = findViewById(R.id.detailDescription);

        zoomIcon = findViewById(R.id.zoomIcon);
        // artwork objekt
        Artwork artwork = (Artwork) getIntent().getSerializableExtra("artwork");

        if (artwork != null) {
            //deatils
            detailTitle.setText(artwork.getTitle());
            detailArtist.setText("by " + artwork.getArtist());

            setBoldLabel(detailDate, "Date:\n", artwork.getDate());
            setBoldLabel(detailMedium, "Medium:\n", artwork.getMedium());
            setBoldLabel(detailSize, "Dimensions:\n", artwork.getDimensions());
            setBoldLabel(detailPlace, "Place:\n", artwork.getPlace());

            // html richtig anzeige
            String description =Html.fromHtml(artwork.getDescription(), Html.FROM_HTML_MODE_LEGACY).toString().trim();

            setBoldLabel(detailDescription, "Description:\n", description);
            // artwork bilder
            Glide.with(this).load(artwork.getUrl()).into(detailImage);

            // ZOOM
                // bild zooms
            if (artwork.isZoomable()) {
                detailImage.setZoomable(true);
                Toast.makeText(this, "Pinch to Zoom!", Toast.LENGTH_SHORT).show();
            } else {
                detailImage.setZoomable(false);
            }

            // ZOOM ICON
                // animation icon anzeige
            detailImage.setOnClickListener(v -> {
                if (zoomIcon.getVisibility() != View.VISIBLE) {
                    zoomIcon.setAlpha(0f);
                    zoomIcon.setVisibility(View.VISIBLE);
                    zoomIcon.animate()
                            .alpha(0.4f)
                            .setDuration(400)
                            .start();

                    // stop  anzeige
                    zoomIcon.postDelayed(() -> {
                        zoomIcon.animate()
                                .alpha(0f)
                                .setDuration(300)
                                .withEndAction(() -> zoomIcon.setVisibility(View.GONE))
                                .start();
                    }, 3000);
                }
            });


        } else {
            Toast.makeText(this, "Artwork Details not available :(", Toast.LENGTH_SHORT).show();
            finish();
        }

        openResultButton = findViewById(R.id.openResultButton);
        openResultButton.setOnClickListener(view -> finish()) ; // finish > letzte scroll postiion zurück
    }

    // bold labels fuer die detail anzeige
    private void setBoldLabel(TextView view, String label, String value) {
        SpannableString styledText = new SpannableString(label + value);
        styledText.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(styledText);
    }

}
