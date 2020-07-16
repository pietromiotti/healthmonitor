package com.example.healthmonitor.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.healthmonitor.R;
import com.example.healthmonitor.RecordAdapter;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.google.android.material.button.MaterialButton;


/*Fragment che contiene la lista di tutti i miei record */
public class RecordsFragment extends Fragment {
    private DatabaseManager databaseManager;

    private RecordAdapter recordAdapter;

    public RecordsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.databaseManager = DatabaseManager.getInstanceDb(getActivity().getApplicationContext());
        /*Creazione del record Adapter con la lista dei record già filtrata*/
        recordAdapter = new RecordAdapter(getActivity(), databaseManager, databaseManager.filteredList);
        View view =  inflater.inflate(R.layout.fragment_records, container, false);
        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Create recordList */
        RecyclerView recyclerView = getActivity().findViewById(R.id.recordListCards);
        MaterialButton filterButton = getActivity().findViewById(R.id.filterButton);

        filterButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_filters, null));
        /*Create New layout Manager - Gestisco la rotazione dello schermo posizionando due record per schermata*/
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }

        /*Bind reclycler and adapter */
        recyclerView.setAdapter(recordAdapter);
    }

}
