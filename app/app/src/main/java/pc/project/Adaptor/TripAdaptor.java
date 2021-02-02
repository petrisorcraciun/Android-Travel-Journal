package pc.project.Adaptor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pc.project.Activities.Trip.UpdateTrip;
import pc.project.Activities.Trip.ViewTrip;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.R;


public class TripAdaptor extends RecyclerView.Adapter<TripAdaptor.TripHolder> {
    private List<Trip> trips;
    private Context ct;


    public TripAdaptor(Context context, List<Trip> trips) {
        this.ct = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.trip_item_row, parent, false);
        return new TripHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        Trip currentNote = trips.get(position);
        holder.bind(currentNote);

        holder.tripName.setText(currentNote.getName());
        holder.tripDestionation.setText(currentNote.getDestination());
        //holder.tripType.setText(currentNote.getType());
        holder.tripPrice.setText(String.valueOf(currentNote.getPrice()) + " Euro");
        holder.tripRating.setRating(currentNote.getRating());

        if(currentNote.getImage() != null )
        {
            if(currentNote.getImage().length() > 0)
            {
                File imageFile = new File(currentNote.getImage());
                if(imageFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(currentNote.getImage());
                    holder.avatar.setImageBitmap(myBitmap);
                    holder.avatar.setClipToOutline(true);
                }
            }
        }

        if(currentNote.isFav())
        {
            DrawableCompat.setTint(holder.fav.getDrawable(), ContextCompat.getColor(ct.getApplicationContext(), R.color.red));
        }


        if(currentNote.getType().equals("CITY_BREAK") || currentNote.getType().equals("City break")) {
            holder.tripType.setImageResource(R.drawable.ic_baseline_location_city_24);
        } else if (currentNote.getType().equals("SEA_SIDE") || currentNote.getType().equals("Seaside")) {
            holder.tripType.setImageResource(R.drawable.ic_baseline_beach_access_24);
        } else {
            holder.tripType.setImageResource(R.drawable.ic_baseline_montains_24);
        }

    }



    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder {

        private TextView tripName;
        private TextView tripDestionation;
        private ImageView tripType;
        private TextView tripPrice;
        private RatingBar tripRating;
        private ImageView fav, avatar;
        private Trip trip;
        public static final String TRIP_KEY = "TRIP_KEY";
        private AppDatabase appDatabase;

        void bind(@NonNull final Trip trip) {
            this.trip = trip;
        }

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            tripName                = itemView.findViewById(R.id.tripName);
            tripDestionation        = itemView.findViewById(R.id.tripDestionation);
            tripType                = itemView.findViewById(R.id.tripType);
            tripPrice               = itemView.findViewById(R.id.tripPrice);
            tripRating              = itemView.findViewById(R.id.tripRating);
            fav                     = itemView.findViewById(R.id.fav);
            avatar                  = itemView.findViewById(R.id.avatar);

            itemView.setOnClickListener(displayTripEvent());
            itemView.setOnLongClickListener(updateTripEvent());


            fav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(trip.isFav()){
                        appDatabase = AppDatabase.createDatabase(itemView.getContext());
                        appDatabase.tripDao().changeFav(trip.getId(), false);
                        DrawableCompat.setTint(fav.getDrawable(), ContextCompat.getColor(ct.getApplicationContext(), R.color.black));
                        Snackbar.make(v, "Remove from favorites.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    } else {
                        appDatabase = AppDatabase.createDatabase(itemView.getContext());
                        appDatabase.tripDao().changeFav(trip.getId(), true);
                        DrawableCompat.setTint(fav.getDrawable(), ContextCompat.getColor(ct.getApplicationContext(), R.color.red));
                        Snackbar.make(v, "Added to favorites.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });

        }



        
        private View.OnClickListener displayTripEvent() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ViewTrip.class);
                    intent.putExtra("TRIP_KEY", trip);
                    v.getContext().startActivity(intent);
                }
            };
        }


        private View.OnLongClickListener updateTripEvent() {
            return new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(v.getContext(), UpdateTrip.class);
                    intent.putExtra(TRIP_KEY, trip);
                    v.getContext().startActivity(intent);
                    return false;
                }
            };
        }


    }




}
