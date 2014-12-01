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
	 * @param s
	 * @param idClient
	 * @param a
	 * @return
	 */
	public static int decoder(String s,long idClient, Aquarium a){
		String contenu [] = s.split("!");
		String commande = contenu[0];
		
		switch (commande){
		case "DECONNECT":
			//message  : CMD
			a.disconnectClient(idClient);
			break;
		case "ADDFISH":
			//message  : CMD!idFish!width!height!x!y!StringClass
			a.addObj(new Mobiles(idClient, 
					Integer.parseInt(contenu[0]),
					Integer.parseInt(contenu[1]), 
					Integer.parseInt(contenu[2]), 
					Integer.parseInt(contenu[3]), 
					Integer.parseInt(contenu[4]), 
					a.getClass(a.getClasseIndex(contenu[5])).getImages()));
			break;
		case "REMOVEFISH":
			//message  : CMD!idFish
			a.deleteSingleClientObj(idClient,Integer.parseInt(contenu[1]));
			break;
		case "MOVEFISH":
			//message  : CMD!idFish!x!y
			a.modifySingleClientObj(idClient,
					Integer.parseInt(contenu[0]),
					Integer.parseInt(contenu[1]),
					Integer.parseInt(contenu[2]));
			break;
		case "NEWCLASS":
			//message  : CMD!nomClasse
			a.addClasses(idClient,contenu[1],"image/polochon.jpg");
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
	
	public static String encodeDisconnect(){
		return "DECONNECT";
	}
	
	public static String encodeNewFish(int idFish, int width, int height, int x, int y, String nameClass) {
		return "ADDFISH"+"!"+idFish + "!" + width + "!" + height + "!" + x + "!" + y + "!" + nameClass;
	}
	
	public static String encodeKillFish(int idFish){
		return "REMOVEFISH"+"!"+idFish;
	}
	
	public static String encodeMoveFish(int idFish, int x, int y){
		return "MOVEFISH"+"!"+idFish+"!"+x+"!"+y;
	}

	public static String encodeNewClass(String nomClass){
		return "NEWCLASS"+"!"+nomClass;
	}
	
	public static boolean sendMyClasses(PrintWriter out, Aquarium aqua){
		try{
			int total = aqua.nbOfClientClasses(0);
			out.println(total);
			out.flush();
			for(int j = 0;j < total;j++){
				out.println(encodeNewClass(aqua.getClass(j).getNom())); //envoi d'une classe, MODIF après gestion de l'image
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean sendMyFishs(PrintWriter out, Aquarium aqua){
		try{
			List<Integer> MobileItems =  aqua.getMobileItems();
			int taille = MobileItems.size();
			out.println(taille);
			out.flush();
			for(int j = 0;j<taille;j++){
				AquariumItem ai = aqua.getAquariumItem(MobileItems.get(j));
				String name = ((MobileItem) ai).getClasse();
				out.println(encodeNewFish(j,ai.getWidth(),ai.getHeight() ,ai.getPosition().x, ai.getPosition().y,name));
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean receiveClasses(BufferedReader in,Aquarium aqua, long id){
		try{
			String tampon;
			tampon = in.readLine();
			int i = decoder(tampon,id, aqua);
			if(i>0){
				for(int j = 0 ; j < i ; j++){
					tampon = in.readLine();
					int k = decoder(tampon,id, aqua);
					if(k == -1){
						throw new Exception();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean receiveFishs(BufferedReader in, Aquarium aqua, long id){
		try{
			String tampon = in.readLine();
			int i = decoder(tampon,id,aqua);
			for (int j = 0; j < i; j++) {
				tampon = in.readLine();
				int k = decoder(tampon,id, aqua);
				if(k == -1){
					throw new Exception();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Envoyer les positions des mobiles extérieurs qui ne viennent pas de id
	 * @param in
	 * @param a
	 * @param id
	 * @return
	 */
	public static boolean sendPositionsOthers(BufferedReader in, Aquarium a, long id){
		try{
			
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	return true;
	}
	
/* traitement par byte[]
	public static void decodeFishByte(byte input[])	throws UnsupportedEncodingException {
		String rawInput = new String(input, "UTF-8");
		String processedInput[] = rawInput.split("!");
		int i = 0;

		while (i < processedInput.length) {
			System.out.println(processedInput[i]);
			i++;
		}
	}*/

}