package simrat39.javabar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time implements Runnable {

    static String timStatus;

    public static String gettimStatus() {
        return timStatus;
    }

    public static void settimStatus(String newval) {
        timStatus = newval;
    }
    
    public static String TimeStatus() {
        SimpleDateFormat i = new SimpleDateFormat("hh:mm aa");
        String datee = i.format(new Date()).toString();
        String finaloutput = datee;
        return finaloutput;
    }
    
    public void run() {

        // Initial Values
        String status = TimeStatus();
        settimStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newTime = TimeStatus();
            if (!(newTime.equals(gettimStatus()))) {
                settimStatus(newTime);
                Bar.update();
            }
            try {
                Thread.sleep(60000); 
            } catch (InterruptedException e) {
                // todo catch exception
            }
        }
    }
}