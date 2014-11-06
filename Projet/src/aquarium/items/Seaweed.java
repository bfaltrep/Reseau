package aquarium.items;


/**
 * Seaweed element
 */
public class Seaweed extends AquariumItem {

	private static final int MIN_WIDTH = 30, MAX_WIDTH = 100;
	private static String img = "image/seaweed.png";
	
	public Seaweed() {
		super(MIN_WIDTH, MAX_WIDTH, img);
	}

	public Seaweed(int width) {
		super(width, img);
	}
	
}
