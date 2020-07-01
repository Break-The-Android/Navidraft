package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navidraft.Model.TravelHistoryModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class HomeMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, DistanceMatrixAsyncTask.Geo {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    FirebaseStorage FireStorage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseTravel;

    public static String FinalDisplayName = "";
    public static String FinalEmail = "";
    public static String FinalPhoneNumber = "";
    public static String FinalPassword = "";
    public static String FinalID = "";
    public static String FinalIMAGEURL = "";
    public static String FINALTRAVELTYPE = "";
    public static String FINALUNIT = "";
    public static String FINALTHEME = "";
    String parameter;
    boolean doubleBackToExitPressedOnce = false;

    public static String FINALDURATION = "";
    public static String FINALDISTANCE = "";

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mAuth=FirebaseAuth.getInstance();

        FireStorage = FirebaseStorage.getInstance();
        storageReference = FireStorage.getReference();

        final FirebaseUser mUser = mAuth.getCurrentUser();
        final String UID = mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabaseTravel = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(encodeUserEmail(userauthnow.getEmail().toLowerCase()))){
                    String DISPLAY = dataSnapshot.child(parameter).child("dname").getValue().toString();
                    String MAIL = dataSnapshot.child(parameter).child("uname").getValue().toString();
                    String PASSWORD = dataSnapshot.child(parameter).child("pword").getValue().toString();
                    String PHONE = dataSnapshot.child(parameter).child("phone").getValue().toString();
                    String TRAVEL = dataSnapshot.child(parameter).child("travelType").getValue().toString();
                    String UNIT = dataSnapshot.child(parameter).child("unit").getValue().toString();
                    String URLData = dataSnapshot.child(parameter).child("imageURL").getValue().toString();
                    String theme = dataSnapshot.child(parameter).child("theme").getValue().toString();

                    FinalDisplayName = DISPLAY;
                    FinalEmail = MAIL;
                    FinalPassword = PASSWORD;
                    FinalPhoneNumber = PHONE;
                    FINALTRAVELTYPE = TRAVEL;
                    FINALUNIT = UNIT;
                    FinalIMAGEURL = URLData;
                    FINALTHEME = theme;

                    if(FINALTHEME.equals("Classic")){
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.drivetheme));
                    }else if(FINALTHEME.equals("Midnight")){
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.mapstyle));
                    }

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                    TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                    tv.setText("Welcome, " + DISPLAY);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Centre Device on current Location
        getDeviceLocation();

        // Google Directions Execution for Routes
        getMyLocation();
    }

    PlacesClient placesClient;

    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final float DEFAULT_ZOOM = 15f;

    private TextView DurationTV;
    private TextView DistanceTV;
    private ImageButton profile;
    FloatingActionButton RouteInfoSummary;
    FloatingActionButton SaveDestination;

    // Variables
    private boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private MarkerOptions mMarkerOptions;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    public static String THMAddress;
    public static String THMTime;
    public static String THMDistance;
    public static String THMSTARTPOINT;
    public static String THMPLACENAME;

    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_map);

        String apiKey = "AIzaSyAgQRJcsmBfDSbb9hqgoTWs1GkXllL-DAI";

        profile = findViewById(R.id.profileBTN);
        RouteInfoSummary = findViewById(R.id.RouteInfoFAB);
        SaveDestination = findViewById(R.id.SaveDestinationFAB);

        RouteInfoSummary.hide();
        SaveDestination.hide();

        RouteInfoSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On FAB Clicked
                try {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeMap.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.summary, null);
                    dialogBuilder.setView(dialogView);

                    TextView TEMPDURATION = (TextView) dialogView.findViewById(R.id.RouteDurationTXT);
                    TextView TEMPDISTANCE = (TextView) dialogView.findViewById(R.id.RouteDistanceTXT);

                    TEMPDISTANCE.setText(FINALDISTANCE);
                    TEMPDURATION.setText(FINALDURATION);

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                }catch (Exception f){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                    tv.setText(f.toString());
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });

        SaveDestination.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // Random Generated ID
                String generatedString = UUID.randomUUID().toString();
                String GeneratedID = generatedString.replace("-", "");

                // Date Field
                String pattern = "dd-MMMM-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String date = simpleDateFormat.format(new Date());
                System.out.println(date);

                // Time Field
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                // Save Destination Information in relevant child node
                TravelHistoryModel THM = new TravelHistoryModel(GeneratedID, date , sdf.format(cal.getTime()), THMAddress, THMSTARTPOINT, THMPLACENAME, THMTime, THMDistance, FINALTRAVELTYPE);
                mDatabaseTravel.child("Destinations").child(encodeUserEmail(encodeUserEmail(FinalEmail.toLowerCase()))).child(GeneratedID).setValue(THM);

                // Display Success Message
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
                tv.setText("Destination Saved");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeMap.this);
                Intent profilePage = new Intent(HomeMap.this, Profile.class);
                startActivity(profilePage, options.toBundle());
            }
        });

        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(HomeMap.this, apiKey);
        }
        // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setHint("Where to?");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                final LatLng latLng = place.getLatLng();

                Geocoder geocoder;

                geocoder = new Geocoder(HomeMap.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(mOrigin.latitude, mOrigin.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    String Origin = city+","+state+","+country;
                    String Locate=address;
                    THMAddress=address;
                    THMPLACENAME=place.getName();
                    THMSTARTPOINT=address;

                try{
                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + Locate + "&destinations=" + place.getAddress() + "&mode=driving&language=fr-FR&avoid=tolls&key=AIzaSyAgQRJcsmBfDSbb9hqgoTWs1GkXllL-DAI";
                    new DistanceMatrixAsyncTask(HomeMap.this).execute(url);
                }catch (Exception v){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                    TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                    tv.setText(v.toString());
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }

                mDestination = latLng;
                mMap.clear();
                mMarkerOptions = new MarkerOptions().position(mDestination).title("Destination");
                mMap.addMarker(mMarkerOptions);
                if(mOrigin != null && mDestination != null)
                    drawRoute();
                    RouteInfoSummary.show();
                    SaveDestination.show();
                    THMAddress = place.getAddress();
            }

            @Override
            public void onError(@NonNull Status status) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                tv.setText(status.getStatusMessage());
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
        getLocationPermission();
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getDeviceLocation(){

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }else{
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.error_toast, (ViewGroup) findViewById(R.id.errorcustomtoastlayout));
                            TextView tv = (TextView) layout.findViewById(R.id.ErrorToastTXT);
                            tv.setText("Location Not Found");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
    }
}

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(HomeMap.this);
    }

    private void getMyLocation(){

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
                if(mOrigin != null && mDestination != null)
                    drawRoute();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                mMap.setMyLocationEnabled(true);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,mLocationListener);

                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        mDestination = latLng;
                        mMap.clear();
                        mMarkerOptions = new MarkerOptions().position(mDestination).title("Destination");
                        mMap.addMarker(mMarkerOptions);
                        if(mOrigin != null && mDestination != null)
                            drawRoute();
                    }
                });

            }else{
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                },100);
            }
        }
    }

    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.Google_Maps_API_Key);

        String mode = "mode="+FINALTRAVELTYPE;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+mode+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void setDouble(final String min) {


        mDatabase= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser userauthnow = FirebaseAuth.getInstance().getCurrentUser();
        parameter = encodeUserEmail(userauthnow.getEmail().toLowerCase());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String res[]=min.split(",");
                Double minimum=Double.parseDouble(res[0])/60;
                int dist=Integer.parseInt(res[1])/1000;
                FINALDURATION = ((int) (minimum / 60) + " Hours " + (int) (minimum % 60) + " Minutes");
                THMTime = ((int) (minimum / 60) + " Hours " + (int) (minimum % 60) + " Minutes");

                if(FINALUNIT.equals("Imperial")){
                    FINALDISTANCE = (String.format("%.2f",dist/1.609) + " Miles");
                    THMDistance = (String.format("%.2f",dist/1.609) + " Miles");
                }else if(FINALUNIT.equals("Metric")){
                    FINALDISTANCE = (dist + " Kilometers");
                    THMDistance = (dist + " Kilometers");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                // Check for map theme
                if(FINALTHEME.equals("Classic")){
                    lineOptions.addAll(points);
                    lineOptions.width(15);
                    lineOptions.color(Color.RED);
                }else if(FINALTHEME.equals("Midnight")){
                    lineOptions.addAll(points);
                    lineOptions.width(15);
                    lineOptions.color(Color.WHITE);
                }
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);
            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.customtoastlayout));
        TextView tv = (TextView) layout.findViewById(R.id.ToastTXT);
        tv.setText("Click back again to exit");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
