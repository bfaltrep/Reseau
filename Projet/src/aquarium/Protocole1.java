package aquarium;

import java.io.UnsupportedEncodingException;

import aquarium.gui.Aquarium;
import aquarium.items.Mobiles;
import aquarium.util.Constante;

public class Protocole1 {

	public static boolean decoder(String s,long idClient, Aquarium a){
		String contenu [] = s.split("!");
		String tmp = contenu[0];
		
		switch (tmp){
		case "DECONNECT":
			//message  : CMD
			a.disconnectClient();
			break;
		case "ADDFISH":
			//message  : CMD!idFish!width!height!x!y!StringClass
			a.addFish(new Mobiles(idClient, 
					Integer.parseInt(contenu[0]),
					Integer.parseInt(contenu[1]), 
					Integer.parseInt(contenu[2]), 
					Integer.parseInt(contenu[3]), 
					Integer.parseInt(contenu[4]), 
					a.getClasse(a.getClasseIndex(contenu[5])).getImages()));
			break;
		case "REMOVEFISH":
			//message  : CMD!idFish
			a.removeFish(idClient,contenu[1]);
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
		default: 
			System.out.println("Reception d'un message non conforme. ");
			System.out.println("non traité : "+s);
			break;
		}
	
		return true;
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