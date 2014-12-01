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
			//création de l'aquarium
			a = new Aquarium();
			AquariumWindow animation = new AquariumWindow(a);
			animation.displayOnscreen();
			
			//mise en contact
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			final ScheduledExecutorService myservice = Executors.newScheduledThreadPool(3);
			myservice.execute(new Runnable() {

				void firstContact() throws Exception {
					
					//MESSAGE DE BIENVENUE DANS L AQUARIUM
					// réception > InputStream > ISReader > BufferedReader
					String message = in.readLine();
					System.out.println(message);
					
					//envoi des classes
					Protocole1.sendMyClasses(out, a);
					
					//envoi des poissons
					Protocole1.sendMyFishs(out, a);
					
					//reception des classes des autres
					Protocole1.receiveClasses(in,a, 0);
					
					//reception des poissons des autres
					Protocole1.receiveFishs( in, a, 0);
					
				}
				
				@Override
				public void run() {
					try {
						firstContact();
					} catch (Exception e) {
						e.printStackTrace();
					}
				

					// comportement dans le temps
					myservice.scheduleWithFixedDelay(new Runnable() {

						private void send() {
							//envoyer les modification de l'aquarium : position / ajout / suppression
							
							//envoi des positions toutes les secondes
							List<Integer> MobileItems =  a.getMobileItems();
							int taille = MobileItems.size();
							out.println(taille);
							out.flush();
							for(int j = 0;j<taille;j++){
								AquariumItem ai = a.getAquariumItem(MobileItems.get(j));
								out.println(j+"!"+ai.getPosition().x+"!"+ai.getPosition().y);
								out.flush();
							}
						}

						private void receive() throws IOException{
							
						}

						public void run() {
							// réception des positions toutes les secondes
							try {
								receive();
							} catch (IOException e) {
								e.printStackTrace();
								try {
									socket.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							send();
						}
					}, 0, 1, TimeUnit.SECONDS);
				
				}
			});
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}

}
