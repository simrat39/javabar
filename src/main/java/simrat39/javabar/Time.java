package simrat39.javabar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Time.status = status;
    }
    
    public static String giveStatus() {
        SimpleDateFormat i = new SimpleDateFormat("hh:mm aa");
        String time = i.format(new Date()).toString();
        return time;
    }

    @Override
    public void run() {
        // Initial Values
        String status = giveStatus();
        setStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newStatus = giveStatus();
            if (!(newStatus.equals(getStatus()))) {
                setStatus(newStatus);
                Bar.update();
            }

            try {
                Thread.sleep(60000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}