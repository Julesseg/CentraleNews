package com.example.centralenews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "9e1afcb1d2b34e46aeccaaa433b89cbd";
    public static TextView text;
    public static JSONArray sources;
    public List<String> sourceIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.textView);

        Intent intent = getIntent();
        String sourcesString = intent.getStringExtra("sources");
        JSONObject sourcesJSON = null;
        try {
            sourcesJSON = new JSONObject(sourcesString);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            JSONArray sources = null;
            try {
                sources = sourcesJSON.getJSONArray("sources");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                MainActivity.sources = sources;
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < sources.length(); i++) {
            String title = null;
            String id;
            try {
                JSONObject source = sources.getJSONObject(i);
                title = source.getString("name");
                id = source.getString("id");
                sourceIds.add(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            menu.add(0, i, 0, title);
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.source_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

    }*/
}
