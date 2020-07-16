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
import com.example.healthmonitor.utils.Converters;
import com.example.healthmonitor.utils.ErrorHandler;
import com.example.healthmonitor.utils.NotificationHandler;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/*  Creating RecordAdapter to Visualize correctly Records as Cards in the Records Fragment"
*   The RecordAdapter receive the init of the Database as argument
* */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder>{
    List<Record> recordList;
    Context context;
    DatabaseManager databaseManager;
    DialogRecord dialog = new DialogRecord(databaseManager);
    RecordAdapter recordAdapter;
    NotificationHandler notificationHandler;

    public RecordAdapter(Context context, DatabaseManager databaseManager, List<Record> list){
        this.context = context;
        this.recordList = list;
        this.notificationHandler = NotificationHandler.getInstanceOfNotificationHandlerNoContext();
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

        holder.textViewMaxPressure.setText(Converters.parseIntToString(record.getMax_pressure()));
        holder.textViewMinPressure.setText(Converters.parseIntToString(record.getMin_pressure()));
        String weight = Converters.parseDoubleToString(record.getWeight());
        holder.textViewWeight.setText(weight);
        String temperature = Converters.parseDoubleToString(record.getTemperature());
        holder.textViewTemperature.setText(temperature);

        if (record.getIsSummary()){
            holder.textViewDate.setText(context.getResources().getString(R.string.mediaGiornalieraText));
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
        else{
            SimpleDateFormat formatter = new SimpleDateFormat(context.getResources().getString(R.string.formatDate));
            String strDate= formatter.format(record.getDate());
            holder.textViewDate.setText(strDate);
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


     class RecordViewHolder extends RecyclerView.ViewHolder implements DialogRecord.DialogListener{
        private TextView textViewMinPressure;
        private TextView textViewMaxPressure;
        private TextView textViewTemperature;
        private TextView textViewWeight;
        private TextView textViewDate;
        public Context context;

        private MaterialButton editButton;
        private MaterialButton deleteButton;


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


            /*Setting del funzionamento del bottone "Edit", salva i valori del record in args, così da mostrargli a schermo come pre-caricati, si veda la gestione in DialogRecord.Java*/
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
                   // means that back key doesn't close the dialog
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

         @Override
         public void dialogEditRecord(int position, int min_pressure, int max_pressure, double temperature, double weight, Date date) {
            Record oldRecord = recordList.get(position);
            /*Controllo se ci sono almeno due parametri inseriti*/
            if (ErrorHandler.checkIfDialogFieldsAreCorrect(min_pressure, max_pressure, temperature, weight)){
                databaseManager.updateRecord(oldRecord, min_pressure, max_pressure, temperature, weight, date);
                notifyItemChanged(position);
                ErrorHandler.editCompleted(context);
                notificationHandler.triggerNotificationInfo();
            }
            else ErrorHandler.showToLessArgument(context);

         }

         @Override
         public void dialogAddRecord(Record r) {
         }
    }
}