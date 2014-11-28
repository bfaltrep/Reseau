package aquarium.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import aquarium.ElementImage;
import aquarium.items.AquariumItem;
import aquarium.items.DorisFish;
import aquarium.items.Mobiles;
import aquarium.items.StableFish;
import aquarium.items.MobileItem;
import aquarium.items.Seastone;
import aquarium.items.Seaweed;

/**
 * An Aquarium is a Java graphical container that extends the JPanel class in
 * order to display graphical elements.
 */
public class Aquarium extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constant (c.f. final) common to all Aquarium instances (c.f. static)
	 * defining the background color of the Aquarium
	 */
	private static final Color backgroundColor = new Color(218, 238, 255);

	/**
	 * Constant common to all Aquarium instances defining the width of this last
	 */
	private static final int SIZE_AQUA_X = 800;
	/**
	 * Constant common to all Aquarium instances defining the height of this
	 * last
	 */
	private static final int SIZE_AQUA_Y = 600;

	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Stones items" to be displayed in the Aquarium
	 */
	private static final int NB_STONES = 6;
	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Seaweed items" to be displayed in the Aquarium
	 */
	private static final int NB_SEAWEED = 9;
	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Fish items" to be displayed in the Aquarium
	 */
	private static final int NB_FISH = 5;
	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Fish items" to be displayed in the Aquarium
	 */
	private static final int NB_DORISFISH = 5;

	/**
	 * Pixel data buffer for the Aquarium rendering
	 */
	private Image buffer = null;
	/**
	 * Graphic component context derived from buffer Image
	 */
	private Graphics graphicContext = null;

	/**
	 * List of Aquarium items to be rendered in the Aquarium
	 */
	private List<AquariumItem> items = new ArrayList<AquariumItem>();

	private List<ElementImage> classes = new ArrayList<ElementImage>();
			
	private List<Mobiles> others = new ArrayList<Mobiles>();
	
	
	
	public Aquarium() {

		classes.add(new ElementImage(0,StableFish.getImageClasse(),"StableFish"));
		classes.add(new ElementImage(0,DorisFish.getImageClasse(),"DorisFish"));
		
		for (int i = 0; i < NB_STONES; i++) {
			AquariumItem ai = new Seastone();
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_SEAWEED; i++) {
			AquariumItem ai = new Seaweed();
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_FISH; i++) {
			AquariumItem ai = new StableFish();
			
			if (ai.sink(items))
				items.add(ai);
		}
		for (int i = 0; i < NB_DORISFISH; i++) {
			AquariumItem ai = new DorisFish();
			if (ai.sink(items))
				items.add(ai);
		}
	}

	/**
	 * @return the width of the Aquarium
	 */
	public static int getSizeX() {
		return SIZE_AQUA_X;
	}

	/**
	 * @return the height of the Aquarium
	 */
	public static int getSizeY() {
		return SIZE_AQUA_Y;
	}
	
	/**
	 * 
	 * @param i, an integer between [0, number of items of the aquarium]
	 * @return the AquariumItem at i position.
	 */
	public AquariumItem getAquariumItem(int i){
		return items.get(i);
	}
	
	/**
	 * Proceeds to the movement of any movable AquariumItem and updates the
	 * screen
	 */
	public void animate() {
		for (AquariumItem item : items)
			if (item instanceof MobileItem)
				((MobileItem) item).move(items);
		for(AquariumItem item : others)
			((Mobiles) item).move();
		updateScreen();
	}

	/*
	 * (non-Javadoc) This method is called by the AWT Engine to paint what
	 * appears in the screen. The AWT engine calls the paint method every time
	 * the operative system reports that the canvas has to be painted. When the
	 * window is created for the first time paint is called. The paint method is
	 * also called if we minimize and after we maximize the window and if we
	 * change the size of the window with the mouse.
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, this);
	}

	/**
	 * Draw each Aquarium item based on new positions
	 */
	private void updateScreen() {

		if (buffer == null) {
			buffer = createImage(SIZE_AQUA_X, SIZE_AQUA_Y);
			if (buffer == null)
				throw new RuntimeException("Could not instanciate graphics");
			else
				graphicContext = buffer.getGraphics();
		}
		graphicContext.setColor(backgroundColor);
		graphicContext.fillRect(0, 0, SIZE_AQUA_X, SIZE_AQUA_Y);
		for (AquariumItem aquariumItem : items) {
			aquariumItem.draw(graphicContext);
		}
		for (AquariumItem aquariumItem : others) {
			aquariumItem.draw(graphicContext);
		}
		this.repaint();
	}

	
	
	
	
	
	
	
	//methodes de gestion des listes
	
	/**
	 * @return le nombre d'éléments mobiles dans l'aquarium qui sont créés par cet aquarium
	 */
	public List<Integer> getNBMobileItems(){
		List<Integer> tmp = new ArrayList<Integer>();
		for (int j = 0;j< items.size();j++){
			AquariumItem item = items.get(j);
			if (item instanceof MobileItem)
				tmp.add(j);
		}
		return tmp;
	}
	
	
	//gestion Classes
	
	/**
	 * Ajouter une classe dans la liste des classes de cet aquarium
	 * @param idC identifiant du client
	 * @param nom nom de la classe
	 * @param image adresse de l'image
	 */

	public void addClasses(long idC, String nom, String image){
		classes.add(new ElementImage(idC,nom,image));
	}
	
	/**
	 * retourne l'élément se trouvant à l'index i de la liste des éléments.
	 * @param i
	 * @return
	 */
	public ElementImage getClasse(int i){
		return classes.get(i);
	}
	
	public int getClasseIndex(String s){
		int index = 0;
		Iterator<ElementImage> it = classes.iterator(); 
		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if(tmp.getNom().matches(s)){
				return index;
			}
		}
		return -1;
	}	
	
	/**
	 * retourne le nombre de classes existantes pour ce client
	 * 
	 * **/
	public int ClassesNbPourClient(long idClient){
		int nb = 0;
		Iterator<ElementImage> it = classes.iterator(); 
		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if(tmp.getidClient() == idClient){
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * supprimer tous les poissons du client idC
	 * @param idC
	 */
	public void ClassesSupprMultiple(long idC){
		Iterator<ElementImage> it = classes.iterator(); 
		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if(tmp.getidClient() == idC){
				it.remove();
			}
		}	
	}
	
	//gestion Others
	
	/**
	 * ajouter un Mobiles dans others
	 * @param m
	 */
	public void addOther(Mobiles m){
		others.add(m);
	}
	
	/**
	 * parcourir others et retourner la position du poisson qui correspond aux id en paramètre
	 * @param client
	 * @param idPoisson
	 * @return
	 */
	public int parcourirOther (long client, int idPoisson){
		for(int i = 0;i<others.size(); i++){
			if(others.get(i).getIdClient() == client && others.get(i).getIdPoisson() == idPoisson){
				return i;
			}
		}
		return -1;
	}

	public void OtherSupprSimple(long client, int idPoisson){
		int index = parcourirOther (client,idPoisson);
		if(index != -1){
			others.remove(index);
		}
	}
	
	public void OtherModifPositionSimple(long client, int idPoisson, int x, int y){
		int index = parcourirOther (client,idPoisson);
		if(index != -1){
			others.get(index).setPosition(new Point(x,y));;
		}
	}
	
	public void OtherSupprMultiple(long client){
		Iterator<Mobiles> it = others.iterator(); 
		while (it.hasNext()) {
			Mobiles tmp = it.next();
			if(tmp.getIdClient() == client){
				it.remove();
			}
		}	
	}
	
}
