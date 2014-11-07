package aquarium;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;




import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashSet;

import java.util.Set;

//import aquarium.gui.AquariumWindow;
import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;

/**
 * Starting point of the Aquarium application
 */
public class Main  {
	/**
	 * 
	 * @param args : ip et port => si ip est localhost : est le serveur, ouvert au port port. Ouvre un client sur soi-même
	 * 							=> si ip est autre : est un client et indique à quelle ip se connecter, via quel port
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		ServerSocket serveur;
		
				
		try{
			
		serveur = new ServerSocket(8888);
		Aquarium aqua = new Aquarium(serveur.getInetAddress());
		Thread t = new MonThread(serveur, aqua);
		t.start();
		
		}catch(IOException e){
		e.printStackTrace();
		}
		*/
		
		System.out.println(args[0]);
		if("serveur".matches(args[0])){
			System.out.println("Je suis le serveur");

			//creer aquarium
			Aquarium aquarium = new Aquarium();
			AquariumWindow animation = new AquariumWindow(aquarium);
			animation.displayOnscreen();
			
			
			ServerSocket serveur = new ServerSocket();
			
			//gestion du port
			serveur.setReuseAddress(true);
			serveur.bind(new InetSocketAddress(Integer.parseInt(args[1])));
			
			
			
			MonThread mt ;
			while(true){
				
				mt = new MonThread(serveur,aquarium);
				if(mt == null)
					break;
				mt.start();
			}
			serveur.close();
			
			//quand nouveau client, lui alloue une couleur et la transmet aux autres
			
			
		}else{			
			//client
			System.out.println("Je suis un client");

			//1 génère l'aquarium et son contenu local
			Aquarium aquarium = new Aquarium();
			AquariumWindow animation = new AquariumWindow(aquarium);
			animation.displayOnscreen();
			
			
			//   /\ attention au host
			Socket s = new Socket(args[1],Integer.parseInt(args[2]));

			byte inputTab[] = new byte [1024];
			byte outputTab[] = new byte [1024];
			
			while(true){
				
				//recoit le message
				InputStream is = s.getInputStream();
				if(is == null){
					System.out.println("pas de message ...");
					break;
				}else{
					System.out.println("> "+is);
				}
				
				//ecriture
				OutputStream os = s.getOutputStream();
				
				String welcomeMessage = "Bonjour Serveur !";				

				os.write(welcomeMessage.getBytes(Charset.forName("UTF-8")));
			}
		}
	}
}
