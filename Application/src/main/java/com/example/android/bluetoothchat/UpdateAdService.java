package com.example.android.bluetoothchat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class UpdateAdService extends IntentService {

    public UpdateAdService() {
        super("UpdateAdService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            String url = "http://192.168.1.3/SmartCartWeb/ad.php";
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            parseDetailsList(jsonData);
                        }
//                        else {
//                        }
                    } catch (IOException e) {
                        Log.d("IOExceptione",""+e);
                    } catch (JSONException e) {
                        Log.d("JSONException",""+e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void parseDetailsList(String jsonData) throws JSONException {
        JSONArray jArray = new JSONArray(jsonData);

        String[] Id = new String[jArray.length()];
        String[] Title = new String[jArray.length()];
        String[] Description = new String[jArray.length()];
        String[] BeaconName = new String[jArray.length()];
        String[] UUID = new String[jArray.length()];
        String[] Major = new String[jArray.length()];
        String[] Minor = new String[jArray.length()];
        String[] MacAddress = new String[jArray.length()];
//        String[] pic = new String[jArray.length()];

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);

            Id[i] = jObject.getString("Id");
            Title[i] = jObject.getString("Title");
            Description[i] = jObject.getString("Description");
            BeaconName[i] = jObject.getString("BeaconName");
            UUID[i] = jObject.getString("UUID");
            Major[i] = jObject.getString("Major");
            Minor[i] = jObject.getString("Minor");
            MacAddress[i] = jObject.getString("MacAddress");
//            pic[i] = jObject.getString("pic");

        }
        SharedPreferences AdSharedPreferences = getSharedPreferences("Ad", Context.MODE_PRIVATE);
        SharedPreferences.Editor AdEditor = AdSharedPreferences.edit();

        AdEditor.clear();

        AdEditor.putInt("length",jArray.length()+1);

        Set<String> IdSet = new HashSet<>(Arrays.asList(Id));
        AdEditor.putStringSet("Id", IdSet);

        Set<String> TitleSet = new HashSet<>(Arrays.asList(Title));
        AdEditor.putStringSet("Title", TitleSet);

        Set<String> DescriptionSet = new HashSet<>(Arrays.asList(Description));
        AdEditor.putStringSet("Description", DescriptionSet);

        Set<String> BeaconNameSet = new HashSet<>(Arrays.asList(BeaconName));
        AdEditor.putStringSet("BeaconName", BeaconNameSet);

        Set<String> UUIDSet = new HashSet<>(Arrays.asList(UUID));
        AdEditor.putStringSet("UUID", UUIDSet);

        Set<String> MajorSet = new HashSet<>(Arrays.asList(Major));
        AdEditor.putStringSet("Major", MajorSet);

        Set<String> MinorSet = new HashSet<>(Arrays.asList(Minor));
        AdEditor.putStringSet("Minor", MinorSet);

        Set<String> MacAddressSet = new HashSet<>(Arrays.asList(MacAddress));
        AdEditor.putStringSet("MacAddress", MacAddressSet);

        AdEditor.apply();

    }
}