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
	
	public StableFish(String img, long id) {
		super(MIN_WIDTH, MAX_WIDTH, img, id);
	}
	
	public StableFish(long id) {
		super(MIN_WIDTH, MAX_WIDTH, img, id);
	}
	
	public StableFish(int width, long id) {
		super(width, img, id);
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
	
	public String getClasse(){
		return "StableFish";
	}
	
	public static String getImageClasse(){
		return img;
	}
	
	public static String getImage(){
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
