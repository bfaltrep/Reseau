package aquarium.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import aquarium.items.AquariumItem;
import aquarium.items.DorisFish;
import aquarium.items.StableFish;
import aquarium.items.MobileItem;
import aquarium.items.Seastone;
import aquarium.items.Seaweed;

/**
 * An Aquarium is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Aquarium extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant (c.f. final) common to all Aquarium instances (c.f. static)
	 * defining the background color of the Aquarium
	 */
	private static final Color backgroundColor = new Color(218, 238, 255);

	/**
	 * Constant common to all Aquarium instances defining the width of this last
	 */
	private static final int SIZE_AQUA_X = 800;
	/**
	 * Constant common to all Aquarium instances defining the height of this
	 * last
	 */
	private static final int SIZE_AQUA_Y = 600;

	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Stones items" to be displayed in the Aquarium
	 */
	private static final int NB_STONES = 6;
	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Seaweed items" to be displayed in the Aquarium
	 */
	private static final int NB_SEAWEED = 9;
	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Fish items" to be displayed in the Aquarium
	 */
	private static final int NB_FISH = 5;

	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Fish items" to be displayed in the Aquarium
	 */
	private static final int NB_DORISFISH = 5;
	
	/**
	 * Pixel data buffer for the Aquarium rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

	/**
	 * List of Aquarium items to be rendered in the Aquarium
	 */
	private List<AquariumItem> items = new ArrayList<AquariumItem>();

	public Aquarium() {

		for (int i = 0; i < NB_STONES; i++) {
			AquariumItem ai = new Seastone();
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_SEAWEED; i++) {
			AquariumItem ai = new Seaweed();
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_FISH; i++) {
			AquariumItem ai = new StableFish();
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_DORISFISH; i++) {
			AquariumItem ai = new DorisFish();
			if (ai.sink(items))
				items.add(ai);
		}
	}

	/**
	 * @return the width of the Aquarium
	 */
	public static int getSizeX() {
		return SIZE_AQUA_X;
	}

	/**
	 * @return the height of the Aquarium
	 */
	public static int getSizeY() {
		return SIZE_AQUA_Y;
	}

	/**
	 * Proceeds to the movement of any movable AquariumItem and updates the
	 * screen
	 */
	public void animate() {
		for (AquariumItem item : items)
			if (item instanceof MobileItem)
				((MobileItem) item).move(items);
		updateScreen();
	}

	/*
	 * (non-Javadoc) This method is called by the AWT Engine to paint what
	 * appears in the screen. The AWT engine calls the paint method every time
	 * the operative system reports that the canvas has to be painted. When the
	 * window is created for the first time paint is called. The paint method is
	 * also called if we minimize and after we maximize the window and if we
	 * change the size of the window with the mouse.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 * Draw each Aquarium item based on new positions
	 */
	private void updateScreen() {

		if (buffer == null) {
			buffer = createImage(SIZE_AQUA_X, SIZE_AQUA_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_AQUA_X, SIZE_AQUA_Y);
		for (AquariumItem aquariumItem : items) {
			aquariumItem.draw(graphicContext);
		}
		this.repaint();
	}

}
