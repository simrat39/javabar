import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class Utils {

    public static String readFile(String filename){
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

    public static String runCommand(String command){
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

    public static String runCommand(String[] command){
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
}