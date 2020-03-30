package simrat39.javabar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Datee implements Runnable {

    static String dateeStatus;

    public static String getdateeStatus() {
        return dateeStatus;
    }

    public static void setdateeStatus(String newval) {
        dateeStatus = newval;
    }
    
    public static String DateeStatus() {
        SimpleDateFormat i = new SimpleDateFormat("d MMM y");
        String datee = i.format(new Date()).toString();
        String finaloutput = datee;
        return finaloutput;
    }
    
    public void run() {

        // Initial Values
        String status = DateeStatus();
        setdateeStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newDatee = DateeStatus();
            if (!(newDatee.equals(getdateeStatus()))) {
                setdateeStatus(newDatee);
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