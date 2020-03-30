package simrat39.javabar;

public class Battery implements Runnable {

    static String batStatus;

    public static String getbatStatus() {
        return batStatus;
    }

    public static void setbatStatus(String newval) {
        batStatus = newval;
    }
    
    public static String batteryStatus() {
        String status = Utils.readFile("/sys/class/power_supply/BAT0/status");
        String capacity = Utils.readFile("/sys/class/power_supply/BAT0/capacity");
        String icon = "";
        String text = capacity + "%";
    
        int intCapacity = Integer.parseInt(capacity);
    
        if (status.equals("Full") || status.equals("Charging") || status.equals("Unknown")){
            icon = "";
        } else {
            if (intCapacity > 80) {
                icon = "";
            } else if (intCapacity > 60 & intCapacity < 80) {
                icon = "";
            } else if (intCapacity > 40 & intCapacity < 60) {
                icon = "";
            } else if (intCapacity > 20 & intCapacity < 40) {
                icon = "";
            } else if (intCapacity < 20) {
                icon = "";
            }
        }   

        String finaloutput = icon + "  " + text;
        return finaloutput;
    }
    
    public void run() {
        // Initial Values
        String status = batteryStatus();
        setbatStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newBattery = batteryStatus();
            if (!(newBattery.equals(getbatStatus()))) {
                setbatStatus(newBattery);
                Bar.update();
            }
            
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
}