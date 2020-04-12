package simrat39.javabar;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class Bar {

    static String[] left_modules;
    static String[] right_modules;
    static String[] center_modules;

    static String separator;
    static String left_padding = "";
    static String right_padding = "";

    public static void update() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String left = "%{l}";
        String right = "%{r}";
        String center = "%{c}";

        left += left_padding;

        left += setOutputString(left_modules);

        right += setOutputString(right_modules);
        right += right_padding;

        center += setOutputString(center_modules);

        System.out.println(left + center + right);
    }

    public static String setOutputString(String[] position) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        if (position[0] != "") {
            String final_out = "";
            for (String i : position) {
                Class c = Class.forName("simrat39.javabar." + i);
                Method method = c.getMethod("getStatus");
                String output = (String)method.invoke(c);
                if (output != null) {
                    final_out += position[0] == i || (output == null || output.length() == 0) ? output : separator + output;
                }
            }
            return final_out;
        }
        return "";
    }

    public static String readProperties(String property, String defaultVal) throws IOException {
        Properties prop = new Properties();
        InputStream is;
        is = new FileInputStream(System.getProperty("user.home")+"/.config/javabar/conf");
        prop.load(is);
        return prop.getProperty(property,defaultVal);
    }

    public static void main(String[] args) throws IOException {
        left_modules = readProperties("modules-left","").split(" ");
        right_modules = readProperties("modules-right","").split(" ");
        center_modules = readProperties("modules-center","").split(" ");

        separator = Utils.removeFirstLastElement(readProperties("seperator","\"   |   \""));

        for (int i=0;i<Integer.parseInt(readProperties("padding-left","0"));i++) {
            left_padding += " ";
        }

        for (int i=0;i<Integer.parseInt(readProperties("padding-right","0"));i++) {
            right_padding += " ";
        }

        String all_modules = "";

        all_modules += String.join(" ",left_modules) + " " + String.join(" ",right_modules) + " " + String.join(" ",center_modules);

        if (all_modules.contains("Network")) {
            Thread net = new Thread(new Network());
            net.start();
        } if (all_modules.contains("Battery")) {
            Thread bat = new Thread(new Battery());
            bat.start();
        } if (all_modules.contains("Time")) {
            Thread tim = new Thread(new Time());
            tim.start();
        } if (all_modules.contains("Datee")) {
            Thread dat = new Thread(new Datee());
            dat.start();
        } if (all_modules.contains("BSPWM")) {
            Thread BSPWM = new Thread(new BSPWM());
            BSPWM.start();
        } if (all_modules.contains("Volume")) {
            Thread vol = new Thread(new Volume());
            vol.start();
        } if (all_modules.contains("Spotify")) {
            Thread spotify = new Thread(new Spotify());
            spotify.start();
        } if (all_modules.contains("I3")) {
            Thread i3 = new Thread(new I3());
            i3.start();
        }
    }
}