package aquarium;

import java.io.IOException;
import java.net.ServerSocket;

//import aquarium.gui.AquariumWindow;
//import aquarium.gui.Aquarium;

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
		ServerSocket serveur;

		try{
		serveur = new ServerSocket(8888);
		Thread t = new Thread(new MonThread(serveur));
		t.start();
		}catch(IOException e){
		e.printStackTrace();
		}
		
		/*
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
		}*/
	}
}
