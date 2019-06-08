package dasilva.marco.mareu.ui.reunion;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import dasilva.marco.mareu.R;
import dasilva.marco.mareu.di.DI;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reunion_list);
        setViews();
        initList();
        addToFavorites();
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
    }


    private void initList(){
        reunionList = apiService.getReunions();
        adapter = new ReunionListRecyclerViewAdapter(apiService.getReunions());
        recyclerView.setAdapter(adapter);
    }


    private void addToFavorites(){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reunionList.size() < 10) {
                        reunionDialogs.createDialogToSetNewReunion();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "Delete some reunions", Toast.LENGTH_LONG).show();
                    }
                }
            });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.place_item:
                apiService.setListReunionByPlace();
                initList();
                break;
            case R.id.date_item:
                apiService.setListReunionByDate();
                initList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        adapter.notifyDataSetChanged();
        super.onResume();
    }

}
