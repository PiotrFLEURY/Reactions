package fr.piotr.reactions.events.time;

import java.io.Serializable;

/**
 * Created by piotr_000 on 01/01/2017.
 *
 */

public class HourMinute implements Serializable {

    private int hour;
    private int minute;

    public HourMinute(int hour, int minute){
        this.hour=hour;
        this.minute=minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return pad(hour) + ":" + pad(minute);
    }

    private String pad(int i){
        if(i<10){
            return "0" + i;
        }
        return "" + i;
    }

    public static HourMinute valueOf(String str){
        String[] hourMinute = str.split(":");
        return new HourMinute(Integer.valueOf(hourMinute[0]), Integer.valueOf(hourMinute[1]));
    }
}
