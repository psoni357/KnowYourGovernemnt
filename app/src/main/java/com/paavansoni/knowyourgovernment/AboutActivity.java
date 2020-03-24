package com.paavansoni.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private static final String API_LINK = "https://developers.google.com/civic-information/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView api = findViewById(R.id.APILink);

    }

    public void launchLink(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_LINK));
        startActivity(intent);
    }
}
