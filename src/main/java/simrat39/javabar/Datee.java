package simrat39.javabar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Datee implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Datee.status = status;
    }
    
    public static String giveStatus() {
        SimpleDateFormat i = new SimpleDateFormat("d MMM y");
        String datee = i.format(new Date());
        return datee;
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