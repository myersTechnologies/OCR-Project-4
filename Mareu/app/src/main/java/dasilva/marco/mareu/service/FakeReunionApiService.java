package dasilva.marco.mareu.service;


import android.widget.DatePicker;

import dasilva.marco.mareu.model.Reunion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class FakeReunionApiService implements ReunionApiService {

    private List<Reunion> listOfReunions = new ArrayList<>();
    private Date date;
    private Date toCompare;

    @Override
    public List<Reunion> getReunions() {
        return listOfReunions;
    }

    @Override
    public void addReunion(Reunion reunion) {
        listOfReunions.add(reunion);
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        listOfReunions.remove(reunion);
    }

    @Override
    public void setListReunionByDate() {
        Comparator<Reunion> dateComparator = new Comparator<Reunion>() {
            @Override
            public int compare(Reunion o1, Reunion o2) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat compare = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = format.parse(o1.getDate());
                    toCompare = compare.parse(o2.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return date.compareTo(toCompare);
            }
        };
        Collections.sort(this.listOfReunions, dateComparator);

    }

    @Override
    public void setListReunionByPlace() {
        Comparator<Reunion> placeComparator = new Comparator<Reunion>() {
            @Override
            public int compare(Reunion o1, Reunion o2) {
                return o1.getPlace().toLowerCase().compareTo(o2.getPlace().toLowerCase());
            }
        };
        Collections.sort(this.listOfReunions, placeComparator);
    }
}
