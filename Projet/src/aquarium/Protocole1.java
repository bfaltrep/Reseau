package aquarium;


import aquarium.gui.Aquarium;
import aquarium.items.Mobiles;

public class Protocole1 {

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