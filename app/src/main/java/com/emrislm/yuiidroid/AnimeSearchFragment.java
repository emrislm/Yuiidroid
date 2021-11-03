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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AnimeSearchFragment extends Fragment implements TextView.OnEditorActionListener, View.OnClickListener {

    private final String URL_STRING = "https://api.jikan.moe/v3/anime/1120";
    private Anime tempAnime = null;

    // define variables for the widgets
    private EditText EditText_animeInput;
    private TextView TextView_animeTitle;
    private TextView TextView_animeEpisodes;

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
        EditText_animeInput = (EditText) view.findViewById(R.id.EditText_animeInput);
        TextView_animeTitle = (TextView) view.findViewById(R.id.TextView_animeTitle);
        TextView_animeEpisodes = (TextView) view.findViewById(R.id.TextView_animeEpisodes);

        // set the listeners
        EditText_animeInput.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            new getAnime().execute();
        }

        return false;
    }

    @Override
    public void onClick(View view) {

    }

    class getAnime extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(URL_STRING);

            tempAnime = new Anime();
            if (jsonStr != null) {
                try {
                    JSONObject mainOjbect = new JSONObject(jsonStr);
                    String animeTitle = mainOjbect.getString("title");
                    int animeEpisodes = mainOjbect.getInt("episodes");

                    tempAnime.setTitle(animeTitle);
                    tempAnime.setEpisodes(animeEpisodes);

                    Log.d("dinges", tempAnime.getTitle());
                }
                catch (JSONException e) {
                    Log.d("dinges", e.toString());
                }
            }

            return null;
        }
    }
}