package aquarium;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;
import aquarium.items.Mobiles;

public class Protocole1 {

	/**
	 * Decoder le contenu du string et l'appliquer sur l'aquarium
	 * @param message
	 * @param identifiant du client
	 * @param Aquarium manipuler
	 * @return la réussite des manipulation : si au moins un echec, return false, si tout est réussit retourne true;
	 */
	public static boolean decoder(String s,long idClient, Aquarium a, boolean server){

		//System.out.println("reception "+s);
		
		String  messageEntier[] = s.split("#"); 
		if (messageEntier.length != 0){
			for(int i =0; i<messageEntier.length;i++){
				int test = treatCommand(messageEntier[i],idClient,a,server);
				if (test == -1)
					return false;
			}
			return true;
		}else{
			System.out.println("Protocole1: Erreur lors du décodage du message : "+s);
			return false;
		}
		
	}

	/**
	 * Traitement de chaque commande séparément.
	 * @param commande
	 * @param identifiant de l'entite envoyant
	 * @param aquarium
	 * @param si est le serveur, mettre true, sinon mettre false
	 * @return un entier indiquant soit : 
	 * 		- -1 :problème lors de la manipulation
	 * 		- 0 : la commande n'est pas un size et s'est bien passée
	 * 		- valeur positive : la commande est un size de cette longueur
	 */
	private static int treatCommand(String s,long idClient, Aquarium a, boolean server){

		String contenu [] = s.split("!");
		String commande = contenu[0];

		switch (commande){
		case "DECONNECT":
			//message  : CMD
			a.disconnectClient(idClient);
			break;
		case "ADDFISH":
			if(server){
				//message  : CMD!idClient!idFish!width!height!x!y!StringClass
				a.addObj(new Mobiles(idClient, 
						Integer.parseInt(contenu[2]),
						Integer.parseInt(contenu[3]), 
						Integer.parseInt(contenu[4]), 
						Integer.parseInt(contenu[5]), 
						Integer.parseInt(contenu[6]), 
						a.getClasse(a.getClasseIndex(contenu[7], idClient)).getImages(),
						contenu[7]));
			}else{	
				a.addObj(new Mobiles(Long.parseLong(contenu[1]), 
						Integer.parseInt(contenu[2]),
						Integer.parseInt(contenu[3]), 
						Integer.parseInt(contenu[4]), 
						Integer.parseInt(contenu[5]), 
						Integer.parseInt(contenu[6]), 
						a.getClasse(a.getClasseIndex(contenu[7],Long.parseLong(contenu[1]))).getImages(),
						contenu[7]));
			}
			break;
		case "REMOVEFISH":
			//message  : CMD!idClient!idFish
			if(server){
				a.deleteSingleClientObj(idClient,Integer.parseInt(contenu[1]));
			}else{
				a.deleteSingleClientObj(Long.parseLong(contenu[1]),Integer.parseInt(contenu[2]));
			}
			break;
		case "MOVEFISH":
			//message  : CMD!idClient!idFish!x!y
			if(server){
				a.modifySingleClientObj(idClient,
						Integer.parseInt(contenu[2]),
						Integer.parseInt(contenu[3]),
						Integer.parseInt(contenu[4]));
			}else{
				a.modifySingleClientObj(Long.parseLong(contenu[1]),
						Integer.parseInt(contenu[2]),
						Integer.parseInt(contenu[3]),
						Integer.parseInt(contenu[4]));
			}
			break;
		case "NEWCLASS":
			//version temporaire : importation image à venir
			//message  : CMD!nomClasse
			a.addClasses(idClient,"image/polochon.png",contenu[1]);
			break;
		case "SIZE":
			//message  : CMD!NB
			return Integer.parseInt(contenu[1]);

		default: 
			System.out.println("Reception d'un message non conforme. ");
			System.out.println("non traité : "+s);
			return -1;
		}
		return 0;
	}
	
	/**
	 * Prévenir le serveur que l'on se déconnecte.
	 * @return
	 */
	public static String encodeDisconnect(){
		return "DECONNECT#";
	}

	/**
	 * Mettre en forme un message de création d'un Mobiles.
	 * @param idClient Identifiant du client (mon identifiant)
	 * @param idFish identifiant du Mobiles à créer
	 * @param width largeur de l'image du Mobiles
	 * @param height hauteur de l'image du Mobiles
	 * @param x valeur de l'abscisse pour la position initiale du Mobiles
	 * @param y valeur de l'ordonne pour la position initiale du Mobiles
	 * @param nameClass nom de la classe du Mobiles
	 * @return Le string contenant toutes les informations formatées
	 */
	public static String encodeNewFish(long idClient, long idFish, int width, int height, int x, int y, String nameClass) {
		return "ADDFISH"+"!"+idClient+"!"+idFish + "!" + width + "!" + height + "!" + x + "!" + y + "!" + nameClass+"#";
	}

	/**
	 * Mettre en forme un message de suppression d'un Mobiles.
	 * @param idClient identifiant du client (mon identifiant)
	 * @param idFish identifiant du Mobiles à supprimer
	 * @return
	 */
	public static String encodeKillFish(long idClient, long idFish){
		return "REMOVEFISH"+"!"+idClient+"!"+idFish+"#";
	}

	public static String encodeMoveFish(long idClient, long idFish, int x, int y){
		return "MOVEFISH"+"!"+idClient+"!"+idFish+"!"+x+"!"+y+"#";
	}

	public static String encodeNewClass(String nomClass){
		return "NEWCLASS"+"!"+nomClass+"#";
	}

	public static String encodeSize(int size){
		return "SIZE"+"!"+size+"#";
	}

	/**
	 * Receptionner les positions d'un client.
	 * @param in Buffer de lecture à partir du bon client
	 * @param aqua Aquarium contenant les infos a envoyer
	 * @param id identifiant du client qui m'a transmit ces données 
	 * @param server true si je suis le serveur, false si je suis un client
	 * @return retourne la réussite de la manipulation
	 */
	public static boolean receivePositions(BufferedReader in, Aquarium aqua, long id, boolean server){
		boolean test = false;
		try{
			String tampon = in.readLine();
			if(tampon != null){
				test = decoder(tampon,id,aqua,server);
				if (test == false)
					return false;
				//le serveur envoit à partir de deux listes
				if(!server){
					tampon = in.readLine();
					test = decoder(tampon,id,aqua,server);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return test;
	} 
	
	/**
	 * receptionner un ensemble de classes ou de Mobiles et les intégrer à l'aquarium.
	 * @param in Buffer de lecture à partir du bon client
	 * @param aqua Aquarium contenant les infos a envoyer
	 * @param idClient identifiant du client qui m'a transmit ces données 
	 * @param server true si je suis le serveur, false si je suis un client
	 * @return retourne la réussite de la manipulation
	 */
	public static boolean receptionInit(BufferedReader in,Aquarium aqua, long idClient, boolean server){
		try{
			String tampon;
			tampon = in.readLine();
			return decoder(tampon,idClient, aqua, server);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * envoyer les classes produites dans cet aquarium
	 * @param out buffer dirigé vers le bon correspondant pour envoyer
	 * @param aqua Aquarium contenant les infos a envoyer
	 * @param idPersonnel mon identifiant
	 * @return retourne la réussite de la manipulation
	 */
	public static boolean sendMyClasses(PrintWriter out, Aquarium aqua, long idPersonnel){
		try{			
			int total = aqua.nbOfClientClasses(idPersonnel);
			out.flush();
			for(int j = 0;j < total;j++){
				out.println(encodeNewClass(aqua.getClasse(j).getNom())); //envoi d'une classe, MODIF après gestion de l'image => envoyer une image
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Envoyer les poissons créés dans cet aquarium
	 * @param out Buffer d'écriture dirigé vers le bon client
	 * @param aqua Aquarium contenant les informations à transmettre
	 * @return la reussite de la manipulation
	 */
	public static boolean sendMyFishs(PrintWriter out, Aquarium aqua, long identifiant){
		try{
			List<AquariumItem> MobileItems =  aqua.getMobileItems();
			int taille = MobileItems.size();
			out.println(encodeSize(taille));
			out.flush();
			for(int j = 0;j<taille;j++){
				String name = ((MobileItem) MobileItems.get(j)).getClasse();
				out.println(encodeNewFish(identifiant,((MobileItem) MobileItems.get(j)).getIdentifiant(),MobileItems.get(j).getWidth(),MobileItems.get(j).getHeight() ,MobileItems.get(j).getPosition().x, MobileItems.get(j).getPosition().y,name));
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Envoyer Les poissons que je possède mais venant d'autres aquariums excepté l'id précisé
	 * @param out Buffer d'écriture dirigé vers le bon client
	 * @param a Aquarium contenant les informations à transmettre
	 * @param id Identifiant du client dont on ne veut pas envoyer les Mobiles
	 * @return la reussite de la manipulation
	 */
	public static boolean sendOthersFishes(PrintWriter out, Aquarium a, long id){
		try{
			List<Mobiles> MobileItems =  a.getMobilesOthersExceptOne(id);
			int taille = MobileItems.size();
			out.println(encodeSize(taille));
			out.flush();
			for(int j = 0;j<taille;j++){
				out.println(encodeNewFish(
						MobileItems.get(j).getIdClient(),
						MobileItems.get(j).getIdPoisson(),
						MobileItems.get(j).getWidth(),
						MobileItems.get(j).getHeight(),
						MobileItems.get(j).getPosition().x,
						MobileItems.get(j).getPosition().y,MobileItems.get(j).getClasse()));
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Envoyer les positions des mobiles extérieurs qui ne viennent pas de id
	 * @param in Buffer de lecture à partir du bon client
	 * @param a Aquarium contenant les informations à transmettre
	 * @param id Identifiant du client
	 * @return la reussite de la manipulation
	 */
	public static boolean sendPositionsOthers(PrintWriter out, Aquarium a, long id){
		try{
			List<List<Long>> mobiles = a.positionsClientObj(id);
			int taille = mobiles.size();
			out.println(encodeSize(taille));
			out.flush();
			for(int i = 0 ;i < mobiles.size(); i++)
				out.println(encodeMoveFish(mobiles.get(i).get(0),mobiles.get(i).get(1),(int)(long) mobiles.get(i).get(2),(int)(long) mobiles.get(i).get(3)));
		}catch(Exception e){
			e.printStackTrace();
			return false;				
		}
		return true;
	}

	/**
	 * Envoi les positions des MobileItems créés dans cet aquarium
	 * @param out Buffer d'écriture dirigé vers le bon client
	 * @param a Aquarium contenant les informations à transmettre
	 * @param id Identifiant du client
	 * @return la reussite de la manipulation
	 */
	public static boolean sendMyPositions(PrintWriter out, Aquarium a, long id){
		try{
			List<List<Long>> mobiles = a.positionsMyFishs();
			int taille = mobiles.size();
			out.println(encodeSize(taille));
			out.flush();
			for(int i = 0 ;i < mobiles.size(); i++){
				out.println(encodeMoveFish(id,mobiles.get(i).get(0),(int)(long) mobiles.get(i).get(1),(int)(long) mobiles.get(i).get(2)));
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

}