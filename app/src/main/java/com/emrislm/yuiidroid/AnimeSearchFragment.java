package com.emrislm.yuiidroid;

import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimeSearchFragment extends Fragment implements TextView.OnEditorActionListener, View.OnClickListener {

    private final String URL_STRING = "https://api.jikan.moe/v3/anime/2335";
    private Anime tempAnime;

    // define variables for the widgets
    private EditText editText_animeInput;
    private TextView textView_animeTitle;
    private TextView textView_animeEpisodes;

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
        textView_animeTitle = (TextView) view.findViewById(R.id.TextView_animeTitle);
        textView_animeEpisodes = (TextView) view.findViewById(R.id.TextView_animeEpisodes);

        // set the listeners
        editText_animeInput.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Log.d("dinges", "ACTION GEDRUKT");
            new getAnime().start();
            Log.d("dinges", "GETANIME UITGEVOERD?");
        }

        return false;
    }

    @Override
    public void onClick(View view) {

    }

    public void updateDisplay() {
        // set the textviews
        textView_animeTitle.setText(tempAnime.getTitle());
        textView_animeEpisodes.setText(String.valueOf(tempAnime.getEpisodes()));

        Log.d("dinges", "UPDATEDISPLAY IS AF");
    }

    class getAnime extends Thread {
        @Override
        public void run() {
            Log.d("dinges", "WE ZIJN BINNEN GETANIME");
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(URL_STRING);

            tempAnime = new Anime();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String animeTitle = jsonObj.getString("title");
                    int animeEpisodes = jsonObj.getInt("episodes");

                    tempAnime.setTitle(animeTitle);
                    tempAnime.setEpisodes(animeEpisodes);

                    Log.d("dinges", "tempanime is geladen g");
                }
                catch (JSONException e) {
                    Log.d("dinges", e.toString());
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AnimeSearchFragment.this.updateDisplay();
                    Log.d("dinges", "runOnUiThread");
                }
            });
        }
    }
}