package simrat39.javabar;

import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class BSPWM implements Runnable {

    static String RawBSPWMstatus;

    public static String getRawBSPWMstatus() {
        return RawBSPWMstatus;
    }

    public static void setRawBSPWMstatus(String RawBSPWMstatus) {
        BSPWM.RawBSPWMstatus = RawBSPWMstatus;
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

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        BSPWM.status = status;
    }

    public static String giveStatus(OutputStream os, InputStream is, String socket_response) throws IOException {
        StringBuilder final_output = new StringBuilder();
        String socket_response_other;

        socket_response_other = socket_response.split("\n")[0];

        int numOfWorkspaces = Integer.parseInt(Bar.readProperties("bspwm.numberOfWorkspaces" , "10"));
        int x = 1; // counter

        for (String i : socket_response_other.split(":")) {
            if (x > numOfWorkspaces) {
                break;
            }
            if (i.charAt(0) == 'O' || i.charAt(0) == 'F'){
                final_output.append("  %{+u}").append(x).append("%{-u}  ");
                x++;
            } else if (i.charAt(0) == 'o'){
                final_output.append("%{A:bspc desktop -f " + x + ":}").append("  ").append(x).append("  ").append("%{A}");
                x++;
            } else if (i.charAt(0) == 'f'){
                x++;
            }
        }
        return String.valueOf(final_output);
    }

    @Override
    public void run(){
        File socketfile = new File("/tmp/bspwm_0_0-socket");
        try {
            AFUNIXSocket sock = createSocket(socketfile);
            InputStream is = sock.getInputStream();
            OutputStream os = sock.getOutputStream();

            writeToSocket("subscribe\0", os);

            // Initial random value
            setStatus("1");
            setRawBSPWMstatus("1");

            while (true) {
                String socket_response = readFromSocket(is);
                if (socket_response != getRawBSPWMstatus()){
                    setStatus(giveStatus(os,is,socket_response));
                    Bar.update();
                }
                setRawBSPWMstatus(socket_response);
                Thread.sleep(150);
            }
        } catch (IOException | InterruptedException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
