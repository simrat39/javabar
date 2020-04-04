package simrat39.javabar;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.Properties;

import java.io.IOException;
import java.util.ArrayList;

public class Network implements Runnable {

    public Network(boolean showNetworkName){ Network.showNetworkName = showNetworkName; }

    static boolean showNetworkName;
    static String netStatus;

    public static String getNetStatus() {
        return netStatus;
    }

    public static void setNetStatus(String netStatus) {
        Network.netStatus = netStatus;
    }
    
    public static String networkStatus(DBusConnection conn, String busname) throws DBusException {
        String wifiStatus = Utils.readFile("/sys/class/net/wlp7s0/operstate");
        String ethStatus = Utils.readFile("/sys/class/net/enp8s0/operstate");
        String connName = "";

        if ((wifiStatus.equals("up") || ethStatus.equals("up")) && showNetworkName) {
            Properties nm_prop_main = (Properties) conn.getRemoteObject(busname, "/org/freedesktop/NetworkManager", Properties.class);
            ArrayList active_connections_arr = nm_prop_main.Get(busname, "ActiveConnections");
            Properties nm_prop_active_device = (Properties) conn.getRemoteObject(busname, active_connections_arr.get(0).toString(), Properties.class);
            connName = nm_prop_active_device.Get("org.freedesktop.NetworkManager.Connection.Active", "Id");
        }

        String icon;

        if (wifiStatus.equals("up")){
            icon = "";
        } else if (ethStatus.equals("up")){
            icon = "";
        } else {
            icon = "";
            connName = "Disconnected";
        }

        String finaloutput;

        if (!showNetworkName) {
            finaloutput = icon;
        } else {
            finaloutput = icon + "  " + connName;
        }
        return finaloutput;
    }

    @Override
    public void run() {
        try {
            DBusConnection conn = DBusConnection.getConnection(DBusConnection.DBusBusType.SYSTEM);
            String busname = "org.freedesktop.NetworkManager";
            if (!showNetworkName) {
                conn.close();
            }

            // Initial Values
            String status = networkStatus(conn,busname);
            setNetStatus(status);

            Bar.update();

            // Change Checker
            while (true){
                String newNetwork = networkStatus(conn,busname);
                if (!(newNetwork.equals(getNetStatus()))) {
                    setNetStatus(newNetwork);
                    Bar.update();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // todo catch exception
                }
            }
            //conn.close();
        } catch (DBusException | IOException e){
            // todo
        }
    }
}