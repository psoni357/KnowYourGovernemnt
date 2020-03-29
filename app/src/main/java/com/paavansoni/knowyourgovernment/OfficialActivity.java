package com.paavansoni.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficialActivity extends AppCompatActivity {

    Politician p;

    TextView location;
    TextView office;
    TextView name;
    TextView party;

    ImageView profile;
    ImageView partyIcon;

    TextView address;
    TextView number;
    TextView email;
    TextView link;

    TextView addressTag;
    TextView numberTag;
    TextView emailTag;
    TextView linkTag;

    String facebook;
    String twitter;
    String google;
    String youtube;

    ImageView facebookIcon;
    ImageView twitterIcon;
    ImageView googleIcon;
    ImageView youtubeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        location = findViewById(R.id.locText);
        office = findViewById(R.id.officeText);
        name = findViewById(R.id.nameText);
        party = findViewById(R.id.partyText);

        profile = findViewById(R.id.profile);
        partyIcon = findViewById(R.id.partyIcon);

        address = findViewById(R.id.addressValue);
        number = findViewById(R.id.phoneValue);
        email = findViewById(R.id.emailValue);
        link = findViewById(R.id.websiteValue);

        addressTag = findViewById(R.id.addressTag);
        numberTag = findViewById(R.id.phoneTag);
        emailTag = findViewById(R.id.emailTag);
        linkTag = findViewById(R.id.websiteTag);

        facebookIcon = findViewById(R.id.facebookIcon);
        twitterIcon = findViewById(R.id.twitterIcon);
        googleIcon = findViewById(R.id.googleIcon);
        youtubeIcon = findViewById(R.id.youtubeIcon);

        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")) {
            location.setText(intent.getCharSequenceExtra("LOCATION"));
        }
        if (intent.hasExtra("POLITICIAN")) {
            p = (Politician) intent.getSerializableExtra("POLITICIAN");

            //name office and party
            name.setText(p.getName());
            office.setText(p.getOffice());
            party.setText(p.getParty());

            //background color and party icon
            if(p.getParty().equals("Democratic Party")){
                ConstraintLayout b = findViewById(R.id.back);
                b.setBackgroundColor(0xFF3F51B5);
                partyIcon.setImageResource(R.drawable.dem_logo);
                partyIcon.setVisibility(View.VISIBLE);
            }
            else if(p.getParty().equals("Republican Party")){
                ConstraintLayout b = findViewById(R.id.back);
                b.setBackgroundColor(0xFFE91E1E);
                partyIcon.setImageResource(R.drawable.rep_logo);
                partyIcon.setVisibility(View.VISIBLE);
            }
            else{
                ConstraintLayout b = findViewById(R.id.back);
                b.setBackgroundColor(Color.BLACK);
                partyIcon.setVisibility(View.INVISIBLE);
            }

            //address of office
            if(p.getAddress().equals("missing")){
                addressTag.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
            }
            else{
                addressTag.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);

                address.setText(p.getAddress());
                Linkify.addLinks(address,Linkify.ALL);
            }

            //phone number
            if(p.getNumber().equals("missing")){
                number.setVisibility(View.GONE);
                numberTag.setVisibility(View.GONE);
            }
            else{
                number.setVisibility(View.VISIBLE);
                numberTag.setVisibility(View.VISIBLE);

                number.setText(p.getNumber());
                Linkify.addLinks(number,Linkify.ALL);
            }

            //email address
            if(p.getEmail().equals("missing")){
                email.setVisibility(View.GONE);
                emailTag.setVisibility(View.GONE);
            }
            else{
                email.setVisibility(View.VISIBLE);
                emailTag.setVisibility(View.VISIBLE);

                email.setText(p.getEmail());
                Linkify.addLinks(email,Linkify.ALL);
            }

            //website link
            if(p.getWebsite().equals("missing")){
                link.setVisibility(View.GONE);
                linkTag.setVisibility(View.GONE);
            }
            else{
                link.setVisibility(View.VISIBLE);
                linkTag.setVisibility(View.VISIBLE);

                link.setText(p.getWebsite());
                Linkify.addLinks(link,Linkify.ALL);
            }

            //Social media icons
            //Facebook
            if(p.getFacebook().equals("missing")){
                facebookIcon.setVisibility(View.INVISIBLE);
            }
            else{
                facebookIcon.setVisibility(View.VISIBLE);
                facebook = p.getFacebook();
            }
            //Twitter
            if(p.getTwitter().equals("missing")){
                twitterIcon.setVisibility(View.INVISIBLE);
            }
            else{
                twitterIcon.setVisibility(View.VISIBLE);
                twitter = p.getTwitter();
            }
            //Google+
            if(p.getGoogle().equals("missing")){
                googleIcon.setVisibility(View.INVISIBLE);
            }
            else{
                googleIcon.setVisibility(View.VISIBLE);
                google = p.getGoogle();
            }
            //Youtube
            if(p.getYoutube().equals("missing")){
                youtubeIcon.setVisibility(View.INVISIBLE);
            }
            else{
                youtubeIcon.setVisibility(View.VISIBLE);
                youtube = p.getYoutube();
            }

            //profile picture
            if(doNetCheck()){
                Picasso picasso = new Picasso.Builder(this).build();
                picasso.load(p.getPhoto())
                        .error(R.drawable.missing)
                        .placeholder(R.drawable.placeholder)
                        .into(profile);
            }
            else{
                profile.setImageResource(R.drawable.brokenimage);
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

    public void twitterClicked(View v) {
        Intent intent;
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter)); intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitter));
        } startActivity(intent);
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + facebook;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW); facebookIntent.setData(Uri.parse(urlToUse)); startActivity(facebookIntent);
    }

    public void googlePlusClicked(View v) {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", google);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + google)));
        }
    }

    public void youTubeClicked(View v) {
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW); intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youtube));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youtube)));
        }
    }

    public void profileClicked(View v){
        if(profile.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.brokenimage)).getConstantState()){
            return;
        }
        if(profile.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.missing)).getConstantState()){
            return;
        }
        Intent intent = new Intent(this, PhotoDetailActivity.class);

        intent.putExtra("POLITICIAN",p);
        intent.putExtra("LOCATION",location.getText().toString());
        startActivity(intent);
    }

    public void partyIconClicked(View v){
        if(party.getText().toString().equals("Democratic Party")){
            String site = "https://democrats.org/";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(site));
            startActivity(i);
        }
        else if (party.getText().toString().equals("Republican Party")){
            String site = "https://www.gop.com/";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(site));
            startActivity(i);
        }
    }
}
