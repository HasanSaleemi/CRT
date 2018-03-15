package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Hasan Saleemi
 * has2375
 * Fall 2018
 */


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */

public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	private static ArrayList<ArrayList<ArrayList<Critter>>> grid;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];

		grid = new ArrayList<ArrayList<ArrayList<Critter>>>();
		for(int x = 0; x < Params.world_width; x++){
			grid.add(x, new ArrayList<>());
			for(int y = 0; y < Params.world_height; y++){
				grid.get(x).add(y, new ArrayList<>());
			}
		}
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}

	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;

	private void moveCrit(int direction){
		switch (direction) {
			case 0:
				x_coord++;
				break;
			case 1:
				x_coord++;
				y_coord--;
				break;
			case 2:
				y_coord--;
				break;
			case 3:
				x_coord--;
				y_coord--;
				break;
			case 4:
				x_coord--;
				break;
			case 5:
				x_coord--;
				y_coord++;
				break;
			case 6:
				y_coord++;
				break;
			case 7:
				x_coord++;
				y_coord++;
				break;
		}
		x_coord =  x_coord - Math.floorDiv(x_coord, Params.world_width)*Params.world_width;
		y_coord =  y_coord - Math.floorDiv(y_coord, Params.world_height)*Params.world_height;
	}
	
	protected final void walk(int direction) {
		grid.get(x_coord).get(y_coord).remove(this);

		energy-=Params.walk_energy_cost;
		moveCrit(direction);

		grid.get(x_coord).get(y_coord).add(this);
	}
	
	protected final void run(int direction) {
		grid.get(x_coord).get(y_coord).remove(this);

		energy-=Params.run_energy_cost;
		moveCrit(direction);
		moveCrit(direction);

		grid.get(x_coord).get(y_coord).add(this);
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if(energy < Params.min_reproduce_energy)
			return;

		offspring.energy = energy/2;
		offspring.x_coord = x_coord;
		offspring.y_coord = y_coord;
		offspring.moveCrit(direction);

		energy/=2;

		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String opponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Critter newCritter = (Critter)Class.forName("assignment4." + critter_class_name).getDeclaredConstructor().newInstance();
			newCritter.x_coord = Critter.getRandomInt(Params.world_width);
			newCritter.y_coord = Critter.getRandomInt(Params.world_height);
			newCritter.energy = Params.start_energy;

			population.add(newCritter);
			grid.get(newCritter.x_coord).get(newCritter.y_coord).add(newCritter);
		}catch (NoSuchMethodException | InvocationTargetException |ClassNotFoundException | InstantiationException | IllegalAccessException e){
			throw new InvalidCritterException(critter_class_name);
		}
	}

	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();

		try{
			Class.forName("assignment4." + critter_class_name);
		}catch (ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}

		for(Critter c: population){
			if(c.getClass().getName().equals("assignment4." + critter_class_name))
				result.add(c);
		}

		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			grid.get(super.x_coord).get(super.y_coord).remove(this);
			super.x_coord = new_x_coord;
			grid.get(super.x_coord).get(super.y_coord).add(this);
		}
		
		protected void setY_coord(int new_y_coord) {
			grid.get(super.x_coord).get(super.y_coord).remove(this);
			super.y_coord = new_y_coord;
			grid.get(super.x_coord).get(super.y_coord).add(this);
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		for(int x = 0; x < Params.world_width; x++){
			for(int y = 0; y < Params.world_height; y++){
				grid.get(x).set(y, new ArrayList<>());
			}
		}
		population.clear();
		babies.clear();
	}

	private static void removeDeadCritters(){
		List<Critter> newPop = new ArrayList<>();
		for(Critter c: population) {
			if(c.energy	> 0){
				newPop.add(c);
			} else {
				grid.get(c.x_coord).get(c.y_coord).remove(c);
			}
		}
		population = newPop;
	}
	public static void worldTimeStep() {
		// add babies
		for(Critter c: babies){
			population.add(c);
			grid.get(c.x_coord).get(c.y_coord).add(c);
		}
		babies.clear();

		// do time step
		ArrayList<ArrayList<Integer>> coords = new ArrayList<>();

		for(Critter c: population) {
			c.energy-=Params.rest_energy_cost;
			c.doTimeStep();

			ArrayList<Integer> saveCoords = new ArrayList<>();
			saveCoords.add(c.x_coord);
			saveCoords.add(c.y_coord);
			coords.add(saveCoords);
		}

		// handle encounters
		for(ArrayList<Integer> gridSpot: coords){
			ArrayList<Critter> spot = grid.get(gridSpot.get(0)).get(gridSpot.get(1));
			while(spot.size() > 1){
				Critter A = spot.get(0);
				Critter B = spot.get(1);

				spot.remove(A);
				spot.remove(B);

				boolean AFight = A.fight(B.toString());
				boolean BFight = B.fight(A.toString());

				if(A.x_coord == B.x_coord && A.y_coord == B.y_coord && (A.energy > 0 && B.energy > 0)){
					int ARand = AFight ? Critter.getRandomInt(A.energy) : 0;
					int BRand = BFight ? Critter.getRandomInt(B.energy) : 0;

					if(ARand > BRand){
						A.energy+=B.energy/2;
						B.energy = 0;
					} else {
						B.energy+=A.energy/2;
						A.energy = 0;
					}
				}

				grid.get(B.x_coord).get(B.y_coord).add(0, B);
				grid.get(A.x_coord).get(A.y_coord).add(0, A);
				removeDeadCritters();
			}
		}

		// remove ded
		removeDeadCritters();

		// create new algae
		for(int i = 0; i < Params.refresh_algae_count; i++) {
			try {
				makeCritter("Algae");
			} catch (InvalidCritterException ignored) { }
		}
	}

	private static void printTopBottom(){
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++)
			System.out.print("-");
		System.out.print("+");
	}
	public static void displayWorld() {
		for(int y = 0; y < Params.world_height; y++){
			if(y == 0){
				printTopBottom();
				System.out.println();
			}

			for(int x = 0; x < Params.world_width; x++){
				if(x == 0)
					System.out.print("|");

				if(grid.get(x).get(y).size() == 0){
					System.out.print(" ");
				} else if(grid.get(x).get(y).size() > 0){
					System.out.print(grid.get(x).get(y).get(0));
				} /*else {
					System.out.print("*");
				}*/

				if(x == Params.world_width - 1)
					System.out.print("|");
			}

			if(y == Params.world_height - 1){
				System.out.println();
				printTopBottom();
			}
			System.out.println();
		}
	}
}
