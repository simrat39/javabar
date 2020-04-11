package simrat39.javabar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Utils {

    public static String readFile(String filename) {
        String data;
        try {
            File myfile = new File(filename);
            Scanner content = new Scanner(myfile);
            data = content.nextLine();
            content.close();
        } catch (FileNotFoundException e) {
            data = "File not found!";
        }
        return data.replace("\r", "").replace("\n", "");
    }

    public static String runCommand(String command) {
        String line = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = reader.readLine();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return line;
    }

    public static String runCommand(String[] command) {
        String line = "";
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = reader.readLine();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return line;
    }

    public static int[] getCurrenWorkspaces(){
        String[] cmd = {
            "dash",
            "-c",
            "i3-msg -t get_workspaces | tr , \"\n\" | grep \'\"name\":\' | cut -d : -f 2 | cut -d \'\"\' -f 2 | tr \'\\n\' _"
        };
        String ws_str_arr = runCommand(cmd);
        String[] ws_str = ws_str_arr.split("_");
        int ws[] = new int[ws_str.length];
        for (int i = 0; i < ws_str.length; i++) {
            int j = Integer.parseInt(ws_str[i]);
            ws[i] = j;
        }
        return ws;
    }

    public static int getActiveWorkspace(){
        String[] cmd = {
            "dash",
            "-c",
            "i3-msg -t get_workspaces | grep -o -P \'\"name\":\\\"[0-9]*\\\",\"visible\":true\' | sed \'s/[a-z|\"|:|,]//g\'"
        };

        String ws_str = runCommand(cmd);
        int ws = Integer.parseInt(ws_str);
        return ws;
    }

    public static String removeFirstLastElement(String str){
        String final_str = "";
        for (int i = 0; i<str.length(); i++) {
            if (i == 0 || i == str.length()-1) {
                continue;
            }
            final_str += str.charAt(i);
        }
        return final_str;
    }
}