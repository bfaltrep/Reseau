package aquarium;

import aquarium.gui.AquariumWindow;
import aquarium.gui.Aquarium;

/**
 * Starting point of the Aquarium application
 */
public class Main  {
	
	public static void main(String[] args) {
		System.out.println("tagazok");
		Aquarium aquarium = new Aquarium();
		AquariumWindow animation = new AquariumWindow(aquarium);
		animation.displayOnscreen();
	}
}
