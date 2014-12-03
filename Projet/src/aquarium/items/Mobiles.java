package aquarium.items;

import java.awt.Point;

public class Mobiles extends AquariumItem {
	private String classe;
	private long idClient;
	private long idFish;
	
	public Mobiles(long idC, int idP, int width, int height,int x, int y, String clas, String classe){
		super(width, clas);
		super.setPosition(new Point(x,y));
		this.idClient = idC;
		idFish = idP;
		this.classe = classe;
	}
	
	public long getIdClient(){
		return idClient;
	}
	
	public long getIdPoisson(){
		return idFish;
	}	

	public String getClasse(){
		return classe;
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
