package assignment4;

/*
    WINSTON
    - Moody critter.
    - Fights more and runs when moody, fights less and walks when happy.
 */

public class Critter2 extends Critter {

    @Override
    public String toString() { return "2"; }

    private int direction;
    private String mood;

    public Critter2() {
        direction = Critter.getRandomInt(8);

        switch (Critter.getRandomInt(3)){
            case 0:
                mood = "happy";
                break;
            case 1:
                mood = "sad";
                break;
            case 2:
                mood = "angry";
                break;
        }
    }
    public boolean fight(String vs) {
        switch (mood) {
            case "angry":
                return true;
            case "sad":
                return getRandomInt(2) == 0;
            case "happy":
                return getRandomInt(4) == 0;
        }
        return false;
    }
    @Override
    public void doTimeStep() {
        if (getEnergy() > 160)
            reproduce(new Critter2(), Critter.getRandomInt(8));

        switch (mood) {
            case "angry":
                run(direction);
                break;
            case "sad":
                walk(direction);
                break;
            case "happy":
                walk(direction);
                break;
        }
        direction = Critter.getRandomInt(8);
    }
    public static void runStats(java.util.List<Critter> Winstons) {
        int happy = 0;
        int sad = 0;
        int angry = 0;

        for (Object obj : Winstons) {
            Critter2 c = (Critter2) obj;
            switch (c.mood) {
                case "happy":
                    happy++;
                    break;
                case "sad":
                    sad++;
                    break;
                case "angry":
                    angry++;
                    break;
            }
        }

        double total=happy+sad+angry;

        System.out.print("" + Winstons.size() + " total Winstons    ");
        System.out.print("" + happy*100 / (total) + "% happy   ");
        System.out.print("" + sad*100 / (total) + "% sad   ");
        System.out.print("" + angry*100 / (total) + "% angry   ");
        System.out.println();
    }
}