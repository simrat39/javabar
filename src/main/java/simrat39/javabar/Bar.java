package simrat39.javabar;

public class Bar {

    public static void update(){
        String separator = "   |   ";
        String right = "%{r}" + Network.getStatus() + separator + Battery.getStatus() + "   ";
        String left = "%{l}" + "   " + Datee.getStatus() + separator + Time.getStatus();
        String center = "%{c}" + BSPWM.getStatus();
        System.out.println(left+center+right);
    }


    public static void main(String[] args) {

        Thread net = new Thread(new Network(false));
        net.start();

        Thread bat = new Thread(new Battery());
        bat.start();

        Thread tim = new Thread(new Time());
        tim.start();

        Thread dat = new Thread(new Datee());
        dat.start();

        Thread BSPWM = new Thread(new BSPWM());
        BSPWM.start();

        // Thread vol = new Thread(new Volume());
        // vol.start();

        // Thread spotify = new Thread(new Spotify());
        // spotify.start();

        // Thread i3 = new Thread(new Ithree());
        // i3.start();
    }
}