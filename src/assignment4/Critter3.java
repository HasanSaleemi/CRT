package assignment4;

/*
    LEO
    - Small, feline Critter.
    - Only fights Algae and Fleas.
    - Movement is dependent on skin.
 */

public class Critter3 extends Critter {

    @Override
    public String toString() {
        return "3";
    }

    private String skin;

    public Critter3() {
        switch (Critter.getRandomInt(3)){
            case 0:
                skin = "spots";
                break;
            case 1:
                skin = "stripes";
                break;
            case 2:
                skin = "colors";
                break;
        }
    }
    public boolean fight(String vs) {
        return !vs.equals("1") && !vs.equals("2");
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > 150)
            reproduce(new Critter3(), Critter.getRandomInt(8));

        if(getEnergy() > 50){
            switch(skin) {
                case "spots":
                    walk(getRandomInt(2)*4);
                    break;
                case "stripes":
                    walk(getRandomInt(2) == 1 ? 2 : 6);
                    break;
                case "colors":
                    walk(getRandomInt(2) == 1 ? 1 : 5);
                    break;
            }
        }
    }
    public static void runStats(java.util.List<Critter> Teds) {
        int spots = 0;
        int stripes = 0;
        int colors = 0;

        for (Object obj : Teds) {
            Critter3 c = (Critter3) obj;
            switch (c.skin) {
                case "spots":
                    spots++;
                    break;
                case "stripes":
                    stripes++;
                    break;
                case "colors":
                    colors++;
                    break;
            }
        }

        double total=spots+stripes+colors;
        System.out.print("" + Teds.size() + " total Leos    ");
        System.out.print("" + spots*100 / (total) + "% spotted   ");
        System.out.print("" + stripes*100 / (total) + "% striped   ");
        System.out.print("" + colors * 100 / (total) + "% colored   ");
        System.out.println();
    }
}