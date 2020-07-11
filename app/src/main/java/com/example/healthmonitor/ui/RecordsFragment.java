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

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.healthmonitor.R;
import com.example.healthmonitor.RecordAdapter;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;


public class RecordsFragment extends Fragment {
    private DatabaseManager databaseManager;

    private RecordAdapter recordAdapter;

    public RecordsFragment() {
        // Required empty public constructor
    }


    public static RecordsFragment newInstance(String param1, String param2) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.databaseManager = DatabaseManager.getInstanceDb(getActivity().getApplicationContext());

        recordAdapter = new RecordAdapter(getActivity(), databaseManager, databaseManager.filteredList);
        View view =  inflater.inflate(R.layout.fragment_records, container, false);
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Create recordList */
        RecyclerView recyclerView = getActivity().findViewById(R.id.recordListCards);
        MaterialButton filterButton = getActivity().findViewById(R.id.filterButton);

        filterButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_filters, null));
        /*Create New layout Manager */
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
