package com.example.healthmonitor.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmonitor.R;
import com.example.healthmonitor.RecordAdapter;
import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.Record;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;


/*Fragment che visualizza i record del giorno selezionato dal calendario */
public class RecordSelectedFragment  extends Fragment{

        private DatabaseManager databaseManager;
        private RecordAdapter recordAdapter;
        private static List<Record> mytempList;

        public RecordSelectedFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.databaseManager = DatabaseManager.getInstanceDb(getActivity().getApplicationContext());
            Date today = new Date();
            if (CalendarFragment.selectedDate == null){
                today = Calendar.getInstance().getTime();
            }
            else {
                today = CalendarFragment.selectedDate.getTime();
            }
            mytempList = databaseManager.getRecordsDate(today);
            mytempList = addSummaryRecord(mytempList);
            recordAdapter = new RecordAdapter(getActivity(), databaseManager, mytempList);
            View view =  inflater.inflate(R.layout.fragment_records_selected, container, false);
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
            RecyclerView recyclerView = getActivity().findViewById(R.id.recordListCards_selected);

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


        /* Questa funzione controlla se i record di un giorno sono maggiori di uno, in tal caso crea un report i cui valori sono le medie dei valori precedenti,
        * La funzione ritorna la lista aumentata del record summary.
        *
        * La gestione del record di sommario, viene effettuata dal record adapter, ciò che si fa è utilizzare un campo ausiliare nel database per ogni record,
        * se questo campo è positivo, allora il record adapter cambia il titolo e toglie i bottoni ad ogni card, altrimenti crea il record di default.
        *
        * Da notare che tutti i record inseriti normalmente hanno il valore del booleano "isSummary" settato a false, tranne il record inserito manualmente da questa funzione
        * */
        public List<Record> addSummaryRecord(List<Record> mylist){
            int size = mylist.size();
            if(mylist.size()>1){
                Record newRecord = new Record();
                newRecord.setIsSummary(true);
                Record current;
                int totalMinPressure = 0;
                int totalMaxPressure = 0;
                double totalTemperature = 0;
                double totalWeight = 0;
                for (int i =0; i< mylist.size(); i++){
                    current = mylist.get(i);
                    totalMaxPressure = totalMaxPressure + current.getMax_pressure();
                    totalMinPressure = totalMinPressure + current.getMin_pressure();
                    totalTemperature = totalTemperature + current.getTemperature();
                    totalWeight= totalWeight + current.getWeight();

                }
                totalMaxPressure = (int) Math.round(totalMaxPressure/size);
                totalMinPressure = (int) Math.round(totalMinPressure/size);
                totalTemperature =  totalTemperature/(double)size;
                totalWeight =  totalWeight/(double)size;

                newRecord.setMin_pressure(totalMinPressure);
                newRecord.setMax_pressure(totalMaxPressure);
                newRecord.setTemperature(totalTemperature);
                newRecord.setWeight(totalWeight);

                mylist.add(newRecord);
            }
            return mylist;
        }
}

