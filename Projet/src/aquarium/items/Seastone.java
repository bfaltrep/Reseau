package aquarium.items;


/**
 * Stone element
 */
public class Seastone extends AquariumItem {

	private static final int MIN_WIDTH = 30, MAX_WIDTH = 60;
	private static final String img = "image/seastone.png";

	public Seastone(String address) {
		super(MIN_WIDTH, MAX_WIDTH, img, address);
	}
	
	public Seastone(int width, String address) {
		super(width, img, address);
	}

	
}
