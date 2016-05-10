package app.androidprog.com.tutomusicplayer.Utility;

import android.util.Log;

public class Utility {

    public static String convertDuration(long duration){

        long minutes = (duration / 1000 ) / 60;
        long seconds = (duration / 1000 ) % 60;

        String converted = String.format("%d:%02d", minutes, seconds);
        return converted;


    }

}
