package aquarium;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import aquarium.gui.AquariumWindow;
import aquarium.gui.Aquarium;

/**
 * Starting point of the Aquarium application
 */
public class Main  {
	
	public static void main(String[] args) throws IOException {
		System.out.println("tagazok");
		Aquarium aquarium = new Aquarium();
		AquariumWindow animation = new AquariumWindow(aquarium);
		animation.displayOnscreen();
		
		//gestion multi
		
		ServerSocket serveur = new ServerSocket();
		serveur.setReuseAddress(true);
		serveur.bind(new InetSocketAddress(8888));

		MonThread mt ;
		Set set = new HashSet<Socket>();
		while(true){
			Socket s = 	serveur.accept();
			synchronized(set){
				set.add(s);
			}
			
			mt = new MonThread(s,set);
			if(mt == null)
				break;
			mt.start();
		}
		serveur.close();

	}
}
