package aquarium;

import java.awt.Image;
import java.awt.Toolkit;

public class ElementImage {
	private long idClient;
	private String imageS; // supposé être temporaire. On doit trouver comment charger l'image pour chaque poisson.
	private Image image;
	private String nom;
	
	public ElementImage(long idC, String img, String n){
		idClient = idC;
		imageS = img;
		image = Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource(img));
		//ImageIcon icon = new ImageIcon(image); verifier l'utilité
		nom = n;
	} 
	
	public long getidClient(){
		return idClient;
	}
	
	public String getNom(){
		return nom;
	}
	
	public Image getImage (){
		return image;
	}
	
	public String getImages(){
		return imageS;
	}
	
}
