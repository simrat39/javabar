package simrat39.javabar;

import org.freedesktop.dbus.DBusMap;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.errors.NoReply;
import org.freedesktop.dbus.errors.ServiceUnknown;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.Properties;

public class Spotify implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Spotify.status = status;
    }

    public static String giveStatus(DBusConnection conn, String busname) throws DBusException {
        String final_out;
        try {
            Properties metadata = (Properties) conn.getRemoteObject(busname, "/org/mpris/MediaPlayer2", Properties.class);
            DBusMap spotify_map = metadata.Get("org.mpris.MediaPlayer2.Player", "Metadata");
            String artist = spotify_map.get("xesam:artist").toString().replace("[", "").replace("]", "");
            String title = spotify_map.get("xesam:title").toString();
            String icon = "\uF025";
            final_out = icon + "  " + artist + " : " + title;
        } catch (ServiceUnknown | NoReply e) {
            final_out = "";
        }
        return final_out;
    }

    @Override
    public void run() {
        try {
            DBusConnection conn = DBusConnection.getConnection(DBusConnection.DBusBusType.SESSION);
            String busname = "org.mpris.MediaPlayer2.spotify";

            // Initial values
            String status = giveStatus(conn,busname);
            setStatus(status);

            while (true) {
                String newStatus = giveStatus(conn,busname);
                if (!(newStatus.equals(getStatus()))) {
                    setStatus(newStatus);
                    Bar.update();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // todo catch exception
                }
            }
        } catch (DBusException e) {
            // todo
        }
    }
}
