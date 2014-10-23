package aquarium.items;

import java.util.List;
import aquarium.util.RandomNumber;

/**
 * Fish element
 */
public class StableFish extends MobileItem {
	
	private static final int MIN_WIDTH = 30, MAX_WIDTH = 86;
	private static final String img = "image/fish.png";
	private AquariumItem target;
	
	public StableFish( String img) {
		super(MIN_WIDTH, MAX_WIDTH, img);
	}
	
	public StableFish() {
		super(MIN_WIDTH, MAX_WIDTH, img);
	}
	
	public StableFish(int width) {
		super(width, img);
	}

	@Override
	public AquariumItem getNewTarget(List<AquariumItem> neighbours) {
		int destination = RandomNumber.randomValue(0, neighbours.size());
		target= neighbours.get(destination);
		return target;
	}

	@Override
	public int getMaximalWidth() {
		return MAX_WIDTH;
	}

	protected void setTarget(AquariumItem a){
		target=a;
	}

	@Override
	public AquariumItem getCurrentTarget() {
		return target;
	}
	
}
