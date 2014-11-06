package aquarium.items;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DorisFish extends StableFish {

	private static final String img = "image/dory.png";
	
	public DorisFish() {
		super(img);
		setBehavior();
	}
	
	public DorisFish(int width) {
		super(width);
		setBehavior();
	}

	private void setBehavior(){
		ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
		e.scheduleWithFixedDelay(new Runnable() {
			
			@Override
			public void run() {
				setTarget(null);
				
			}
		}, 0, 100, TimeUnit.MILLISECONDS);
	}
	
}
