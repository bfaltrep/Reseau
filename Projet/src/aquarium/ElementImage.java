package aquarium;

import java.awt.Image;
import java.awt.Toolkit;

public class ElementImage {
	private int idClient;
	private String imageS; // supposé être temporaire. On doit trouver comment charger l'image pour chaque poisson.
	private Image image;
	private String nom;
	
	public ElementImage(int idC, String s, String i){
		idClient = idC;
		imageS = s;
		image = Toolkit.getDefaultToolkit().createImage(
				ClassLoader.getSystemResource(i));
		//ImageIcon icon = new ImageIcon(image); verifier l'utilité
		nom = s;
	} 
	
	public int getidClient(){
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
