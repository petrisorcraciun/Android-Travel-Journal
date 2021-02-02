package pc.project.Activities.Trip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import pc.project.Activities.LoginActivity;
import pc.project.Activities.NavigationDrawerActivity;
import pc.project.Adaptor.TripAdaptor;
import pc.project.BuildConfig;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.R;

public class ViewTrip extends AppCompatActivity {

    Intent intent;
    Trip trip;
    TextView tripName;
    TextView tripDestionation;
    TextView tripType;
    TextView tripPrice;
    TextView period;
    RatingBar tripRating;
    public static TextView temperature;
    public String tripDestionationString;
    ImageView imgType;
    ImageView imgWeather;
    ImageView tripPicture;
    private AppDatabase appDatabase;

    private static final String API_KEY = "db6187ea3471ca1e7ddf671798fb3d99";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        trip = (Trip) intent.getSerializableExtra(TripAdaptor.TripHolder.TRIP_KEY);

        temperature             = findViewById(R.id.temperature);
        tripName                = findViewById(R.id.tripName);
        tripDestionation        = findViewById(R.id.tripDestionation);
        //tripType                = findViewById(R.id.tripType);
        tripPrice               = findViewById(R.id.tripPrice);
        tripRating              = findViewById(R.id.tripRating);
        imgWeather              = findViewById(R.id.imgWeather);
        period                  = findViewById(R.id.period);
        tripPicture             = findViewById(R.id.tripPicture);
        tripName.setText(trip.getName());
        tripDestionation.setText(trip.getDestination());
        period.setText("Period: " + trip.getStart_date() + " - " + trip.getEnd_date());

        if(trip.getImage() != null )
        {
            if(trip.getImage().length() > 0)
            {
                File imageFile = new File(trip.getImage());
                if(imageFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(trip.getImage());
                    tripPicture.setImageBitmap(myBitmap);
                    tripPicture.setClipToOutline(true);
                }
            }
        }

        imgType = findViewById(R.id.imgType);
        if(trip.getType().equals("CITY_BREAK") || trip.getType().equals("City break")) {
            imgType.setImageResource(R.drawable.ic_baseline_location_city_24);
        } else if (trip.getType().equals("SEA_SIDE") || trip.getType().equals("Seaside")) {
            imgType.setImageResource(R.drawable.ic_baseline_beach_access_24);
        }
        else {
            imgType.setImageResource(R.drawable.ic_baseline_montains_24);
        }
        tripPrice.setText(trip.getPrice() + " Euro");
        tripRating.setRating(trip.getRating());
        tripDestionationString = trip.getDestination();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showTemperature();

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        initCollapsingToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton share = (FloatingActionButton) findViewById(R.id.share);

        if(trip.isFav()){
            DrawableCompat.setTint(fab.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.red));
        } else {
            DrawableCompat.setTint(fab.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.white));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appDatabase = AppDatabase.createDatabase(getApplicationContext());

                if(trip.isFav()) {
                    appDatabase.tripDao().changeFav(trip.getId(), false);
                    DrawableCompat.setTint(fab.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.red));
                    Snackbar.make(view, "Remove from favorites.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    appDatabase.tripDao().changeFav(trip.getId(), true);
                    DrawableCompat.setTint(fab.getDrawable(), ContextCompat.getColor(getApplicationContext(), R.color.red));
                    Snackbar.make(view, "Added to favorites.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            appDatabase = AppDatabase.createDatabase(getApplicationContext());
            appDatabase.tripDao().deleteTrip(trip.getId());
            Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    public void showTemperature() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + tripDestionationString + "&appid=" + API_KEY + "&units=Metric";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject           = response.getJSONObject("main");
                    JSONArray jsonArray             = response.getJSONArray("weather");
                    JSONObject object               = jsonArray.getJSONObject(0);
                    double temperatureDouble        = jsonObject.getDouble("temp");
                    String description              = object.getString("main");


                    double temperatureRounded = (int) Math.round(temperatureDouble);
                    int temperatureInt        = (int) temperatureRounded;
                    String temp = String.valueOf(temperatureInt + "°C");

                    temperature.setText(temp + " (" + description + ") ");

                    if (description.contains("Cloud")) {
                        imgWeather.setImageResource(R.drawable.clouds);
                    } else if (description.contains("Clear")) {
                        imgWeather.setImageResource(R.drawable.clear);
                    } else if (description.contains("Rain")) {
                        imgWeather.setImageResource(R.drawable.rain);
                    } else if (description.contains("Snow")) {
                        imgWeather.setImageResource(R.drawable.snow);
                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}