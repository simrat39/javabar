package simrat39.javabar;

public class Ithree implements Runnable {

    static String i3Status;

    public static String geti3Status() {
        return i3Status;
    }

    public static void seti3Status(String newval) {
        i3Status = newval;
    }
    
    public static String ithreeStatus() {
        int[] current_workspaces = Utils.getCurrenWorkspaces();
        int active_workspace = Utils.getActiveWorkspace();
        String finaloutput = "";

        for (int i: current_workspaces){
            if (i == active_workspace){
                finaloutput += "   %{+u}" + i + "%{-u}  ";  
            } else {
                finaloutput += "   " + i + "  ";
            }
        }

        return finaloutput;
    }
    
    public void run() {
        // Initial Values
        String status = ithreeStatus();
        seti3Status(status);

        Bar.update();

        // Change Checker
        while (true){
            String newIthree = ithreeStatus();
            if (!(newIthree.equals(geti3Status()))) {
                seti3Status(newIthree);
                Bar.update();
            }
            
            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
}