package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import aquarium.gui.AquariumWindow;
import aquarium.gui.Aquarium;

/**
 * Starting point of the Aquarium application
 */
public class Main  {
	/**
	 * 
	 * @param args si args[0] = "serveur" et port => est le serveur, ouvert au port port
	 * 							"client" et une chaine de caractère représentant l'ip d'une personne et le port 
	 * 							=> indique à quel serveur on souhaite se connecter, a quel port
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
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
			//ensemble des clients
			Set set = new HashSet<Socket>();
			
			while(true){
				Socket s = 	serveur.accept();
				synchronized(set){
					set.add(s);
				}
				
				mt = new MonThread(s,set, aquarium);
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
