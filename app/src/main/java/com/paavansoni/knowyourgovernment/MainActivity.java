package com.paavansoni.knowyourgovernment;

import androidx.annotation.NonNull;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

    private static final String TAG = "MainActivityTest";
    private final List<Politician> politicianList = new ArrayList<>();

    private RecyclerView recyclerView;
    private PoliticianAdapter mAdapter;

    int pos; //position of note
    Politician p; //note selected on tap

    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new PoliticianAdapter(politicianList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(doNetCheck()){
            startLoc();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void doTest() {
        politicianList.add(new Politician());
        mAdapter.notifyDataSetChanged();

    }

    public void startLoc(){
        Log.d(TAG, "startLoc:");
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            Log.d(TAG, "startLoc: asking for permission");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    MY_LOCATION_REQUEST_CODE_ID);
        } else {
            setLocation();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocation();
            }
            else{
                TextView loc = findViewById(R.id.location);
                loc.setText("No data for location");
            }
        }

    }

    @SuppressLint("MissingPermission")
    public void setLocation(){
        Log.d(TAG, "startLoc: permission granted");
        double lat, lon;
        String bestProvider = locationManager.getBestProvider(criteria, true);
        assert bestProvider != null;
        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            lat = currentLocation.getLatitude();
            lon = currentLocation.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
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
                if(doNetCheck()){
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
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
                    builder.setTitle("No Network Connection");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findloc(String entry){
        new InfoDownloader(this).execute(entry);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "You made a short click", Toast.LENGTH_SHORT).show();
        pos = recyclerView.getChildLayoutPosition(v);
        p = politicianList.get(pos);

        TextView loc = findViewById(R.id.location);
        Intent intent = new Intent(this, OfficialActivity.class);

        intent.putExtra("POLITICIAN",p);
        intent.putExtra("LOCATION",loc.getText().toString());
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
        //doTest();
    }

    public void displayLoc(String city, String state, String zip) {
        TextView loc = findViewById(R.id.location);
        String location = city + ", " + state + " " + zip;
        loc.setText(location);
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
