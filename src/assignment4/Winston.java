package assignment4;
public class Winston extends Critter {

    @Override
    public String toString() { return "&"; }

    private int direction;
    private String mood;

    public Winston() {
        direction = Critter.getRandomInt(8);
        int rand = Critter.getRandomInt(3);
        switch (rand){
            case 0:
                mood= "happy";
                break;
            case 1:
                mood= "sad";
                break;
            case 2:
                mood= "angry";
                break;
        }
    }
    public boolean fight(String vs) {
        if (getEnergy()>20 && mood == "angry") {
            return true;
        }
        else if (getEnergy()>50 && mood == "sad") {
            return true;
        }else if (getEnergy()>80 && mood == "happy") {
            return true;
        }else return false;
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > Params.min_reproduce_energy*1.7) {
            Winston child = new Winston();
            reproduce(child, Critter.getRandomInt(8));
        }
        if (getEnergy() > Params.run_energy_cost*1.2) {
            run(direction);
        } else {
            walk(direction);
        }


        direction = Critter.getRandomInt(8);
    }
    public static void runStats(java.util.List<Critter> Winstons) {
        int happy = 0;
        int sad = 0;
        int angry = 0;
        for (Object obj : Winstons) {
            Winston c = (Winston) obj;
            if (c.mood=="happy"){
                happy++;}
            if (c.mood=="sad"){
                sad++;}
            if (c.mood=="angry"){
                angry++;}
        }
        int total=happy+sad+angry;
        System.out.print("" + Winstons.size() + " total Winstons    ");
        System.out.print("" + happy*100 / (total) + "% happy   ");
        System.out.print("" + sad*100 / (total) + "% sad   ");
        System.out.print("" + (angry / (total))*100 + "% angry   ");
        System.out.println();
    }
}