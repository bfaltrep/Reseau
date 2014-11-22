package aquarium;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;
import aquarium.items.AquariumItem;
import aquarium.items.Mobiles;
import aquarium.Protocole1;

public class ServerThread extends Thread {	
	private Object o = new Object();
	private Aquarium aqua;
	
	//gestion des clients
	private ServerSocket socketserver;
	private Socket socket;
	private static Set<Socket> clients;
	private int nbClient;
	
	//entrées sorties 
	private PrintWriter out;
	private BufferedReader in;
	  
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
	       		System.out.println(clients);
	       		nbClient = clients.size();
	       		System.out.println("Le client n° "+nbClient+" est connecté. ");

	            //premier contact
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out = new PrintWriter(socket.getOutputStream());
	            
	            //envoi d'un message
	            out.println(" vous êtes bien dans l'aquarium de "+socket.getLocalAddress()+" au numéro "+nbClient);
	            out.flush();
	            
	            
	            String tampon;

	            //réception des classes du client
            	tampon = in.readLine();
            	int nbr = Integer.parseInt(tampon);

            	for(int i = 1; i<=nbr;i++){
            		tampon = in.readLine();
            		aqua.addClasses(nbClient,tampon, "image/polochon.jpg");
            	}
            	
            	
            	//réception des poissons du client
            	tampon = in.readLine();
            	nbr = Integer.parseInt(tampon);
            	for(int i = 0; i<nbr;i++){
            		tampon = in.readLine();
            		String[] contenu = tampon.split("!");
            		//tmp gestion image/nom
            		aqua.addOther(new Mobiles(nbClient,
            				Integer.parseInt(contenu[0]),Integer.parseInt(contenu[1]),Integer.parseInt(contenu[2]),
            				Integer.parseInt(contenu[3]), Integer.parseInt(contenu[4]),aqua.getClasse(aqua.getClasseIndex(contenu[5])).getImages()));
            	}
  
            	
            	//envoi des poissons des autres
            	
            	
            	
    			//comportement dans le temps			
    			ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
    			es.scheduleWithFixedDelay(new Runnable() {
    				
    				public void run() {
    					//réception des positions toutes les secondes
    					String tampon;
						try {
							tampon = in.readLine();
	    					int nbr = Integer.parseInt(tampon);
	    					for(int j = 0;j<nbr;j++){
	    						tampon = in.readLine();
	    						String[] contenu = tampon.split("!");    						
	    						aqua.OtherModifPositionSimple(nbClient, Integer.parseInt(contenu[0]),Integer.parseInt(contenu[1]), Integer.parseInt(contenu[2]));
	    						//out.println(j+"!"+ai.getPosition().x+"!"+ai.getPosition().y);
	    					}								
						} catch (IOException e) {
							e.printStackTrace();
						}
    					
    				}
    			}, 0, 1, TimeUnit.SECONDS);

	            
	            //tant qu'il n'a pas le message qui indique que le client se déconnecte.
	            /*while(Protocole1.decode(tampon) != 0){
		            tampon = in.readLine();
		            synchronized(o){
		            	traiterReceptionPositions(tampon);
		            }
            
	            }*/
            	
            	
            	
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
	boolean traiterReceptionPositions(String s){
		
		return true;
	}
	
	boolean traiterEnvoi(){
		return true;
	}
		
}
