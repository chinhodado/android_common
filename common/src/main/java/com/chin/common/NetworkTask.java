package com.chin.common;

import org.jsoup.Jsoup;

import android.os.AsyncTask;
import android.util.Log;

/**
 * An AsyncTask that performs a network request in the background
 */
public class NetworkTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String json = null;
        try {
            json = Jsoup.connect(params[0]).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            Log.e("frdict", "Error with network request", e);
        }
        return json;
    }
}
