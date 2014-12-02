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
	public static int decoder(String s,long idClient, Aquarium a, boolean server){
		
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
						a.getClass(a.getClasseIndex(contenu[7], idClient)).getImages(),
						contenu[7]));
			}else{				
				a.addObj(new Mobiles(Long.parseLong(contenu[1]), 
						Integer.parseInt(contenu[2]),
						Integer.parseInt(contenu[3]), 
						Integer.parseInt(contenu[4]), 
						Integer.parseInt(contenu[5]), 
						Integer.parseInt(contenu[6]), 
						a.getClass(a.getClasseIndex(contenu[7],Long.parseLong(contenu[1]))).getImages(),
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

	public static String encodeDisconnect(){
		return "DECONNECT";
	}

	public static String encodeNewFish(long idClient, long idFish, int width, int height, int x, int y, String nameClass) {
		return "ADDFISH"+"!"+idClient+"!"+idFish + "!" + width + "!" + height + "!" + x + "!" + y + "!" + nameClass;
	}

	public static String encodeKillFish(long idClient, long idFish){
		return "REMOVEFISH"+"!"+idClient+"!"+idFish;
	}

	public static String encodeMoveFish(long idClient, long idFish, int x, int y){
		return "MOVEFISH"+"!"+idClient+"!"+idFish+"!"+x+"!"+y;
	}

	public static String encodeNewClass(String nomClass){
		return "NEWCLASS"+"!"+nomClass;
	}

	public static String encodeSize(int size){
		return "SIZE"+"!"+size;
	}

	/**
	 * envoyer les classes produites dans cet aquarium
	 * @param out
	 * @param aqua
	 * @return
	 */
	public static boolean sendMyClasses(PrintWriter out, Aquarium aqua, long idPersonnel){
		try{
			int total = aqua.nbOfClientClasses(idPersonnel);
			out.println(encodeSize(total));
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

	/**
	 * Envoyer les poissons créés dans cet aquarium
	 * @param out
	 * @param aqua
	 * @return
	 */
	public static boolean sendMyFishs(PrintWriter out, Aquarium aqua){
		try{
			List<AquariumItem> MobileItems =  aqua.getMobileItems();
			int taille = MobileItems.size();
			out.println(encodeSize(taille));
			out.flush();
			for(int j = 0;j<taille;j++){
				String name = ((MobileItem) MobileItems.get(j)).getClasse();
				out.println(encodeNewFish(0,j,MobileItems.get(j).getWidth(),MobileItems.get(j).getHeight() ,MobileItems.get(j).getPosition().x, MobileItems.get(j).getPosition().y,name));
				out.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * receptionner des classes et les intégrer à l'aquarium.
	 * @param in
	 * @param aqua
	 * @param id
	 * @return
	 */
	public static boolean receiveClasses(BufferedReader in,Aquarium aqua, long idClient, boolean server){
		try{
			String tampon;
			tampon = in.readLine();
			int i = decoder(tampon,idClient, aqua, server);
			if(i>0){
				for(int j = 0 ; j < i ; j++){
					tampon = in.readLine();
					int k = decoder(tampon,idClient, aqua,server);
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

	/**
	 * receptionner des mobiles et les intégrer à l'aquarium.
	 * @param in
	 * @param aqua
	 * @param id
	 * @return
	 */
	public static boolean receiveFishs(BufferedReader in, Aquarium aqua, long id, boolean server){
		try{
			String tampon = in.readLine();
			int i = decoder(tampon,id,aqua,server);
			for (int j = 0; j < i; j++) {
				tampon = in.readLine();
				int k = decoder(tampon,id, aqua,server);
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
	 * @param in
	 * @param a
	 * @param id
	 * @return
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

	public static boolean sendMyPositions(PrintWriter out, Aquarium a, long id){
		try{
			List<List<Long>> mobiles = a.positionsMyFishs();
			int taille = mobiles.size();
			System.out.println("sendMyPositions "+taille);
			out.println(encodeSize(taille));
			out.flush();
			for(int i = 0 ;i < mobiles.size(); i++){
				System.out.println("sendMyPositions "+i+" "+id+" "+mobiles.get(i).get(0)+" "+mobiles.get(i).get(1)+" "+mobiles.get(i).get(2));
				out.println(encodeMoveFish(id,mobiles.get(i).get(0),(int)(long) mobiles.get(i).get(1),(int)(long) mobiles.get(i).get(2)));
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean receivePositions(BufferedReader in, Aquarium aqua, long id, boolean server){
		try{
			String tampon = in.readLine();
			System.out.println("receivePositions "+tampon);
			if(tampon != null){
				int i = decoder(tampon,id,aqua,server);
				for (int j = 0; j < i; j++) {
					tampon = in.readLine();
					int k = decoder(tampon,id, aqua,server);
					if(k == -1){
						throw new Exception();
					}
				}
				
				//le serveur envoit à partir de deux listes
				if(!server){
					tampon = in.readLine();
					i = decoder(tampon,id,aqua,server);
					for (int j = 0; j < i; j++) {
						tampon = in.readLine();
						int k = decoder(tampon,id, aqua,server);
						if(k == -1){
							throw new Exception();
						}
					}
				}
			}
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