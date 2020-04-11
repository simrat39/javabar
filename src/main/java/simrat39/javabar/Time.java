package simrat39.javabar;

import java.lang.reflect.InvocationTargetException;
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

        try {
            Bar.update();
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        // Change Checker
        while (true){
            String newStatus = giveStatus();
            if (!(newStatus.equals(getStatus()))) {
                setStatus(newStatus);
                try {
                    Bar.update();
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(60000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}