package com.paavansoni.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private final List<Politician> politicianList = new ArrayList<>();

    private RecyclerView recyclerView;
    private PoliticianAdapter mAdapter;

    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;

    int pos; //position of note
    Politician p; //note selected on tap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new PoliticianAdapter(politicianList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        // gps
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // network
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        //criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            double lat, lon;
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
            if (currentLocation != null) {
                lat = currentLocation.getLatitude();
                lon = currentLocation.getLongitude();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    Toast.makeText(this, "Zip code is " + addresses.get(0).getPostalCode(), Toast.LENGTH_LONG).show();
                    new InfoDownloader(this).execute("" + addresses.get(0).getPostalCode());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Geocoder failed", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Location unavailable", Toast.LENGTH_SHORT).show();
            }
        }

        /*for(int i = 0;i<10;i++){
            politicianList.add(new Politician());
        }
        mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                //launch activity
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.loctionSearch:
                //do search
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Create an edittext and set it to be the builder's view
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //search for stock
                        findloc(et.getText().toString());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancelled
                    }
                });

                builder.setTitle("Please enter a City, State, or Zipcode");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findloc(String entry){
        Toast.makeText(this, "Your entered " + entry, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "You made a short click", Toast.LENGTH_SHORT).show();
        pos = recyclerView.getChildLayoutPosition(v);
        p = politicianList.get(pos);

        Intent intent = new Intent(this, OfficialActivity.class);

        intent.putExtra("POLITICIAN",p);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "You made a long click", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void recieveList(ArrayList<Politician> p) {
        politicianList.clear();
        politicianList.addAll(p);
        mAdapter.notifyDataSetChanged();
    }

    public void displayLoc(String city, String state, String zip) {
        TextView loc = findViewById(R.id.location);
        loc.setText(city + ", " + state + " " + zip);
    }
}
