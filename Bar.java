public class Bar {

    public static void update(){
        System.out.println(Network.getNetStatus() + Battery.getbatStatus() + Volume.getvolStatus());
    }

    public static void main(String[] args) throws InterruptedException{
        Thread net = new Thread(new Network());
        net.start();

        Thread bat = new Thread(new Battery());
        bat.start();

        Thread vol = new Thread(new Volume());
        vol.start();
    }

}