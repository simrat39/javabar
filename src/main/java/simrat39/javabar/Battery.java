package simrat39.javabar;

import java.lang.reflect.InvocationTargetException;

public class Battery implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Battery.status = status;
    }
    
    public static String giveStatus() {
        String status = Utils.readFile("/sys/class/power_supply/BAT0/status");
        String capacity = Utils.readFile("/sys/class/power_supply/BAT0/capacity");
        String icon = "";
        String text = capacity + "%";
    
        int intCapacity = Integer.parseInt(capacity);
    
        if (status.equals("Full") || status.equals("Charging") || status.equals("Unknown")){
            icon = "";
        } else {
            if (intCapacity >= 80) {
                icon = "";
            } else if (intCapacity > 60) {
                icon = "";
            } else if (intCapacity > 40) {
                icon = "";
            } else if (intCapacity > 20) {
                icon = "";
            } else {
                icon = "";
            }
        }

        return icon + "  " + text;
    }

    @Override
    public void run() {
        // Initial Values
        String status = giveStatus();
        setStatus(status);

        try {
            Bar.update();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
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
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}