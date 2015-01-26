package com.alldigitalshop.jeffkrantz.trucktrackr;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by jeffkrantz on 1/8/15.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate=null;
    private Exception exception;

    protected String doInBackground(String... url) {
        BufferedReader in = null;
        String data = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet request = new HttpGet();
            URI website = new URI("http://foodtrukr.herokuapp.com/cft2");
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            // NEW CODE
            String line = in.readLine();

//            Log.e("log_tag", line);
//            Log.e("log_tag", "YEAA");

            return line;

        } catch (Exception e) {
//            Log.e("log_tag", "Error in http connection " + e.toString());
//            Log.e("log_tag", "NOOO");
            return "Error in http connection " + e.toString();
        }
    }

    protected void onPostExecute(String line) {
//        JSONObject jObject = null;
        Log.e("log_tag", "Double YEAAA");
        delegate.processFinish(line);
//        try {
//            JSONObject jObject = new JSONObject(line);
//            String aJsonString = jObject.getString("trucks");
////            Log.e("log_tag", aJsonString);
//
//        } catch (JSONException e) {
//            Log.e("JSON Parser", "Error parsing data " + e.toString());
//        }
    }
}