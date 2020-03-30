package simrat39.javabar;

public class Bar {

    public static void update(){
        String separator = "   /   ";
        String right = "%{r}" + Time.gettimStatus() + separator + Network.getNetStatus() + separator + Volume.getvolStatus() + separator + Battery.getbatStatus() + "   ";
        String left = "%{l}" + "   " + Datee.getdateeStatus();
        String center = "%{c}" + BSPWM.getBSPWMstatus();
        System.out.println(left+center+right);
    }

    public static void main(String[] args) throws InterruptedException{
        Thread net = new Thread(new Network());
        net.start();

        Thread bat = new Thread(new Battery());
        bat.start();

        Thread vol = new Thread(new Volume());
        vol.start();

        Thread tim = new Thread(new Time());
        tim.start();

        Thread dat = new Thread(new Datee());
        dat.start();

        Thread BSPWM = new Thread(new BSPWM());
        BSPWM.start();

       // Thread i3 = new Thread(new Ithree());
       // i3.start();
    }
}