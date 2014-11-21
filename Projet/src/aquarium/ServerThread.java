package aquarium;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;
import aquarium.items.Mobiles;
import aquarium.Protocole1;

public class ServerThread extends Thread {	
	//private Object o = new Object();
	private Aquarium aqua;
	
	//gestion des clients
	private ServerSocket socketserver;
	private Socket socket;
	private static Set<Socket> clients;
	private int nbClients;
	
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
	       		System.out.println("Le client n° "+nbClients+" est connecté. ");
	            nbClients++;

	            //premier contact
	            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            out = new PrintWriter(socket.getOutputStream());
	            
	            //envoi d'un message
	            out.println(" vous êtes bien dans l'aquarium de "+socket.getLocalAddress()+" au numéro "+nbClients);
	            out.flush();
	            
	            
	            String tampon;
	            
	            //FONCTIONNE PAS
	            
	            //réception des classes du client
            	tampon = in.readLine();
            	int nbr = Integer.parseInt(tampon);

            	for(int i = 1; i<=nbr;i++){
            		tampon = in.readLine();
            		aqua.addClasses(nbClients,tampon, "image/polochon.jpg");
            	}
            	
            	
            	//réception des poissons du client
            	tampon = in.readLine();
            	nbr = Integer.parseInt(tampon);
            	System.out.println("nb poissons recus : "+nbr);
            	for(int i = 0; i<nbr;i++){
            		tampon = in.readLine();
            		System.out.println("poissons : "+tampon);
            		String[] contenu = tampon.split("!");
            		aqua.addOther(new Mobiles(nbClients,Integer.parseInt(contenu[0]),Integer.parseInt(contenu[1]),Integer.parseInt(contenu[2]),Integer.parseInt(contenu[3]), Integer.parseInt(contenu[4]),contenu[5]));
            	}
  
            	
            	
            	
            	
            	

	            /*
	            //traitement dans le temps après le premier contact
	            do{
		            tampon = in.readLine();
		            synchronized(o){
		            	traiterReception(tampon);
		            }
	            	
	            	
	            }while(Protocole1.decode(tampon) != 0);
	            */
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
