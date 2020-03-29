package com.paavansoni.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    Politician p;

    TextView locationDetail;
    TextView officeDetail;
    TextView nameDetail;
    TextView partyDetail;

    ImageView profileDetail;
    ImageView partyIconDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        locationDetail = findViewById(R.id.locationDetail);
        officeDetail = findViewById(R.id.officeDetail);
        nameDetail = findViewById(R.id.nameDetail);
        partyDetail = findViewById(R.id.partyDetail);

        profileDetail = findViewById(R.id.profileDetail);
        partyIconDetail = findViewById(R.id.partyIconDetail);

        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")) {
            locationDetail.setText(intent.getCharSequenceExtra("LOCATION"));
        }
        if (intent.hasExtra("POLITICIAN")) {
            p = (Politician) intent.getSerializableExtra("POLITICIAN");

            //basic info
            officeDetail.setText(p.getOffice());
            nameDetail.setText(p.getName());
            partyDetail.setText(p.getParty());

            //background color and party icon
            if(p.getParty().equals("Democratic Party")){
                ConstraintLayout b = findViewById(R.id.backDetail);
                b.setBackgroundColor(0xFF3F51B5);
                partyIconDetail.setImageResource(R.drawable.dem_logo);
                partyIconDetail.setVisibility(View.VISIBLE);
            }
            else if(p.getParty().equals("Republican Party")){
                ConstraintLayout b = findViewById(R.id.backDetail);
                b.setBackgroundColor(0xFFE91E1E);
                partyIconDetail.setImageResource(R.drawable.rep_logo);
                partyIconDetail.setVisibility(View.VISIBLE);
            }
            else{
                ConstraintLayout b = findViewById(R.id.backDetail);
                b.setBackgroundColor(Color.BLACK);
                partyIconDetail.setVisibility(View.INVISIBLE);
            }

            //profile picture
            if(doNetCheck()){
                Picasso picasso = new Picasso.Builder(this).build();
                picasso.load(p.getPhoto())
                        .error(R.drawable.missing)
                        .placeholder(R.drawable.placeholder)
                        .into(profileDetail);
            }
            else{
                profileDetail.setImageResource(R.drawable.brokenimage);
            }
        }
    }

    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }
}
