package simrat39.javabar;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.*;
import java.util.Arrays;

public class BSPWM implements Runnable {

    static String bspwmStatus;

    public static String getBSPWMstatus() {
        return bspwmStatus;
    }

    public static void setBSPWMstatus(String newval) {
        bspwmStatus = newval;
    }

    public static void writeToSocket(String message, OutputStream os) throws IOException {
        os.write(message.getBytes("UTF-8"));
        os.flush();
    }

    public static String readFromSocket(InputStream is) throws IOException {
        byte[] buf = new byte[100];
        int read = is.read(buf);
        return new String(buf,0,read,"UTF-8");
    }

    public static AFUNIXSocket createSocket(File socketfile) throws IOException {
        AFUNIXSocket sock = AFUNIXSocket.newInstance();
        sock.connect(new AFUNIXSocketAddress(socketfile));
        return sock;
    }

    public static String BSPWMstatus(OutputStream os, InputStream is) throws IOException {
        writeToSocket("wm\0--get-status\0",os);
        String socket_response = readFromSocket(is);

        String final_output = "";

        int x = 1; // counter
        for (char i : socket_response.toCharArray()){
            if (i == 'O' || i == 'F'){
                final_output +=  "  %{+u}" + x + "%{-u}  ";
                x++;
            } else if (i == 'o'){
                final_output += "  "+x+"  ";
                x++;
            } else if (i == 'f'){
                x++;
            }
        }

        return final_output;
    }

    public void run(){
        File socketfile = new File("/tmp/bspwm_0_0-socket");
        try {

            AFUNIXSocket sock = createSocket(socketfile);
            InputStream is = sock.getInputStream();
            OutputStream os = sock.getOutputStream();

            // Initial Values
            String status = BSPWMstatus(os,is);
            setBSPWMstatus(status);

            Bar.update();

            sock.close();
            os.close();
            is.close();

            // Change Checker
            while (true) {
                AFUNIXSocket sock1 = createSocket(socketfile);

                InputStream is1 = sock1.getInputStream();
                OutputStream os1 = sock1.getOutputStream();
                String newBSPWMstatus = BSPWMstatus(os1,is1);
                if (!(newBSPWMstatus.equals(getBSPWMstatus()))) {
                    setBSPWMstatus(newBSPWMstatus);
                    Bar.update();
                }
                sock1.close();
                is1.close();
                os1.close();
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    // todo catch exception
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
