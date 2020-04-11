package simrat39.javabar;

import java.lang.reflect.InvocationTargetException;

public class I3 implements Runnable {

    static String status;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        I3.status = status;
    }
    
    public static String giveStatus() {
        int[] current_workspaces = Utils.getCurrenWorkspaces();
        int active_workspace = Utils.getActiveWorkspace();
        StringBuilder finaloutput = new StringBuilder();

        for (int i: current_workspaces){
            if (i == active_workspace){
                finaloutput.append("   %{+u}").append(i).append("%{-u}  ");
            } else {
                finaloutput.append("   ").append(i).append("  ");
            }
        }

        return String.valueOf(finaloutput);
    }

    @Override
    public void run() {
        // Initial Values
        String status = giveStatus();
        setStatus(status);

        try {
            Bar.update();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Change Checker
        while (true){
            String newStatus = giveStatus();
            if (!(newStatus.equals(getStatus()))) {
                setStatus(newStatus);
                try {
                    Bar.update();
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(100); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}