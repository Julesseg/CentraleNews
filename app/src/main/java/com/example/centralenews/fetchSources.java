package com.example.centralenews;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchSources extends AsyncTask<String, Void, String> {

    private String apiKey = "9e1afcb1d2b34e46aeccaaa433b89cbd";
    private String data;

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL("https://newsapi.org/v2/sources?apiKey=" + apiKey + "&language=" + strings[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
            data = data.substring(4); // cutting out the strange "null" string added at the beginning of every response

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
