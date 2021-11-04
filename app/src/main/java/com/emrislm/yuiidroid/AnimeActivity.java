package com.emrislm.yuiidroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AnimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        // get references to widgets
        ImageView coverImageView = (ImageView) findViewById(R.id.activity_coverImageView);
        TextView titleTextView = (TextView) findViewById(R.id.activity_titleTextView);
        TextView episodesTextView = (TextView) findViewById(R.id.activity_episodesTextview);
        TextView scoreTextView = (TextView) findViewById(R.id.activity_scoreTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.activity_descriptionTextView);

        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        String coverUrl = intent.getStringExtra("image_url");
        String title = intent.getStringExtra("title");
        String episodes = intent.getIntExtra("episodes", 0) + " Episodes";
        double score = intent.getDoubleExtra("score", 0.00);
        String description = intent.getStringExtra("synopsis").replace('\n', ' ');

        // display data on the widgets
        Picasso.get().load(coverUrl).into(coverImageView);
        titleTextView.setText(title);
        episodesTextView.setText(String.valueOf(episodes));
        scoreTextView.setText(String.valueOf(score));
        descriptionTextView.setText(description);
    }
}