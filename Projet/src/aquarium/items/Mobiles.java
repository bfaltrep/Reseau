package aquarium.items;

import java.awt.Point;

public class Mobiles extends AquariumItem {
	private int id [];
	
	public Mobiles(int idC, int idP, int width, int height,int x, int y, String clas){
		super(width, clas);
		super.setPosition(new Point(x,y));
		id = new int[2];
		this.id[0] = idC;
		id[1] = idP;
	}
	
	public int getIdClient(){
		return id[0];
	}
	
	public int getIdPoisson(){
		return id[1];
	}	

	public void move() {
		double v = getWidth()/getWidth();
		int dx = (super.getPosition().x - super.getPosition().x);
		int dy = (super.getPosition().y - super.getPosition().y);
		double direction = (float)Math.atan2(dy, dx);
		double speed = 1.0+v;
		int modx = (int) (speed * Math.cos(direction));
		int mody = (int) (speed * Math.sin(direction));
		Point p = super.getPosition();
		p.translate(modx, mody);
		setPosition(p);
	}
	
}
