package com.example.a2_museum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    RecyclerView artworkRecyclerView;
    List<Artwork> artworkList = new ArrayList<>();
    ArtworkListAdapt adapter;
    SearchView againSearchView;
    ImageButton openFilterButton;
    Button openLandingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String searchTerm = getIntent().getStringExtra("searchTerm");
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchTerm = " "; // default falls search empty
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        // FILTER receiven
        String titleFilter = getIntent().getStringExtra("title");
        String artistFilter = getIntent().getStringExtra("artist");
        String minYear = getIntent().getStringExtra("minYear");
        String maxYear = getIntent().getStringExtra("maxYear");

        if (titleFilter == null) titleFilter = "";
        if (artistFilter == null) artistFilter = "";

        // fuer RecyclerView
        artworkRecyclerView = findViewById(R.id.artworkRecyclerView);

        artworkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtworkListAdapt(this, artworkList);
        artworkRecyclerView.setAdapter(adapter);

        // 2 GRIDS ANSICHT
        // artworkRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            // STAGGERED FÜR ABSTAND IGNORE
        artworkRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        // NEUE suche und filterung
        againSearchView = findViewById(R.id.againSearchView);
        openFilterButton = findViewById(R.id.openFilterButtonAgain);

        againSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                intent.putExtra("searchTerm", query);
                startActivity(intent);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        openFilterButton.setOnClickListener(view -> {
            String newSearchTerm = againSearchView.getQuery().toString().trim();
            Intent intent = new Intent(ResultActivity.this, FilterActivity.class);
            intent.putExtra("searchTerm", newSearchTerm);
            startActivity(intent);
        });

        // URL KOMBI
        String baseUrl = "https://api.artic.edu/api/v1/artworks/search?q=";
        String fields = "&fields=title,artist_display,date_start,date_end,image_id,date_display," +
                "medium_display,dimensions,artwork_type_title,place_of_origin,description,is_zoomable&limit=100"; // limit auf 100 bilder


        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        // freitext weniger eingeschränkt
        urlBuilder.append(Uri.encode(searchTerm)).append("&");

        // filter mit MATCH für mehr accuracy
        if (!artistFilter.isEmpty()) {
            urlBuilder.append("query[bool][must][][match][artist_title]=")
                    .append(Uri.encode(artistFilter)).append("&");
        }
        if (!titleFilter.isEmpty()) {
            urlBuilder.append("query[bool][must][][match][title]=")
                    .append(Uri.encode(titleFilter)).append("&");
        }

        if (minYear != null && !minYear.isEmpty()) {
            urlBuilder.append("query[bool][must][][range][date_start][gte]=")
                    .append(minYear).append("&");
        }

        if (maxYear != null && !maxYear.isEmpty()) {
            urlBuilder.append("query[bool][must][][range][date_end][lte]=")
                    .append(maxYear).append("&");
        }


        urlBuilder.append(fields);
        String url = urlBuilder.toString();

        // API Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    JSONArray dataArray = response.optJSONArray("data");
                    if (dataArray != null && dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.optJSONObject(i);
                            String imageId = data.optString("image_id", "");
                            if (!imageId.isEmpty()) {
                                String title = data.optString("title", "Untitled");
                                String artistFull = data.optString("artist_display", "Unknown");
                                String artistName = artistFull.split("\\(")[0].split("\n")[0].trim();
                                String year = data.optString("date_display", "Unknown");
                                String medium = data.optString("medium_display", "Unknown");
                                String dimensions = data.optString("dimensions", "Unknown");
                                String type = data.optString("artwork_type_title", "Unknown");
                                String place = data.optString("place_of_origin", "Unknown");
                                String description = data.optString("description", "fallback test");


                                // fallbacks funktioniert nicht, null wird als string erkannt ka .....
                                if (description.equals("null")) description = "No Description";
                                if (artistName.equals("null")) artistName = "Unknown Artist";
                                if (title.equals("null")) title = "Untitled";
                                if (place.equals("null") || dimensions.equals("null") ||medium.equals("null") ||year.equals("null") ) description = "Unknown";

                                boolean zoomable = data.optBoolean("is_zoomable", false);

                                /* brauch ich nimma wegen MATCH

                                //  KONTROLLIEREN OB NICHT KOMISCH X
                                int yearStart = data.optInt("date_start", 0);
                                int yearEnd = data.optInt("date_end", 0);

                                boolean passesMinYear = minYear == null || minYear.isEmpty() || yearEnd >= parseIntSafe(minYear);
                                boolean passesMaxYear = maxYear == null || maxYear.isEmpty() || yearStart <= parseIntSafe(maxYear);
                                  */

                                String imageUrl = "https://www.artic.edu/iiif/2/" + imageId + "/full/843,/0/default.jpg";
                                artworkList.add(new Artwork(title, artistName, year, imageUrl, medium, dimensions, type, place, description, zoomable));

                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No artworks found.", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    Toast.makeText(this, "Failed to load artwork.", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);

        // zurück button
        openLandingButton = findViewById(R.id.openLandingButton);
        openLandingButton.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

}
