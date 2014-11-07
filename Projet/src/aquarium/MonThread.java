package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.Protocole1;

public class MonThread extends Thread {
	//pour l'aquarium
	private static Aquarium aqua;
	private static List<ElementClient> poissons;
	
	//gestion des clients
	private ServerSocket socketserver;
	private Socket socket;
	private static Set<Socket> clients;
	
	//entrées sorties 
	private PrintWriter out;
	private BufferedReader in;
	 
	//pour les tests
	private int nbClients;
	  
	public MonThread(ServerSocket s, Aquarium aqua){
		socketserver = s;
		this.aqua = aqua;
		clients = new HashSet<Socket>();
	}
	
	public void run() {

		try {
			poissons = new ArrayList<ElementClient>();
			poissons.add(new ElementClient("localhost"));
			
			
			while(true){
	       		socket = socketserver.accept(); // Un client se connecte on l'accepte
	       		clients.add(socket);
	       		System.out.println("Le client n° "+nbClients+" est connecté. ");
	            nbClients++;
	              
	     
	            
	            //premier contact
	            
	            //envoi d'un message
	            out = new PrintWriter(socket.getOutputStream());
	            out.println(" vous êtes bien dans l'aquarium de "+socket.getLocalAddress()+" au numéro "+nbClients);
	            out.flush();
	            
	            //réception des classes du client
	            
	            //réception des poissons du client
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String tampon = in.readLine();
	            
	            //int categorieMessage = decoder(tampon);
	            //System.out.println(tampon+decoder(tampon)); // TEMPORAIRE
	            boolean reussite = traiterReception(tampon);
	            //TMP traitement d'erreur à revoir
	            if(!reussite){
	            	System.out.println("problème de réception");
	            }
	            
	            
	            //traitement dans le temps après le premier contact
	            do{
	            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            tampon = in.readLine();
	            	traiterReception(tampon);
	            	
	            	
	            	
	            }while(decoder(tampon) != 0)
	            
	            socket.close();
	               
	       	}
	       	
	      } catch (IOException e) {
	    	   e.printStackTrace();  
	      }
			
	}
	
	//traiter les cas de contenu du message
	boolean traiterReception(String s){
		
		return true;
	}
	
	
	
	
	
	/*Socket so;
	Set<Socket> set;
	Aquarium aqua;
	
	
	public MonThread(Socket s,Set t, Aquarium a) throws IOException{
		so = s;
		set = t;
		aqua = a;
	}
	
	public void run(){
		try {
			System.out.println("thread créé");
			byte inputTab[] = new byte [1024];
			byte outputTab[] = new byte [1024];
			
			String welcomeMessage = "Bonjour Client !";
			//réception poissons du nouveau client
			//envoyer les poissons supplémentaire
			

			OutputStream os = so.getOutputStream();
			os.write(welcomeMessage.getBytes(Charset.forName("UTF-8")));

			while(true){
				InputStream is = so.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while(true){
					String str = br.readLine();
					System.out.println(str);
				}
			}
			//so.close();
			
		} catch (IOException e){
				e.printStackTrace();
		}
	}*/
		
}
