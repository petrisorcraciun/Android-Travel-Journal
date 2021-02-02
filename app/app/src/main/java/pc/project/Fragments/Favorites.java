package pc.project.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pc.project.Adaptor.TripAdaptor;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.R;

public class Favorites extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase appDatabase;
    private List<Trip> trips;
    TripAdaptor tripAdaptor;
    Context appContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        appDatabase = AppDatabase.createDatabase(getActivity());

        trips                   = appDatabase.tripDao().getFav();
        recyclerView            = view.findViewById(R.id.recycleViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripAdaptor             = new TripAdaptor(getActivity(), trips);
        recyclerView.setAdapter(tripAdaptor);

        return view;
    }
}