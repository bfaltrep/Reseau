package aquarium.items;

import java.awt.Point;
import java.util.List;

/**
 * A MobileItem is an AquariumItem capable of changing its position (e.g., fish)
 */
public abstract class MobileItem extends AquariumItem {

	/**
	 * Destination point of the current movement
	 */
	private AquariumItem destination = null;

	/**
	 * Constructs a Mobile Item of a given width using the image at imagePath
	 * 
	 * @param width
	 * @param imagePath
	 */
	public MobileItem(int width, String imagePath) {
		super(width, imagePath);
	}

	/**
	 * Constructs a Mobile Item of random width between minWidth and maxWidth
	 * using the image at imagePath
	 * 
	 * @param minWidth
	 * @param maxWidth
	 * @param imagePath
	 */
	public MobileItem(int minWidth, int maxWidth, String imagePath) {
		super(minWidth, maxWidth, imagePath);
	}

	/**
	 * @param neighbours
	 *            List of other Aquarium items in the Aquarium
	 * @return the position of one of the neighbours
	 */
	public abstract AquariumItem getNewTarget(List<AquariumItem> neighbours);
	
	/**
	 * 
	 * @return the current target
	 */
	public abstract AquariumItem getCurrentTarget();

	/**
	 * @return the maximal width allowed for a MobileItem
	 */
	public abstract int getMaximalWidth();

	/**
	 * Makes a move towards the destination which is inversely proportional to
	 * the size of the Mobile Item
	 * 
	 * @param destination
	 *            Position to be reached
	 */
	public void move() {

		double v = getMaximalWidth()/getWidth();
		int dx = (destination.getPosition().x - getPosition().x);
		int dy = (destination.getPosition().y - getPosition().y);
		double direction = (float)Math.atan2(dy, dx);
		double speed = 1.0+v;
		int modx = (int) (speed * Math.cos(direction));
		int mody = (int) (speed * Math.sin(direction));
		Point p = getPosition();
		p.translate(modx, mody);
		setPosition(p);
	}

	/**
	 * @return the current destination Point
	 */
	public AquariumItem getCurrentDestination() {
		return destination;
	}

	/**
	 * Moves to the current destination, if reached, change to another
	 * destination using {@link MobileItem#getNewtarget(List)} method on neighbours
	 * parameter
	 * 
	 * @param neighbours
	 */
	public void move(List<AquariumItem> neighbours) {
		destination=getCurrentTarget();
		if (destination == null || overlap(destination))
			destination = getNewTarget(neighbours);
		move();
	}
}
