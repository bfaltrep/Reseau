package aquarium.gui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

/**
 * An AquariumWindow is a Java Visual Application containing an Aquarium
 */
public class AquariumWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Aquarium component to be displayed
	 */
	private final Aquarium aquarium;
	/**
	 * Constant defining the margin surrounding the Aquarium component
	 */
	private final static int MARGIN = 10;

	
	
	public AquariumWindow(Aquarium aquarium) {
		this.aquarium = aquarium;
	}

	/**
	 * Displays the AquariumWindow using the defined margins, and call the
	 * {@link Aquarium#animate()} method of the {@link Aquarium} every 100ms
	 */
	public void displayOnscreen() {
		add(aquarium);
		setSize(Aquarium.getSizeX() + MARGIN, Aquarium.getSizeY() + MARGIN);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		/* Periodic task (every 100ms) calling animate method of the Aquarium */
		ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
		e.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				aquarium.animate();
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}
}
