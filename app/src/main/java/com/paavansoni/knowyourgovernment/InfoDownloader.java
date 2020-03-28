package com.paavansoni.knowyourgovernment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InfoDownloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "InfoDownloaderAsync";

    private static final String KEY = "AIzaSyB3z2KXWxAdNc6ahwrHih_bL390HiAP4qY";
    private static final String DATA_URL_START = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    private static final String DATA_URL_END = "&address=";

    private String city;
    private String state;
    private String zip;

    @SuppressLint("StaticFieldLeak")
    private MainActivity mainactivity;

    InfoDownloader(MainActivity ma){
        mainactivity = ma;
    }

    @Override
    protected void onPostExecute(String value) {
        if(value.equals("missing")){

        }
        else{
            ArrayList<Politician> p = parseJSON(value);
            mainactivity.displayLoc(city, state, zip);
            mainactivity.recieveList(p);
        }
    }

    private ArrayList<Politician> parseJSON(String value) {
        ArrayList <Politician> pol = new ArrayList<>();
        try{
            JSONObject jObjMain = new JSONObject(value);

            JSONObject normalized = jObjMain.getJSONObject("normalizedInput");
            city = normalized.getString("city");
            state = normalized.getString("state");
            zip = normalized.getString("zip");

            JSONArray offices = jObjMain.getJSONArray("offices");
            JSONArray officials = jObjMain.getJSONArray("officials");

            for(int i = 0;i<offices.length();i++){
                JSONObject position = offices.getJSONObject(i);

                String office = position.getString("name");//     office value

                JSONArray indexes = position.getJSONArray("officialIndices");
                for(int j = 0;j<indexes.length();j++){
                    JSONObject positionInfo = officials.getJSONObject(indexes.getInt(j));

                    String name = positionInfo.getString("name");//     person name
                    String party = "";
                    String address = "";
                    String phones = "";
                    String emails = "";
                    String urls = "";
                    String facebook = "";
                    String twitter = "";
                    String google = "";
                    String youtube = "";
                    String photoUrls = "";


                    if(positionInfo.has("address")){
                        JSONArray addressArray = positionInfo.getJSONArray("address");
                        JSONObject addressInfo = addressArray.getJSONObject(0);

                        if(addressInfo.has("line1")){
                            address = address + addressInfo.getString("line1");
                        }
                        if(addressInfo.has("line2")){
                            address = address +  " " + addressInfo.getString("line2");
                        }
                        if(addressInfo.has("line3")){
                            address = address +  " " + addressInfo.getString("line3");
                        }
                        address = address + " " + addressInfo.getString("city") + " " + addressInfo.getString("state") + " " + addressInfo.getString("zip");
                    }

                    if(positionInfo.has("party")){
                        party = positionInfo.getString("party");
                    }
                    else{
                        party = "Unknown";
                    }
                    if(positionInfo.has("phones")){
                        phones = positionInfo.getJSONArray("phones").getString(0);
                    }
                    if(positionInfo.has("urls")){
                        urls = positionInfo.getJSONArray("urls").getString(0);
                    }
                    if(positionInfo.has("emails")){
                        emails = positionInfo.getJSONArray("emails").getString(0);
                    }
                    if(positionInfo.has("photoUrl")){
                        photoUrls = positionInfo.getString("photoUrl");
                    }

                    if(positionInfo.has("channels")){
                        JSONArray mediaList = positionInfo.getJSONArray("channels");
                        for(int k = 0;k<mediaList.length();k++){
                            JSONObject site = mediaList.getJSONObject(k);

                            if(site.getString("type").equals("Facebook")){
                                facebook = site.getString("id");
                            }
                            else if(site.getString("type").equals("Twitter")){
                                twitter = site.getString("id");
                            }
                            else if(site.getString("type").equals("YouTube")){
                                youtube = site.getString("id");
                            }
                            else if(site.getString("type").equals("GooglePlus")){
                                google = site.getString("id");
                            }
                        }
                    }
                Politician p = new Politician(office, name, party, address, phones, emails, urls, facebook, twitter, google, youtube, photoUrls);
                pol.add(p);
                }

            }

        }
        catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return pol;
    }

    @Override
    protected String doInBackground(String... strings) {
        String builtURL = DATA_URL_START + KEY + DATA_URL_END + strings[0];

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(builtURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());
        }
        catch (Exception e){
            Log.e(TAG, "doInBackground: ", e);
            return "missing";
        }
        return sb.toString();
    }
}
