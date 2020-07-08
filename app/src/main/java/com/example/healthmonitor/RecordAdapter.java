package com.example.healthmonitor;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.RoomDatabase.MyRoomDatabase;
import com.example.healthmonitor.RoomDatabase.Record;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*  Creating RecordAdapter to Visualize correctly Records as Cards in the Records Fragment"
*   The RecordAdapter receive the init of the Database as argument
* */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    public static List<Record> recordList;
    Context context;
    DatabaseManager databaseManager;
    DialogRecord dialog = new DialogRecord(databaseManager);
    RecordAdapter recordAdapter;

    public RecordAdapter(Context context, DatabaseManager databaseManager){
        this.context = context;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.databaseManager = DatabaseManager.getInstanceDBNOContext();
        View view = LayoutInflater.from(context).inflate(R.layout.record_layout, parent, false);
        return new RecordViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordViewHolder holder, int position) {
        Record record = recordList.get(position);

        holder.textViewMaxPressure.setText(String.valueOf(record.getMax_pressure()));
        holder.textViewMinPressure.setText(String.valueOf(record.getMin_pressure()));
        String weight = String.valueOf(record.getWeight());
        holder.textViewWeight.setText(weight);
        String temperature = String.valueOf(record.getTemperature());
        holder.textViewTemperature.setText(temperature);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate= formatter.format(record.getDate());
        holder.textViewDate.setText(strDate);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


     class RecordViewHolder extends RecyclerView.ViewHolder implements DialogRecord.DialogListener{
        public TextView textViewMinPressure;
        public TextView textViewMaxPressure;
        public TextView textViewTemperature;
        public TextView textViewWeight;
        public TextView textViewDate;
        public Context context;

        public MaterialButton editButton;
        public MaterialButton deleteButton;

        public RecordViewHolder(View view, final Context context) {
            super(view);
            this.textViewDate = view.findViewById(R.id.date);
            this.textViewMaxPressure = view.findViewById(R.id.max_pressure_val);
            this.textViewMinPressure = view.findViewById(R.id.min_pressure_val);
            this.textViewTemperature = view.findViewById(R.id.temperature_val);
            this.textViewWeight = view.findViewById(R.id.weight_val);
            this.editButton = view.findViewById(R.id.edit_record);
            this.deleteButton = view.findViewById(R.id.remove_record);
            this.context = context;
            dialog.setDialogListener(this);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Record record = recordList.get(pos);
                    Bundle args = new Bundle();

                    args.putInt("min_pressure", record.getMin_pressure());
                    args.putInt("max_pressure", record.getMax_pressure());
                    args.putDouble("temperature", record.getTemperature());
                    args.putDouble("weight", record.getWeight());
                    args.putString("Accept", "Modifica");
                    args.putString("Title", "Modifica un Record");
                    args.putString("Description", "Modifica il tuo record");
                    args.putInt("Position", pos);
                    dialog.setArguments(args);
                    dialog.show( ((FragmentActivity) context).getSupportFragmentManager(), "pos");
                    dialog.setCancelable(false);

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Record r = recordList.remove(pos);
                    databaseManager.deleteRecord(r);
                    notifyItemRemoved(pos);
                }
            });

        }

        //TODO solve bug rotation and Reference Adapter for notifyDAta
         @Override
         public void dialogEditRecord(int position, int min_pressure, int max_pressure, double temperature, double weight, Date date) {
            Record oldRecord = recordList.get(position);
            databaseManager.updateRecord(oldRecord, min_pressure, max_pressure, temperature, weight, date);
            notifyDataSetChanged();
            Toast.makeText(context.getApplicationContext(), "EDIT RECORD" + position, Toast.LENGTH_LONG).show();
         }


         @Override
         public void dialogAddRecord(Record r) {
            databaseManager.addRecord(r);
            Toast.makeText(context.getApplicationContext(), "Added in the DB", Toast.LENGTH_LONG).show();
         }

    }



}