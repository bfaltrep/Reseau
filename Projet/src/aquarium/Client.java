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
	private int port;
	private BufferedReader in;
	private PrintWriter out;
	//aquarium
	private Aquarium a ;
	private long identifiant;

	public Client(int p){
		port = p;
		//vide car initialisation dans le run puisqu'on est dans le client => fait la demande
	}

	public void run() {
		try{

			//mise en contact
			socket = new Socket (InetAddress.getLocalHost(),port);
			System.out.println("demande de connexion ");
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


			final ScheduledExecutorService myservice = Executors.newScheduledThreadPool(3);
			myservice.execute(new Runnable() {

				void firstContact() throws Exception {
					//MESSAGE DE BIENVENUE DANS L AQUARIUM
					// réception > InputStream > ISReader > BufferedReader
					String message = in.readLine();
					identifiant = Long.parseLong(message);
					
					//création de l'aquarium
					a = new Aquarium(identifiant);
					AquariumWindow animation = new AquariumWindow(a);
					animation.displayOnscreen();
					
					//envoi des classes
					Protocole1.sendMyClasses(out, a,identifiant);

					//envoi des poissons
					Protocole1.sendMyFishs(out, a);

					//reception des classes des autres
					Protocole1.receiveClasses(in,a, 0,false);
					
					//reception des poissons des autres
					Protocole1.receiveFishs( in, a, 0,false);

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
							//envoyer positions
							Protocole1.sendMyPositions(out,a,identifiant);
							
							
							
							//envoyer  ajout / suppression
							
						}

						private void receive() throws IOException{
							//recevoir positions
							
							Protocole1.receivePositions( in, a, identifiant, false);
							//recevoir ajout / suppression
							
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
					}, 0, 10, TimeUnit.MILLISECONDS);

				}
			});
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}

}
