package dasilva.marco.mareu.service;


import android.util.Log;

import dasilva.marco.mareu.model.Reunion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FakeReunionApiService implements ReunionApiService {

    private List<Reunion> listOfReunions = new ArrayList<>();
    private static String[] salles = new String[]{"Salle 1", "Salle 2", "Salle 3", "Salle 4", "Salle 5", "Salle 6", "Salle 7",
            "Salle 8", "Salle 9", "Salle 10" };

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
    public List<Reunion> setListReunionByDate(String choiceDate) {
        ArrayList<Reunion> reunionByDate = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = null;
        try {
            currentDate = formatter.parse(choiceDate);
        } catch (ParseException e) {
            Log.d("Error null", "CurrentDate");
        }
        for (Reunion reunion : listOfReunions){
            Date date = null;
            try{
                date = formatter.parse(reunion.getDate());
            } catch (ParseException e){

            }

            if (currentDate.toString().contains(date.toString())){
                reunionByDate.add(reunion);
            }
        }
       return reunionByDate;
    }

    @Override
    public List<Reunion> setListReunionByPlace(String place) {
        ArrayList<Reunion> reunionByPlace = new ArrayList<>();
        for (Reunion reunion : listOfReunions){
            if (reunion.getPlace().contains(place)){
                reunionByPlace.add(reunion);
            }
        }
        return reunionByPlace;
    }

    @Override
    public String[] getSalles() {
        return salles;
    }
}
