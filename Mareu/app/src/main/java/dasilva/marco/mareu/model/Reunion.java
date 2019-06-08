package dasilva.marco.mareu.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dasilva.marco.mareu.R;

public class Reunion {

    private int avatar;
    private String date;
    private String time;
    private String place;
    private String name;
    private String participants;
    private static int colorAvatar;


    static int[] randomColor = new int[]{R.drawable.rounded, R.drawable.rounded_green, R.drawable.rounded_orange,
    R.drawable.rounded_yellow, R.drawable.rounded_red};

    public Reunion(int avatar, String date, String time, String place, String subject, String participants){
        this.avatar = avatar;
        this.date = date;
        this.time = time;
        this.place = place;
        this.name = subject;
        this.participants = participants;
    }


    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getParticipants() {
        return participants;
    }

    public String getName() {
        return name;
    }

    public int getColorAvatar() {
        return avatar;
    }

    public String getTime(){
        return time;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public static int getRandomColorAvatar() {
        int randomColors = randomColor[new Random().nextInt(randomColor.length)];
        colorAvatar = randomColors;
        return colorAvatar;
    }

    public String getDescription(){
        return name + " - " + date + " - " + time + " - "+ place;
    }
}
