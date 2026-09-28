package com.example.a2_museum;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;
public class ArtworkListAdapt extends RecyclerView.Adapter<ArtworkListAdapt.ArtworkViewHolder> {

    private Context context;
    private List<Artwork> artworkList;

    public ArtworkListAdapt(Context context, List<Artwork> artworkList) {
        this.context = context;
        this.artworkList = artworkList;
    }

    @NonNull
    @Override
    public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.artwork, parent, false);
        return new ArtworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkViewHolder holder, int position) {
        Artwork artwork = artworkList.get(position);
        holder.titleText.setText(artwork.getTitle());
        holder.artistText.setText(artwork.getArtist());
        holder.yearText.setText(artwork.getDate());
        Glide.with(context).load(artwork.getUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("artwork", artwork); // ganze klasse
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return artworkList.size();
    }
    static class ArtworkViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText, artistText, yearText;

        public ArtworkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            titleText = itemView.findViewById(R.id.itemTitle);
            artistText = itemView.findViewById(R.id.itemArtist);
            yearText = itemView.findViewById(R.id.itemYear);
        }
    }
}
