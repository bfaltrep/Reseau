package aquarium.items;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.ImageIcon;

import aquarium.gui.Aquarium;
import aquarium.util.RandomNumber;

/**
 * An AquariumItem is a visual component to be displayed in the Aquarium (e.g.,
 * stone, seaweed, fish). The class is abstract meaning that an AquariumItem has
 * to be extended in order to be instantiated.
 */
public abstract class AquariumItem {
	
	/**
	 * Image representation of the AquariumItem
	 */
	private final Image image;
	
	/**
	 * Current position of the Aquarium Item in the Aquarium
	 */
	private Point position = new Point(0, 0);

	/**
	 * displayed width of the Aquarium Item
	 */
	private int width;
	/**
	 * displayed height of the Aquarium Item
	 */
	private int height;

	/**
	 * Constructs an Aquarium Item of random width between minWidth and maxWidth
	 * using the image at imagePath
	 * 
	 * @param minWidth
	 * @param maxWidth
	 * @param imagePath
	 */
	public AquariumItem(int minWidth, int maxWidth, String imagePath) {
		this(RandomNumber.randomValue(minWidth, maxWidth), imagePath);
	}

	/**
	 * Constructs an Aquarium Item of a given width using the image at imagePath
	 * 
	 * @param width
	 * @param imagePath
	 */
	public AquariumItem(int width, String imagePath) {
		this.image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource(imagePath));
		ImageIcon icon = new ImageIcon(image);
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
		double ratio = width / (this.width * 1.0);
		this.width = (int) (this.width * ratio);
		this.height = (int) (this.height * ratio);
	}

	/**
	 * Draw the Aquarium Item into the graphic component g at its current
	 * position
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {

		g.drawImage(image, position.x, position.y, width, height, null);
	}

	/**
	 * Update the Aquarium Item position to p
	 * 
	 * @param p
	 */
	public void setPosition(Point p) {
		position = p;
	}

	/**
	 * @return the current Position of the Aquarium Item
	 */
	public Point getPosition() {
		return new Point(position);
	}

	/**
	 * @return a Rectangle representing the outline of the Aquarium Item
	 */
	public Rectangle outline() {
		return new Rectangle(position.x, position.y, width, height);
	}

	/**
	 * @param items
	 *            a List of Aquarium items
	 * @return true if at least one Aquarium items outline is intersecting the
	 *         outline of this
	 */
	public boolean intersects(List<AquariumItem> items) {
		for (AquariumItem ai : items) {
			if (ai.outline().intersects(outline()))
				return true;
		}
		return false;
	}

	public boolean overlap(AquariumItem item) {
		Rectangle intersection = outline().intersection(item.outline());
		return ((intersection.width > outline().width / 2) || (intersection.width > item
				.outline().width / 2))
				&& ((intersection.height > outline().height / 2) || (intersection.height > item
						.outline().height / 2));
	}

	/**
	 * Will position randomly the AquariumItem (this) without overlapping an
	 * already positioned Aquarium item
	 * 
	 * @param items
	 *            Already positioned Aquarium items
	 * @return true if the AquariumItem was able to be positioned without
	 *         conflicts after 20 tries
	 */
	public boolean sink(List<AquariumItem> items) {
		int nbTries = 0;
		while (this.intersects(items)) {
			this.setPosition(RandomNumber.randomPoint(0, Aquarium.getSizeX() - this.width, 0, Aquarium.getSizeY() - this.height));
			nbTries++;
			if (nbTries > 20)
				return false;
		}
		return true;
	}

	public int getWidth() {
		return width;
	}

}
