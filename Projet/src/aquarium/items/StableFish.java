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
	
	public StableFish(String img, String address) {
		super(MIN_WIDTH, MAX_WIDTH, img, address);
	}
	
	public StableFish(String address) {
		super(MIN_WIDTH, MAX_WIDTH, img, address);
	}
	
	public StableFish(int width, String address) {
		super(width, img, address);
	}

	@Override
	public AquariumItem getNewTarget(List<AquariumItem> neighbours) {
		int destination = RandomNumber.randomValue(0, neighbours.size()-1);
		target= neighbours.get(destination);
		return target;
	}

	@Override
	public int getMaximalWidth() {
		return MAX_WIDTH;
	}
	
	public String getImage(){
		return img;
	}

	protected void setTarget(AquariumItem a){
		target=a;
	}

	@Override
	public AquariumItem getCurrentTarget() {
		return target;
	}
	
}
