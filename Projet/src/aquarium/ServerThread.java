package aquarium;

import java.awt.Image;
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
import aquarium.gui.AquariumWindow;
import aquarium.items.AquariumItem;
import aquarium.Protocole1;

public class ServerThread extends Thread {	
	private Object o = new Object();
	private Aquarium aqua;
	
	//gestion des clients
	private ServerSocket socketserver;
	private Socket socket;
	private static Set<Socket> clients;
	
	//entrées sorties 
	private PrintWriter out;
	private BufferedReader in;
	 
	//pour les tests
	private int nbClients;
	  
	public ServerThread(ServerSocket s){
		aqua = new Aquarium();
		socketserver = s;
		clients = new HashSet<Socket>();
	}
	
	public void run() {

		try {
			AquariumWindow animation = new AquariumWindow(aqua);
			animation.displayOnscreen();
			
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
	            
	            

	            //traitement dans le temps après le premier contact
	            String tampon;

	            //réception des classes du client
            	//réception des poissons du client
	            
	            do{		            
	            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            tampon = in.readLine();
		            synchronized(o){
		            	traiterReception(tampon);
		            }
	            	
	            	
	            }while(Protocole1.decode(tampon) != 0);
	            
	            socket.close();
	               
	       	}
	       	
	      } catch (IOException e) {
	    	   e.printStackTrace();  
	      }
			
	}
	
	/*code : 
	0 transfert ?
	1 transfert classe
	2 transfert poisson
	3 transfert ? kill ?
	4 transfert position poisson 
	5 transfert ?
	 */
	
	//traiter les cas de contenu du message
	boolean traiterReception(String s){
		if(Protocole1.decode(s) == 0){
			//a definir
		}else if(Protocole1.decode(s) == 1){
			
		}else if(Protocole1.decode(s) == 2){
			
		}else if(Protocole1.decode(s) == 3){
			
		}else if(Protocole1.decode(s) == 4){
			
		}else if(Protocole1.decode(s) == 5){
			//a definir
		}
		return true;
	}
	
	boolean traiterEnvoi(){
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
