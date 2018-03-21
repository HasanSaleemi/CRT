package assignment4;

public class Rex extends Critter {

        @Override
        public String toString() { return "X"; }

        private int direction;
        private String type;

        public Rex() {
            direction = Critter.getRandomInt(8);
            int rand = Critter.getRandomInt(3);
            switch (rand){
                case 0:
                    type= "stripes";
                    break;
                case 1:
                    type= "spots";
                    break;
                case 2:
                    type= "colored";
                    break;
            }
        }
        public boolean fight(String vs) {
            if (getEnergy()>75) {
                return true;
            }
            else return false;
        }
        @Override
        public void doTimeStep() {
            if (getEnergy() > Params.run_energy_cost*1.5) {
                run(direction);
            } else {
                walk(direction);
            }
            if (getEnergy() > Params.min_reproduce_energy*1.5) {
                Rex child = new Rex();
                reproduce(child, Critter.getRandomInt(8));
            }

            direction = Critter.getRandomInt(8);
        }
        public static void runStats(java.util.List<Critter> rexes) {
            int stripes = 0;
            int spots = 0;
            int colored = 0;
            for (Object obj : rexes) {
                Rex c = (Rex) obj;
                if (c.type=="stripes"){
                    stripes++;}
                if (c.type=="spots"){
                    spots++;}
                if (c.type=="colored"){
                    colored++;}
            }
            int total=stripes+spots+colored;
            System.out.print("" + rexes.size() + " total Rexes    ");
            System.out.print("" + stripes*100 / (total) + "% stripes   ");
            System.out.print("" + spots*100 / (total) + "% spots   ");
            System.out.print("" + (colored / (total))*100 + "% colored   ");
            System.out.println();
        }
    }