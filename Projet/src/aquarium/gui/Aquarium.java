package aquarium.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import aquarium.items.AquariumItem;
import aquarium.items.DorisFish;
import aquarium.items.Mobiles;
import aquarium.items.StableFish;
import aquarium.items.MobileItem;
import aquarium.items.Seastone;
import aquarium.items.Seaweed;
import aquarium.util.ClassesMobiles;

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

	//gestion de la concurrence
	private Object oitems;
	private Object oothers;
	private Object oclasses;
	
	
	/**
	 * Lists of Aquarium items to be rendered in the Aquarium
	 */
	private List<AquariumItem> items = new ArrayList<AquariumItem>();
	private List<Mobiles> others = new ArrayList<Mobiles>();	
	
	private List<ClassesMobiles> classes = new ArrayList<ClassesMobiles>();

	/**
	 * Constructor
	 * @param idC my identifiant.
	 */
	public Aquarium(long idC) {
		oitems = new Object();
		oothers = new Object();
		oclasses = new Object();
		
		
		classes.add(new ClassesMobiles(idC, StableFish.getImageClasse(), "StableFish"));
		classes.add(new ClassesMobiles(idC, DorisFish.getImageClasse(), "DorisFish"));

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
			AquariumItem ai = new StableFish(items.size()-1);
			if (ai.sink(items)) {
				items.add(ai);
			}
		}
		for (int i = 0; i < NB_DORISFISH; i++) {
			AquariumItem ai = new DorisFish(items.size()-1);
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
		synchronized(oitems){
			return items.get(i);
		}
	}

	/**
	 * Proceeds to the movement of any movable AquariumItem and updates the
	 * screen
	 */
	public void animate() {
		synchronized(oitems){
			for (AquariumItem item : items) {
				if (item instanceof MobileItem)
					((MobileItem)item).move(items);
			}
		}
		synchronized(oothers){
			for (AquariumItem item : others) 
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
		synchronized(oitems){
			for (AquariumItem aquariumItem : items) {
				aquariumItem.draw(graphicContext);
			}
		}
		synchronized(oothers){
			for (AquariumItem aquariumItem : others)
				aquariumItem.draw(graphicContext);
		}
		checkCollision();
		this.repaint();
	}

	//gestion de items
	/**
	 * @return la liste des mobiles dans l'aquarium qui sont créés par cet aquarium
	 */
	public List<AquariumItem> getMobileItems() {
		List<AquariumItem> tmp = new ArrayList<AquariumItem>();
		synchronized(oitems){
			for (int j = 0; j < items.size(); j++) {
				AquariumItem item = items.get(j);
				if (item instanceof MobileItem) {
					tmp.add(item);
				}
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
		synchronized(oitems){
			Iterator<AquariumItem> it = items.iterator();
			while (it.hasNext()) {
				AquariumItem tmp = it.next();
				if (tmp instanceof MobileItem) {
					Point p = tmp.getPosition();
					List<Long> element = new ArrayList<Long>();
					element.add(((MobileItem)tmp).getIdentifiant());
					element.add((long)p.x);
					element.add((long)p.y);
					res.add(element);
				}
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
		synchronized(oclasses){
			classes.add(new ClassesMobiles(idC,nom,image));
		}
	}

	/**
	 * Retourne l'élément se trouvant à l'index i dans la liste des classes
	 * @param i l'index de l'élément que l'on veut récupérer
	 * @return l'élément demandé
	 */
	public ClassesMobiles getClasse(int i) {
		synchronized(oclasses){
			return classes.get(i);
		}
	}

	/**
	 * Retourne l'index de la classe dans la liste.
	 * @param s La classe a rechercher
	 * @return -1 si elle n'existe pas. Sinon retourne l'index de la classe.
	 */
	public int getClasseIndex(String s, long idC) {
		int index = 0;
		synchronized(oclasses){
			Iterator<ClassesMobiles> it = classes.iterator(); 
			int i = 0;
			while (it.hasNext()) {
				ClassesMobiles tmp = it.next();
				if (tmp.getNom().matches(s) && tmp.getidClient() == idC) {
					index = i;
					return index;
				}
				i++;
			}
		}
		return -1;
	}

	/**
	 * Retourne le nombre de classes d'un client
	 *@param idClient 
	 */
	public int nbOfClientClasses(long idClient) {
		int nb = 0;
		synchronized (oclasses){
			Iterator<ClassesMobiles> it = classes.iterator(); 
			while (it.hasNext()) {
				ClassesMobiles tmp = it.next();
				if (tmp.getidClient() == idClient) {
					nb++;
				}
			}
		}
		return nb;
	}

	/**
	 * Supprime toutes les classes d'un client
	 * @param idClient l'identifiant du client dont on souhaite supprimer les classes
	 */
	public void deleteMultipleClasses(long idClient) {
		synchronized(oclasses){
			Iterator<ClassesMobiles> it = classes.iterator();
			while (it.hasNext()) {
				ClassesMobiles tmp = it.next();
				if (tmp.getidClient() == idClient) {
					it.remove();
				}
			}
		}
	}

	//gestion Others
	/**
	 * Ajoute un Mobile dans la liste de l'aquarium 
	 * @param m Le Mobiles à ajouter
	 */
	public void addObj(Mobiles m) {
		synchronized(oothers){
			others.add(m);
		}
	}

	/**
	 * Parcoure la liste des objets d'un client et retourne la position de l'objet recherché
	 * 	 * @param idClient l'identifiant du client dont on souhaite récupérer un Mobiles
	 * @param idPoisson l'identifiant du Mobiles qui nous intéresse
	 * @return l'index du Mobiles recherché ou -1 s'il n'a pas été trouvé
	 */
	public Mobiles browseClientObj(long idClient, int idPoisson) {
		System.out.println("browseClientObj client : "+idClient+" poisson : "+idPoisson);
		synchronized(oothers){
			for (int i = 0; i < others.size(); i++) {
				System.out.println("browseClientObj i "+others.get(i)+" client : "+others.get(i).getIdClient()+" poisson : "+others.get(i).getIdPoisson());
				if (others.get(i).getIdClient() == idClient && others.get(i).getIdPoisson() == idPoisson) {
					return others.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * Supprimer un objet mobiles venant d'un autre client
	 * @param idClient l'identifiant du client dont on souhaite supprimer un mobiles
	 * @param idPoisson l'identifiant du Mobiles qui nous intéresse
	 */	
	public void deleteSingleClientObj(long idClient, int idPoisson) {
		
		/*
		int index = browseClientObj(idClient, idPoisson);
		if (index != -1) {
			synchronized(oothers){
				others.remove(index);
			}
		}*/
	}

	/**
	 * Retourne l'identifiant et la position de chaque Mobiles n'appartenant pas au client précisé.
	 * Formaté en liste de liste : chaque sous-liste contient les élément du Mobiles formaté :
	 * 		- idclient
	 * 		- idMobiles
	 * 		- axe abscisse : x
	 * 		- axe ordonné : y
	 * @param idClient l'identifiant du client dont les Mobiles nous intéressent
	 * @return une liste contenant les identifiant et les coordonnées de tous les Mobiles d'un client
	 */
	public List<List<Long>> positionsClientObj(long idClient){
		List<List<Long>> res = new ArrayList<List<Long>>();
		synchronized(oothers){
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
		}
		return res;
	}

	/**
	 * Modifier la position d'un poisson venant d'un autre client
	 * @param idClient l'identifiant du client dont les Mobiles nous intéressent
	 * @param idPoisson L'identifiant du Mobiles à modifier
	 * @param x la valeur à placer pour l'axe des abscisses
	 * @param y la valeur à placer pour l'axe des ordonnés
	 */
	public void modifySingleClientObj(long idClient, int idPoisson, int x, int y) {
		Mobiles m = browseClientObj(idClient, idPoisson);
		System.out.println("modifySingleClientObj  "+m);
		if (m != null) {
			synchronized(oothers){
				m.setPosition(new Point(x,y));//others.get(index).setPosition(new Point(x,y));
			}
		}
	}

	/**
	 * Supprime tout les objets appartenant à un client
	 * @param idClient Identifiant du client dont on souhaite supprimer les objets
	 */
	public void deleteMultipleClientObj(long idClient) {
		synchronized(oothers){
			Iterator<Mobiles> it = others.iterator();
			while (it.hasNext()) {
				Mobiles tmp = it.next();
				if (tmp.getIdClient() == idClient) {
					it.remove();
				}
			}	
		}
	}

	/**
	 * supprime les données retenues pour un client.
	 * @param idClient identifiant du client dont on souhaite supprimer les données
	 */
	public void disconnectClient(long idClient) {
		deleteMultipleClasses(idClient);
		deleteMultipleClientObj(idClient);
	}

	/**
	 * Recupérer la liste des Mobiles de tous les clients excepté un.
	 * @param idClient Identifiant du seul client dont on ne souhaite pas avoir les Mobiles
	 * @return Liste de Mobiles
	 */
	public List<Mobiles> getMobilesOthersExceptOne(long idClient){
		List<Mobiles> res = new ArrayList<Mobiles>();
		synchronized(oothers){
			Iterator<Mobiles> it = others.iterator();
			while (it.hasNext()) {
				Mobiles tmp = it.next();
				if(tmp.getIdClient() != idClient){
					res.add(tmp);
				}
			}
		}
		return res;
	}
	
	public void checkCollision() {
		for (int i = 0; i < items.size() - 1; i++) {
			for (int j = i + 1; j < items.size(); j++) {
				if (items.get(i).overlap(items.get(j))) {
					if (items.get(i).getClasse().equalsIgnoreCase(items.get(j).getClasse())) {
						if (items.get(i).getClasse().equalsIgnoreCase("StableFish")) {
							AquariumItem ai = new StableFish(items.size()-1);

							if (ai.sink(items)) {
								items.add(ai);
							}
						}
						else if (items.get(i).getClasse().equalsIgnoreCase("DorisFish")) {
							AquariumItem ai = new DorisFish(items.size()-1);
							
							if (ai.sink(items)) {
								items.add(ai);
							}
						}
						break;
					}
					if (!items.get(i).getClasse().equalsIgnoreCase(items.get(j).getClasse())) {

						if (items.get(i).getClasse().equalsIgnoreCase("StableFish")) {
							items.remove(j);
							break;
						}
						else if (items.get(i).getClasse().equalsIgnoreCase("DorisFish")) {
							items.remove(i);
							break;
						}
					}
				}
			}
		}
		
		for (int i = 0; i < items.size() - 1; i++) {
			for (int j = 0; j < others.size(); j++) {
				if (items.get(i).overlap(others.get(j))) {
					if (items.get(i).getClasse().equalsIgnoreCase(others.get(j).getClasse())) {
						if (items.get(i).getClasse().equalsIgnoreCase("StableFish")) {
							AquariumItem ai = new StableFish(items.size()-1);

							if (ai.sink(items)) {
								items.add(ai);
							}
						}
						else if (items.get(i).getClasse().equalsIgnoreCase("DorisFish")) {
							AquariumItem ai = new DorisFish(items.size()-1);
							
							if (ai.sink(items)) {
								items.add(ai);
							}
						}
						break;
					}
					if (!items.get(i).getClasse().equalsIgnoreCase(others.get(j).getClasse())) {

						if (items.get(i).getClasse().equalsIgnoreCase("StableFish")) {
							others.remove(j);
							break;
						}
						else if (items.get(i).getClasse().equalsIgnoreCase("DorisFish")) {
							items.remove(i);
							break;
						}
					}
				}
			}
		}
	}
}


