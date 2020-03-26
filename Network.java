public class Network implements Runnable {

    static String netStatus;

    public static String getNetStatus(){
        return netStatus;
    }

    public static void setNetStatus(String newval){
        netStatus = newval;
    }
    
    public static String networkStatus(){
        String wifiStatus = Utils.readFile("/sys/class/net/wlp7s0/operstate");
        String ethStatus = Utils.readFile("/sys/class/net/enp8s0/operstate");
        String connName =  Utils.runCommand("nmcli -t -f name connection show --active");
        String icon;

        if (wifiStatus.equals("up")){
            icon = "";
        } else if (ethStatus.equals("up")){
            icon = "";
        } else {
            icon = "";
            connName = "Disconnected";
        }

        String finaloutput = icon + "  " + connName;
        return finaloutput;
    }
    
    public void run() {

        // Initial Values
        String status = networkStatus();
        setNetStatus(status);

        Bar.update();

        // Change Checker
        while (true){
            String newNetwork = networkStatus();
            if (!(newNetwork.equals(getNetStatus()))){
                setNetStatus(newNetwork);
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