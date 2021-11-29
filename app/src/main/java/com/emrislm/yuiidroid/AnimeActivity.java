package com.emrislm.yuiidroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimeActivity extends AppCompatActivity {

    String BASEURL = "https://api.jikan.moe/v3/anime/";

    ImageView coverImageView;
    TextView titleTextView;
    TextView episodesTextView;
    TextView scoreTextView;
    TextView descriptionTextView;
    RecyclerView staffListView;
    ListView statsLinearLayout;

    int MAL_ID;

    AnimeStaff tempStaff;
    ArrayList<AnimeStaff> tempStaffList;

    AnimeStats tempStats;
    ArrayList<Integer> tempStatsList;

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
        statsLinearLayout = (ListView) findViewById(R.id.statsListView);

        MAL_ID = getAnime();
        new GetData().start();
    }

    class GetData extends Thread {
        @Override
        public void run() {
            getStaff(MAL_ID);
            getStats(MAL_ID);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AnimeActivity.this.updateDisplay();
                }
            });
        }
    }

    public void updateDisplay() {
        displayStaff();
        displayStats();
    }

    public void displayStats() {
        if (tempStats == null) {
            return;
        }

        ArrayList<HashMap<String, Integer>> data = new ArrayList<HashMap<String, Integer>>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("watching", tempStats.getWatching());
        map.put("completed", tempStats.getCompleted());
        map.put("on_hold", tempStats.getOn_hold());
        map.put("dropped", tempStats.getDropped());
        map.put("plan_to_watch", tempStats.getPlan_to_watch());
        map.put("total", tempStats.getTotal());
        data.add(map);

        int resource = R.layout.listview_stats;
        String[] from = { "watching", "completed", "on_hold", "dropped", "plan_to_watch", "total" };
        int[] to = { R.id.statsWatching, R.id.statsCompleted, R.id.statsOnHold, R.id.statsDropped, R.id.statsPlanToWatch, R.id.statsTotal };

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        statsLinearLayout.setAdapter(adapter);
    }

    public void displayStaff() {
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
    }

    public void getNews(int id) {
        String STAFFURL = BASEURL + String.valueOf(id) + "/news";

        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(STAFFURL);

        tempStatsList = new ArrayList<Integer>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                tempStats = new AnimeStats();

                tempStats.setWatching(jsonObj.getInt("watching"));
                tempStats.setCompleted(jsonObj.getInt("completed"));
                tempStats.setOn_hold(jsonObj.getInt("on_hold"));
                tempStats.setDropped(jsonObj.getInt("dropped"));
                tempStats.setPlan_to_watch(jsonObj.getInt("plan_to_watch"));
                tempStats.setTotal(jsonObj.getInt("total"));

                Log.d("det", "tempstafflist is gevuld");
            }
            catch (JSONException e) {
                Log.d("det", e.toString());
            }
        }
    }

    public void getStats(int id) {
        String STAFFURL = BASEURL + String.valueOf(id) + "/stats";

        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(STAFFURL);

        tempStatsList = new ArrayList<Integer>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                tempStats = new AnimeStats();

                tempStats.setWatching(jsonObj.getInt("watching"));
                tempStats.setCompleted(jsonObj.getInt("completed"));
                tempStats.setOn_hold(jsonObj.getInt("on_hold"));
                tempStats.setDropped(jsonObj.getInt("dropped"));
                tempStats.setPlan_to_watch(jsonObj.getInt("plan_to_watch"));
                tempStats.setTotal(jsonObj.getInt("total"));

                Log.d("det", "tempstafflist is gevuld");
            }
            catch (JSONException e) {
                Log.d("det", e.toString());
            }
        }
    }

    public void getStaff(int id) {
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

    public int getAnime() {
        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        int mal_id = intent.getIntExtra("mal_id", 0);
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

        return mal_id;
    }
}