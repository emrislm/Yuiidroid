package com.emrislm.yuiidroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimeSearchFragment extends Fragment implements View.OnClickListener {

    private String URL_STRING = "https://api.jikan.moe/v3/search/anime?q=";
    private Anime tempAnime;
    private ArrayList<Anime> animeList;

    // define variables for the widgets
    private EditText editText_animeInput;
    private RecyclerView listView_animesListView;
    private ImageButton button_search;
    private Adapter adapter;
    //private ListView listView_animesListView;

    private static final String TAG = "AnimeSearchFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView Method");

        View view = inflater.inflate(R.layout.fragment_anime_search, container, false);

        // get references to the widgets
        editText_animeInput = (EditText) view.findViewById(R.id.EditText_animeInput);
        listView_animesListView = (RecyclerView) view.findViewById(R.id.ListView_animesListView);
        button_search = (ImageButton) view.findViewById(R.id.Button_search);

        // set the listeners
        button_search.setOnClickListener(this);
        listView_animesListView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), listView_animesListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //get anime
                        Anime anime = animeList.get(position);

                        //create intent
                        Intent intent = new Intent(getContext(), AnimeActivity.class);
                        intent.putExtra("image_url", anime.getImage_url());
                        intent.putExtra("title", anime.getTitle());
                        intent.putExtra("episodes", anime.getEpisodes());
                        intent.putExtra("score", anime.getScore());
                        intent.putExtra("synopsis", anime.getSynopsis());

                        getActivity().startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) { }
                })
        );

        return view;
    }

    public void updateDisplay() {
        if (animeList == null) {
            Log.d("dinges", "ERR: Unable to get results");
            return;
        }

        // create a List of Map<String, ?> objects
//        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//        for (Anime anime : animeList) {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("coverUrl", anime.getImage_url());
//            map.put("title", anime.getTitle());
//            data.add(map);
//        }
        ArrayList<String> titles = new ArrayList<String>();
        for (Anime anime : animeList) {
            titles.add(anime.getTitle());
        }
        ArrayList<String> imgurls = new ArrayList<String>();
        for (Anime anime : animeList) {
            imgurls.add(anime.getImage_url());
        }

        // create the resource, from, and to variables
//        int resource = R.layout.listview_anime;
//        String[] from = {"coverUrl", "title"};
//        int[] to = {R.id.coverImageView, R.id.titleTextView};

        // create and set the adapter
        //SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to);
        //listView_animesListView.setAdapter(adapter);
        Log.d("dinges", "JUIST VOOR DE ADAPTER");
        adapter = new Adapter(getActivity(), titles, imgurls);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);

        listView_animesListView.setLayoutManager(gridLayoutManager);
        listView_animesListView.setAdapter(adapter);

        Log.d("dinges", "Feed displayed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_search:
                Log.d("dinges", "KNOP GEDRUKT");
                String inputText = editText_animeInput.getText().toString();
                URL_STRING = URL_STRING + inputText;

                new getAnimesFromSearch().start();
                Log.d("dinges", "getAnimesFromSearch UITGEVOERD?");

                inputText = "";
                break;
        }
    }

    class getAnimesFromSearch extends Thread {
        @Override
        public void run() {
            Log.d("dinges", "WE ZIJN BINNEN GETANIME");
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(URL_STRING);

            animeList = new ArrayList<Anime>();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray results  = jsonObj.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        tempAnime = new Anime();
                        JSONObject result = results.getJSONObject(i);

                        tempAnime.setMal_id(result.getInt("mal_id"));
                        tempAnime.setUrl(result.getString("url"));
                        tempAnime.setImage_url(result.getString("image_url"));
                        tempAnime.setTitle(result.getString("title"));
                        tempAnime.setAiring(result.getBoolean("airing"));
                        tempAnime.setSynopsis(result.getString("synopsis"));
                        tempAnime.setType(result.getString("type"));
                        tempAnime.setEpisodes(result.getInt("episodes"));
                        tempAnime.setScore(result.getDouble("score"));
                        tempAnime.setStart_date(result.getString("start_date"));
                        tempAnime.setEnd_date(result.getString("end_date"));
                        tempAnime.setMembers(result.getInt("members"));
                        tempAnime.setRated(result.getString("rated"));

                        animeList.add(tempAnime);
                    }

                    Log.d("dinges", "tempanime is geladen g");
                }
                catch (JSONException e) {
                    Log.d("dinges", e.toString());
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("dinges", "runOnUiThread");
                    AnimeSearchFragment.this.updateDisplay();
                }
            });
        }
    }
}