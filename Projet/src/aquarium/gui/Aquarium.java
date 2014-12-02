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
	private static final int NB_FISH = 3;

	/**
	 * Constant common to all Aquarium instances defining the number of
	 * "Fish items" to be displayed in the Aquarium
	 */
	private static final int NB_DORISFISH = 3;

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
	private List<Mobiles> others = new ArrayList<Mobiles>();	

	private List<ElementImage> classes = new ArrayList<ElementImage>();

	public Aquarium(long idC) {

		classes.add(new ElementImage(idC, StableFish.getImageClasse(), "StableFish"));
		classes.add(new ElementImage(idC, DorisFish.getImageClasse(), "DorisFish"));

		for (int i = 0; i < NB_STONES; i++) {
			AquariumItem ai = new Seastone();

			if (ai.sink(items)) {
				items.add(ai);
			}
		}
		for (int i = 0; i < NB_SEAWEED; i++) {
			AquariumItem ai = new Seaweed();

			if (ai.sink(items)) {
				items.add(ai);
			}
		}
		for (int i = 0; i < NB_FISH; i++) {
			AquariumItem ai = new StableFish();

			if (ai.sink(items)) {
				items.add(ai);
			}
		}
		for (int i = 0; i < NB_DORISFISH; i++) {
			AquariumItem ai = new DorisFish();

			if (ai.sink(items)) {
				items.add(ai);
			}
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
	public AquariumItem getAquariumItem(int i) {
		return items.get(i);
	}

	/**
	 * Proceeds to the movement of any movable AquariumItem and updates the
	 * screen
	 */
	public void animate() {
		for (AquariumItem item : items) {
			if (item instanceof MobileItem) {
				((MobileItem)item).move(items);
			}
		}
		for (AquariumItem item : others) {
			((Mobiles)item).move();
		}
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
			if (buffer == null) {
				throw new RuntimeException("Could not instanciate graphics");
			}
			else {
				graphicContext = buffer.getGraphics();
			}
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
	//gestion de items
	/**
	 * @return la liste des mobiles dans l'aquarium qui sont créés par cet aquarium
	 */
	public List<Integer> getMobileItems() {
		List<Integer> tmp = new ArrayList<Integer>();

		for (int j = 0; j < items.size(); j++) {
			AquariumItem item = items.get(j);
			if (item instanceof MobileItem) {
				tmp.add(j);
			}
		}
		return tmp;
	}

	
	/**
	 * Retourne une liste des identifiants/positions des instances mobiles créées dans cet aquarium
	 * @return
	 */
	public List<List<Long>> positionsMyFishs(){
		List<List<Long>> res = new ArrayList<List<Long>>();
		int i=0;
		Iterator<AquariumItem> it = items.iterator();
		while (it.hasNext()) {
			AquariumItem tmp = it.next();
			if (tmp instanceof MobileItem) {
				Point p = tmp.getPosition();
				List<Long> element = new ArrayList<Long>();
				element.add((long)i);
				element.add((long)p.x);
				element.add((long)p.y);
				res.add(element);
			}
		}
		return res;
	}
	
	//gestion Classes
	/**
	 * Ajouter une classe
	 * @param idC identifiant du client
	 * @param nom nom de la classe
	 * @param image adresse de l'image
	 */
	public void addClasses(long idC, String nom, String image) {
		classes.add(new ElementImage(idC,nom,image));
	}

	/**
	 * Retourne l'élément se trouvant à l'index i dans la liste des éléments
	 * @param i
	 * @return
	 */
	public ElementImage getClass(int i) {
		return classes.get(i);
	}

	/**
	 * Retourne l'index de la classe dans la liste.
	 * @param s La classe a rechercher
	 * @return -1 si elle n'existe pas. Sinon retourne l'index de la classe.
	 */
	public int getClasseIndex(String s, long idC) {
		int index = 0;
		Iterator<ElementImage> it = classes.iterator(); 
		int i = 0;
		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if (tmp.getNom().matches(s) && tmp.getidClient() == idC) {
				index = i;
				return index;
			}
			i++;
		}
		return -1;
	}

	/**
	 * Retourne le nombre de classes d'un client
	 *@param idClient 
	 */
	public int nbOfClientClasses(long idClient) {
		int nb = 0;
		Iterator<ElementImage> it = classes.iterator(); 

		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if (tmp.getidClient() == idClient) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * Supprime toutes les classes d'un client
	 * @param idClient
	 */
	public void deleteMultipleClasses(long idClient) {
		Iterator<ElementImage> it = classes.iterator();

		while (it.hasNext()) {
			ElementImage tmp = it.next();
			if (tmp.getidClient() == idClient) {
				it.remove();
			}
		}	
	}

	//gestion Others
	/**
	 * Ajoute un objet
	 * @param m
	 */
	public void addObj(Mobiles m) {
		others.add(m);
	}

	/**
	 * Parcoure la liste des objets d'un client et retourne la position de l'objet recherché
	 * 	 * @param idClient
	 * @param idPoisson
	 * @return
	 */
	public int browseClientObj(long idClient, int idPoisson) {
		for (int i = 0; i < others.size(); i++) {
			if (others.get(i).getIdClient() == idClient && others.get(i).getIdPoisson() == idPoisson) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Supprimer un objet mobiles venant d'un autre client
	 * @param idClient
	 * @param idPoisson
	 */	
	public void deleteSingleClientObj(long idClient, int idPoisson) {
		int index = browseClientObj(idClient, idPoisson);

		if (index != -1) {
			others.remove(index);
		}
	}

	/**
	 * Retourne l'identifiant et la position de chaque Mobiles n'appartenant pas au client précisé
	 * @return
	 */
	public List<List<Long>> positionsClientObj(long idClient){
		List<List<Long>> res = new ArrayList<List<Long>>();
		Iterator<Mobiles> it = others.iterator();
		while (it.hasNext()) {
			Mobiles tmp = it.next();
			if (tmp.getIdClient() != idClient) {
				Point p = tmp.getPosition();
				List<Long> element = new ArrayList<Long>();
				element.add(tmp.getIdClient());
				element.add(tmp.getIdPoisson());
				element.add((long)p.x);
				element.add((long)p.y);
				res.add(element);
			}
		}
		return res;
	}

	/**
	 * Modifier la position d'un poisson venant d'un autre client
	 * @param idClient
	 * @param idPoisson
	 * @param x
	 * @param y
	 */
	public void modifySingleClientObj(long idClient, int idPoisson, int x, int y) {
		int index = browseClientObj(idClient, idPoisson);

		System.out.println("modifySingleClientObj "+index);
		if (index != -1) {
			others.get(index).setPosition(new Point(x,y));
		}
	}

	/**
	 * Supprime tout les objets appartenant à un client
	 * @param idClient
	 */
	public void deleteMultipleClientObj(long idClient) {
		Iterator<Mobiles> it = others.iterator();

		while (it.hasNext()) {
			Mobiles tmp = it.next();
			if (tmp.getIdClient() == idClient) {
				it.remove();
			}
		}	
	}

	public void disconnectClient(long idClient) {
		deleteMultipleClasses(idClient);
		deleteMultipleClientObj(idClient);
	}

	public List<Mobiles> getMobilesOthersExceptOne(long idClient){
		List<Mobiles> res = new ArrayList<Mobiles>();
		Iterator<Mobiles> it = others.iterator();
		while (it.hasNext()) {
			Mobiles tmp = it.next();
			if(tmp.getIdClient() != idClient){
				res.add(tmp);
			}
		}
		return res;
	}
	
	/*
	public void afficherClass (){
		Iterator<ElementImage> it = classes.iterator(); 
		int i = 0;
		while (it.hasNext()) {
			ElementImage tmp = it.next();
			System.out.println("element "+i+" : idclient "+tmp.getidClient()+" nom "+tmp.getNom()+" image "+tmp.getImages());
			i++;
		}}*/
	/*
	public void afficherMobiles (){
		Iterator<Mobiles> it = others.iterator(); 
		int i = 0;
		while (it.hasNext()) {
			Mobiles tmp = it.next();
			System.out.println("element "+i+" : position "+tmp.getPosition().toString()+" nom "+tmp.+" image "+tmp.getImages());
			i++;
		}
	}*/
}
