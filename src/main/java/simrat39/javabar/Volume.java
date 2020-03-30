package simrat39.javabar;

public class Volume implements Runnable {

    static String volStatus;

    public static String getvolStatus() {
        return volStatus;
    }

    public static void setvolStatus(String newval) {
        volStatus = newval;
    }
    
    public static String VolumeStatus() {
        String status = Utils.runCommand("amixer get Master");

        String icon = "";

        String[] volumeCMD = {
            "dash",
            "-c",
            "amixer get Master | egrep -o -m1 '[0-9]*%'"
        };

        String volume = Utils.runCommand(volumeCMD);

        if (status.contains("on")) {
            icon = "";
        } else if (status.contains("off")) {
            icon = "";
        }

        String finaloutput = icon + "  " + volume;
        return finaloutput;
    }
    
    public void run() {

        // Initial Values
        String status = VolumeStatus();
        setvolStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newVolume = VolumeStatus();
            if (!(newVolume.equals(getvolStatus()))) {
                setvolStatus(newVolume);
                Bar.update();
            }
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                // todo catch exception
            }
        }
    }
}