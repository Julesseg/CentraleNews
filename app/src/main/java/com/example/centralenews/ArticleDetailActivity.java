package com.example.centralenews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ArticleDetailActivity extends AppCompatActivity {

    TextView title;
    TextView author;
    TextView date;
    TextView source;
    TextView description;
    ImageView image;
    Button button;
    JSONObject article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        try {
            article = new JSONObject(intent.getStringExtra("articleData"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        date = findViewById(R.id.date);
        source = findViewById(R.id.source);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        button = findViewById(R.id.button);

        try {
            title.setText(article.getString("title"));

            author.setText(article.getString("author") != null ? article.getString("author") : "");

            String dateString = article.getString("publishedAt");
            String formatedDate = dateString.substring(8, 10) + "/" + dateString.substring(5, 7) + "/" + dateString.substring(0,4);
            date.setText(formatedDate);

            source.setText(article.getJSONObject("source").getString("name"));

            if (article.getString("urlToImage") != null) {
                fetchArticleImage process = new fetchArticleImage();
                Bitmap bitmap = process.execute(article.getString("urlToImage")).get();
                image.setImageBitmap(bitmap);
            }
            description.setText(article.getString("description"));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getString("url")));
                        startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

