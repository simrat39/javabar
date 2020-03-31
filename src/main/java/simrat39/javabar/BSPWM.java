package simrat39.javabar;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.*;
import java.util.Arrays;

public class BSPWM implements Runnable {

    static String bspwmStatus;
    static String RawBSPWMstatus;

    public static String getBSPWMstatus() {
        return bspwmStatus;
    }

    public static void setBSPWMstatus(String newval) {
        bspwmStatus = newval;
    }

    public static String getRawBSPWMstatus() {
        return RawBSPWMstatus;
    }

    public static void setRawBSPWMstatus(String newval) {
        RawBSPWMstatus = newval;
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

    public static String BSPWMstatus(OutputStream os, InputStream is, String socket_response) throws IOException {

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

            writeToSocket("subscribe\0", os);

            // Initial random value
            setBSPWMstatus("1");
            setRawBSPWMstatus("1");

            while (true) {
                String socket_response = readFromSocket(is);
                if (socket_response != getRawBSPWMstatus()){
                    setBSPWMstatus(BSPWMstatus(os,is,socket_response));
                    Bar.update();
                }
                setRawBSPWMstatus(socket_response);
                Thread.sleep(150);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
