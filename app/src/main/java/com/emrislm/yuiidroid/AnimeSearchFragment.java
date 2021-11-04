package com.emrislm.yuiidroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimeSearchFragment extends Fragment implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    private String URL_STRING = "https://api.jikan.moe/v3/search/anime?q=";
    private Anime tempAnime;
    private ArrayList<Anime> animeList;

    // define variables for the widgets
    private EditText editText_animeInput;
    private ListView listView_animesListView;

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
        listView_animesListView = (ListView) view.findViewById(R.id.ListView_animesListView);

        // set the listeners
        editText_animeInput.setOnEditorActionListener(this);
        listView_animesListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String inputText = editText_animeInput.getText().toString();
            URL_STRING = URL_STRING + inputText;

            new getAnimesFromSearch().start();
            Log.d("dinges", "getAnimesFromSearch UITGEVOERD?");
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get anime
        Anime anime = animeList.get(position);

        //create intent
        Intent intent = new Intent(getContext(), AnimeActivity.class);
        intent.putExtra("image_url", anime.getImage_url());
        intent.putExtra("title", anime.getTitle());
        intent.putExtra("episodes", anime.getEpisodes());
        intent.putExtra("score", anime.getScore());
        intent.putExtra("synopsis", anime.getSynopsis());

        this.startActivity(intent);
    }

    public void updateDisplay() {
        if (animeList == null) {
            editText_animeInput.setText("Unable to get results");
            return;
        }

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Anime anime : animeList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("coverUrl", anime.getImage_url());
            map.put("title", anime.getTitle());
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_anime;
        String[] from = {"coverUrl", "title"};
        int[] to = {R.id.coverImageView, R.id.titleTextView};

        // create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, resource, from, to);
        listView_animesListView.setAdapter(adapter);

        Log.d("dinges", "Feed displayed");
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