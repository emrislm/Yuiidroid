package com.emrislm.yuiidroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<String> titles;
    ArrayList<String> imgurls;
    LayoutInflater inflater;

    Context context;

    public Adapter(Context context, ArrayList<String> titles, ArrayList<String> imgurls) {
        this.titles = titles;
        this.imgurls = imgurls;

        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listview_anime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Log.d("dinges", titles.get(position));
        holder.title.setText(titles.get(position));

        Log.d("dinges", titles.get(position));
        Picasso.get().load(imgurls.get(position)).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextView);
            cover = itemView.findViewById(R.id.coverImageView);
        }
    }
}
