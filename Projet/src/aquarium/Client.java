package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;
import aquarium.items.AquariumItem;
import aquarium.items.MobileItem;

public class Client extends Thread {
	//reseau - communication
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	//aquarium
	private Aquarium a ;
	
	public Client(){
		//vide car initialisation dans le run puisqu'on est dans le client => fait la demande
	}
	
	public void run() {
		try{
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			
			out = new PrintWriter(socket.getOutputStream());
			
			//MESSAGE DE BIENVENUE DANS L AQUARIUM
			// réception > InputStream > ISReader > BufferedReader
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
			
			//création de l'aquarium
			a = new Aquarium();
			AquariumWindow animation = new AquariumWindow(a);
			animation.displayOnscreen();
			
			//envoi des classes
			int total = a.nbOfClientClasses(0);
			out.println(total);
			out.flush();
			for(int i = 0;i < total;i++){
				out.println(a.getClass(i).getNom()); //envoi d'une classe, MODIF après gestion de l'image
				out.flush();
			}
			
			//envoi des poissons
			List<Integer> MobileItems =  a.getNbOfMobileItems();
			int taille = MobileItems.size();
			out.println(taille);
			out.flush();
			for(int j = 0;j<taille;j++){
				AquariumItem ai = a.getAquariumItem(MobileItems.get(j));
				String name = ((MobileItem) ai).getClasse();
				out.println(Protocole1.encodeFishString(j,ai.getWidth(),ai.getHeight() ,ai.getPosition().x, ai.getPosition().y,name));
				out.flush();
			}
			
			//reception des poissons des autres
			
			//comportement dans le temps			
			ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
			es.scheduleWithFixedDelay(new Runnable() {
				
				public void run() {
					//envoi des positions toutes les secondes
					List<Integer> MobileItems =  a.getNbOfMobileItems();
					int taille = MobileItems.size();
					out.println(taille);
					out.flush();
					for(int j = 0;j<taille;j++){
						AquariumItem ai = a.getAquariumItem(MobileItems.get(j));
						out.println(j+"!"+ai.getPosition().x+"!"+ai.getPosition().y);
						out.flush();
					}
					
				}
			}, 0, 1, TimeUnit.SECONDS);
			
			socket.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}

}
