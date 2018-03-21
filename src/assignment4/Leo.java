package assignment4;
public class Leo extends Critter {

    @Override
    public String toString() { return "%"; }

    private int direction;
    private String item;

    public Leo() {
        direction = Critter.getRandomInt(8);
        int rand = Critter.getRandomInt(3);

        switch (rand){
            case 0:
                item= "ball";
                break;
            case 1:
                item= "shoe";
                break;
            case 2:
                item= "socks";
                break;
        }
    }
    public boolean fight(String vs) {
        if (getEnergy()>40 && item == "socks") {
            return true;
        } else return false;
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > Params.min_reproduce_energy*3) {
            Leo child = new Leo();
            reproduce(child, Critter.getRandomInt(8));
        }
        if (getEnergy() > Params.run_energy_cost*2.4) {
            run(direction);
        } else {
            walk(direction);
        }


        direction = Critter.getRandomInt(8);
    }
    public static void runStats(java.util.List<Critter> Teds) {
        int ball = 0;
        int shoe = 0;
        int socks = 0;
        for (Object obj : Teds) {
            Leo c = (Leo) obj;
            if (c.item=="ball"){
                ball++;}
            if (c.item=="shoe"){
                shoe++;}
            if (c.item=="socks"){
                socks++;}
        }
        int total=ball+shoe+socks;
        System.out.print("" + Teds.size() + " total Teds    ");
        System.out.print("" + ball*100 / (total) + "% ball   ");
        System.out.print("" + shoe*100 / (total) + "% shoe   ");
        System.out.print("" + (socks / (total))*100 + "% socks   ");
        System.out.println();
    }
}