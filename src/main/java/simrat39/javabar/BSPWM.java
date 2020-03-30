package simrat39.javabar;

import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;
import org.freedesktop.dbus.interfaces.Properties;

import java.io.IOException;
import java.util.ArrayList;

public class BSPWM {
    public static void main(String[] args) throws DBusException, IOException {
        DBusConnection conn = DBusConnection.getConnection(DBusConnection.DBusBusType.SYSTEM);
        Properties nibba = (Properties) conn.getRemoteObject("org.freedesktop.NetworkManager","/org/freedesktop/NetworkManager", Properties.class);
        ArrayList conna = nibba.Get("org.freedesktop.NetworkManager","ActiveConnections");
        System.out.println(conna.get(0));

        Properties nibba2 = (Properties) conn.getRemoteObject("org.freedesktop.NetworkManager",conna.get(0).toString(),Properties.class);
        String nibbi = nibba2.Get("org.freedesktop.NetworkManager.Connection.Active","Id");
        System.out.println(nibbi);

        conn.close();

    }
}
