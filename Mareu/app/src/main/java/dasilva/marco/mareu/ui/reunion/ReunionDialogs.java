package dasilva.marco.mareu.ui.reunion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Calendar;

import dasilva.marco.mareu.R;
import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

public class ReunionDialogs {

    private Context context;
    private EditText subjectEditText, participantEditText;
    private Spinner placeSpinner;
    private TextView timeTextView, dateTextView;
    private String subject, lieu, participants;
    private String  date = "Date";
    private String heure = "Heure";
    private ReunionApiService apiService;
    private String[] sales;
    private ArrayAdapter<String> spin_adapter;
    private  DatePickerDialog datePickerDialog;
    private  TimePickerDialog timePickerDialog;
    private ReunionListRecyclerViewAdapter adapter;

    public ReunionDialogs(Context context){
        this.context = context;
        apiService = DI.getReunionApiService();
    }

    public void createDialogToSetNewReunion(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_reunion_dialog, null);
        final AlertDialog.Builder setReunionDialog = new AlertDialog.Builder(context);

        //View for Alert Dialog
        subjectEditText = (EditText) view.findViewById(R.id.subject_editText);
        timeTextView = (TextView) view.findViewById(R.id.txtView_time);
        dateTextView = (TextView) view.findViewById(R.id.txtView_date);
        placeSpinner = (Spinner) view.findViewById(R.id.place_Spinner);
        participantEditText = (EditText) view.findViewById(R.id.participant_editText);
        sales = apiService.getSalles();
        spin_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sales);
        spin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        placeSpinner.setAdapter(spin_adapter);


        setReunionDialog.setView(view);
        setReunionDialog.setTitle("Ajoutez une réunion");
        setReunionDialog.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    getNewReunion();
            }
        });
        setReunionDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
               date = "Date";
               heure = "Heure";
            }
        });
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeTextView.setText(hour + ":" + minute);
                        heure = hour + ":" + minute;
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }

        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        dateTextView.setText(mDay + "/" + (mMonth +1) + "/" + mYear);
                        date = mDay + "/" + (mMonth +1) + "/" + mYear;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        setReunionDialog.show();
    }

    private void getNewReunion() {
        String errorInDialog = null;
        subject = subjectEditText.getText().toString();
        lieu = placeSpinner.getSelectedItem().toString();
        participants = participantEditText.getText().toString();
        String[] email = participants.split(", ");
        int count = 0;
        for (String emailAdress : email) {
            if (Patterns.EMAIL_ADDRESS.matcher(emailAdress).matches()) {
                count++;
            }
        }
        if (date != "Date" && heure != "Heure" && !subject.isEmpty() && count == email.length) {
            Reunion reunion = new Reunion(Reunion.getRandomColorAvatar(), date, heure, lieu, subject, participants);
            apiService.addReunion(reunion);
            date = "Date";
            heure = "Heure";
            adapter.notifyDataSetChanged();
        } else {
            if (date == "Date") {
                errorInDialog = "Date";
            }
            if (heure == "Heure"){
                errorInDialog = "Heure";
            }
            if (subject.isEmpty()){
                errorInDialog = "Sujet de la réunion";
            }
            if (count != email.length){
                errorInDialog = "Email";
            }

            Toast.makeText(context, "Veuillez remplir la case" + " " + errorInDialog, Toast.LENGTH_SHORT).show();
            createDialogToSetNewReunion();
            subjectEditText.setText(subject);
            placeSpinner.setSelection(spin_adapter.getPosition(lieu));
            dateTextView.setText(date);
            timeTextView.setText(heure);
            participantEditText.setText(participants);
        }
    }

    public void setAdapter(ReunionListRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
