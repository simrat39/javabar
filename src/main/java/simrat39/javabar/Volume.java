package simrat39.javabar;

import java.lang.reflect.InvocationTargetException;

public class Volume implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String volStatus) {
        Volume.status = status;
    }

    public static String giveStatus() {
        String icon = "";

        String[] volumeCMD = {
            "dash",
            "-c",
            "amixer get Master | egrep -o -m1 '[0-9]*%'"
        };

        String volume = Utils.runCommand(volumeCMD);

        return icon + "  " + volume;
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
                } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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