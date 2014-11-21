package aquarium.items;

import java.awt.Point;
import java.util.List;

public class Mobiles extends AquariumItem {
	private int id [];
	
	public Mobiles(int idC, int idP, int width, int height,int x, int y, String clas){
		super(width, clas);
		super.setPosition(new Point(x,y));
		this.id[0] = idC;
		id[1] = idP;
	}
	
	public int getIdClient(){
		return id[0];
	}
	
	public int getIdPoisson(){
		return id[1];
	}	
}
