package com.example.centralenews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "9e1afcb1d2b34e46aeccaaa433b89cbd";
    private String language = Locale.getDefault().getLanguage();
    public static JSONArray sources = null;
    public List<String> sourceIds = new ArrayList<String>();
    public static JSONArray articles = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String sourcesString = intent.getStringExtra("sources");
        try {
            sources = new JSONObject(sourcesString).getJSONArray("sources");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buildArticleList("google-news-fr");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        String selectedSource = sourceIds.get(selectedId);

        buildArticleList(selectedSource);
        return true;
    }

    public void buildArticleList(String source) {

        fetchArticles process = new fetchArticles();
        String articlesString = null;
        try {
            articlesString = process.execute(apiKey, language, source).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            articles = new JSONObject(articlesString).getJSONArray("articles");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<HashMap<String, String>> listItem = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < articles.length(); i++) {
            JSONObject article = null;
            try {
                article = articles.getJSONObject(i);
                map.put("title", article.getString("title"));
                map.put("description", article.getString("description"));
                map.put("author", article.getString("author") != "null" ? article.getString("author") : "");
                listItem.add(map);
                map = new HashMap<>();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SimpleAdapter sa = new SimpleAdapter(this.getBaseContext(),
                listItem,
                R.layout.layout_item,
                new String[] {"title", "description", "author"},
                new int[] {R.id.title, R.id.description, R.id.author});

        ListView lv = findViewById(R.id.mylistview);
        lv.setAdapter(sa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent articleIntent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                    articleIntent.putExtra("articleData", articles.getJSONObject(position).toString());
                    startActivity(articleIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

