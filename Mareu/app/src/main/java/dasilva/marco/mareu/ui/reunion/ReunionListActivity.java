package dasilva.marco.mareu.ui.reunion;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

import dasilva.marco.mareu.R;
import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.events.DeleteReunionEvent;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

public class ReunionListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Reunion> reunionList;
    private ReunionApiService apiService;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ReunionListRecyclerViewAdapter adapter;
    private ReunionDialogs reunionDialogs;
    private String date = null;
    private Spinner placeChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reunion_list);
        setViews();
        initList();
        addReunion();
    }

    private void setViews(){
        toolbar = (Toolbar) findViewById(R.id.reunion_toolbar);
        fab = findViewById(R.id.add_reunion_fab);
        recyclerView = (RecyclerView) findViewById(R.id.reunion_list_recyclerview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        apiService = DI.getReunionApiService();
        setSupportActionBar(toolbar);
        reunionDialogs = new ReunionDialogs(this);
        reunionList = apiService.getReunions();
    }

    private void initList(){
        adapter = new ReunionListRecyclerViewAdapter(reunionList);
        reunionDialogs.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
    }

    private void addReunion(){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        reunionDialogs.createDialogToSetNewReunion();
                        reunionList = apiService.getReunions();
                        initList();

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void filterByplace(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.place_spinner, null);
        final AlertDialog.Builder reunionPlaceDialog = new AlertDialog.Builder(this);
        placeChoice = (Spinner) view.findViewById(R.id.place_choice);
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, apiService.getSalles());
        spin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        placeChoice.setAdapter(spin_adapter);
        reunionPlaceDialog.setView(view);
        reunionPlaceDialog.setTitle("Séléctionnez une salle");
        reunionPlaceDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reunionList =  apiService.setListReunionByPlace(placeChoice.getSelectedItem().toString());
                initList();
            }
        });
        reunionPlaceDialog.show();
    }

    public void filterByDate(){
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                date = mDay + "/" + (mMonth + 1) + "/" + mYear;
                reunionList = apiService.setListReunionByDate(date);
                initList();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.place_item:
               filterByplace();
                break;
            case R.id.date_item:
               filterByDate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteReunion(final DeleteReunionEvent event){
        AlertDialog.Builder deleteReunionDialog = new AlertDialog.Builder(this);
        deleteReunionDialog.setTitle("Êtes vous sûr de vouloir supprimer ?");
        deleteReunionDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                apiService.deleteReunion(event.reunion);
                reunionList.remove(event.reunion);
                initList();
            }
        });
        deleteReunionDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        deleteReunionDialog.show();
    }

}
