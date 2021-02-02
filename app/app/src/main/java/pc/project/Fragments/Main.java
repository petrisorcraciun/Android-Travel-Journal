package pc.project.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pc.project.Activities.Trip.NewTrip;
import pc.project.Adaptor.TripAdaptor;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.Database.Models.TripViewModel;
import pc.project.R;

public class Main extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase appDatabase;
    private List<Trip> trips;
    TripAdaptor tripAdaptor;
    Context appContext;

    public static Main newInstance() {
        return new Main();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        appDatabase = AppDatabase.createDatabase(getActivity());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentData = df.format(new Date());

//        Trip trip1 = new Trip("Hawaii", "Hawaii", "CITY_BREAK", 100, 5, "","28/01/2021", "28/01/2021", true, "28/01/2021");
//        Trip trip2 = new Trip("Name 1", "ddfsdf", "CITY_BREAK", 100, 1, "","28/01/2021", "28/01/2021", false, "28/01/2021");
//        Trip trip3  = new Trip( "Name 1", "ddfsdf", "CITY_BREAK", 100, 3, "","28/01/2021", "28/01/2021", true, "28/01/2021");
//        Trip trip4  = new Trip("Name 1", "ddfsdf", "CITY_BREAK", 100, 3, "","28/01/2021", "28/01/2021", true, "28/01/2021");
//        Trip trip5  = new Trip("Name 1", "ddfsdf", "CITY_BREAK", 100, 3, "","28/01/2021", "28/01/2021", false, "28/01/2021");
//        Trip trip6  = new Trip("Name 1", "ddfsdf", "CITY_BREAK", 100, 3,"", "28/01/2021", "28/01/2021", true, "28/01/2021");
//
//
//          appDatabase.tripDao().deleteAllTrips();
//
//        appDatabase.tripDao().insert(trip1);
//        appDatabase.tripDao().insert(trip2);
//        appDatabase.tripDao().insert(trip3);
//        appDatabase.tripDao().insert(trip4);
//        appDatabase.tripDao().insert(trip5);
//        appDatabase.tripDao().insert(trip6);


        trips                   = appDatabase.tripDao().getAllTrips();
        recyclerView            = view.findViewById(R.id.recycleViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripAdaptor             = new TripAdaptor(getActivity(), trips);
        recyclerView.setAdapter(tripAdaptor);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), NewTrip.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }




}