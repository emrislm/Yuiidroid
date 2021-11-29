package com.emrislm.yuiidroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class AnimeActivity extends AppCompatActivity implements View.OnClickListener {

    String BASEURL = "https://api.jikan.moe/v3/anime/";

    ImageView coverImageView;
    TextView titleTextView;
    TextView episodesTextView;
    TextView scoreTextView;
    TextView descriptionTextView;
    RecyclerView staffListView;
    ImageButton button;

    AnimeStaff tempStaff;
    ArrayList<AnimeStaff> tempStaffList;
    long MAL_ID;

    AdapterStaff adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        // get references to widgets
        coverImageView = (ImageView) findViewById(R.id.activity_coverImageView);
        titleTextView = (TextView) findViewById(R.id.activity_titleTextView);
        episodesTextView = (TextView) findViewById(R.id.activity_episodesTextview);
        scoreTextView = (TextView) findViewById(R.id.activity_scoreTextView);
        descriptionTextView = (TextView) findViewById(R.id.activity_descriptionTextView);
        staffListView = (RecyclerView) findViewById(R.id.activity_staffRecyclerView);
        button = (ImageButton) findViewById(R.id.Button_get);
        button.setOnClickListener(this);

//        getAnime();
//        new GetData().start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_get:
                Log.d("det", "BUTTON");
                getAnime();
                //getStaff(12563);
                new GetData().start();
                break;
        }
    }

    class GetData extends Thread {
        @Override
        public void run() {
            Log.d("det","Run in thread");

            getStaff(1);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("det", "runOnUiThread");
                    AnimeActivity.this.updateDisplay();
                }
            });
        }
    }

    public void updateDisplay() {
        // create List objects
        ArrayList<String> roles = new ArrayList<String>();
        for (AnimeStaff staff : tempStaffList) {
            roles.add(staff.getStaffRole());
        }
        ArrayList<String> names = new ArrayList<String>();
        for (AnimeStaff staff : tempStaffList) {
            names.add(staff.getStaffName());
        }
        ArrayList<String> imgurls = new ArrayList<String>();
        for (AnimeStaff staff : tempStaffList) {
            imgurls.add(staff.getStaffImg());
        }

        // create and set the adapter
        adapter = new AdapterStaff(AnimeActivity.this, imgurls, roles, names);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(AnimeActivity.this, 1, RecyclerView.HORIZONTAL, false);

        staffListView.setLayoutManager(gridLayoutManager);
        staffListView.setAdapter(adapter);

        Log.d("det", "Feed displayed");
    }

    public void getStaff(long id) {
        String STAFFURL = BASEURL + String.valueOf(id) + "/characters_staff";

        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(STAFFURL);

        tempStaffList = new ArrayList<AnimeStaff>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray staffResults  = jsonObj.getJSONArray("staff");

                for (int i = 0; i < staffResults.length(); i++) {
                    tempStaff = new AnimeStaff();
                    JSONObject staff = staffResults.getJSONObject(i);

                    tempStaff.setStaffImg(staff.getString("image_url"));
                    tempStaff.setStaffName(staff.getString("name"));
                    tempStaff.setStaffRole(staff.getJSONArray("positions").getString(0));

                    tempStaffList.add(tempStaff);
                }

                Log.d("det", "tempstafflist is gevuld");
            }
            catch (JSONException e) {
                Log.d("det", e.toString());
            }
        }
    }

    public void getAnime() {
        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        long mal_id = intent.getLongExtra("mal_id", 0);
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

        MAL_ID = mal_id;
    }
}