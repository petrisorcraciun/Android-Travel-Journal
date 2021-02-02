package pc.project.Database.Models;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pc.project.Database.AppDatabase;

public class TripViewModel extends AndroidViewModel {

    private final List<Trip> trips;

    public TripViewModel(@NonNull Application application) {
        super(application);

        trips = AppDatabase
                .createDatabase(getApplication())
                .tripDao()
                .getAllTrips();
    }

    public List<Trip> getTrips() {
        return trips;
    }
}