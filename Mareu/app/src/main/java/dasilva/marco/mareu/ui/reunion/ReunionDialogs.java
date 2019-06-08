package dasilva.marco.mareu.ui.reunion;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import dasilva.marco.mareu.R;
import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

public class ReunionDialogs {

    private Context context;
    private EditText subjectEditText, placeEditText, participantEditText;
    private TextView timeTextView, dateTextView;
    private String subject, date, heure, lieu, participants;
    private TimePicker timePicker;
    private ReunionApiService apiService;

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
        placeEditText = (EditText) view.findViewById(R.id.place_editBox);
        participantEditText = (EditText) view.findViewById(R.id.participant_editText);

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

            }
        });
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog();
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });
        setReunionDialog.show();
    }

    private void timePickerDialog(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_picker, null);
        AlertDialog.Builder setTimeDialog = new AlertDialog.Builder(context);
        //view for timePicker dialog
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        setTimeDialog.setView(view);
        setTimeDialog.setTitle("Sélectionner l'heure");

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                heure = hourOfDay + "h:" + minute + "m";
            }
        });
        setTimeDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (heure != null){
                timeTextView.setText(heure);
                }else {
                    Toast.makeText(context, "Veuillez choisir l'heure", Toast.LENGTH_SHORT).show();
                    timePickerDialog();
                }
            }
        });
        setTimeDialog.show();
    }

    private void datePickerDialog(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.date_picker, null);
        AlertDialog.Builder setDateDialog = new AlertDialog.Builder(context);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        setDateDialog.setView(view);
        setDateDialog.setTitle("Sélectionner la date");
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = dayOfMonth + "/" + String.valueOf(monthOfYear + 1)  + "/" + year;
                    }
                });

        setDateDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (date != null){
                dateTextView.setText(date);
                }else {
                    datePickerDialog();
                    Toast.makeText(context, "Veuillez choisir la date", Toast.LENGTH_SHORT).show();

                }
            }
        });
        setDateDialog.show();
    }

    private void getNewReunion(){
        subject = subjectEditText.getText().toString();
        lieu = placeEditText.getText().toString();
        participants = participantEditText.getText().toString();
        if (! lieu.isEmpty() && date != null && heure != null && ! subject.isEmpty() && ! participants.isEmpty()) {
            Reunion reunion = new Reunion(Reunion.getRandomColorAvatar(), date, heure, lieu, subject, participants);
            apiService.addReunion(reunion);
        } else {
            Toast.makeText(context, "Veuillez remplir toutes les informations", Toast.LENGTH_SHORT).show();
            createDialogToSetNewReunion();

            subjectEditText.setText(subject);
            placeEditText.setText(lieu);
            participantEditText.setText(participants);
            dateTextView.setText(dateTextView.getText());
            timeTextView.setText(timeTextView.getText());

        }
    }
}
