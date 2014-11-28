package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;
import aquarium.items.Mobiles;

public class ServerThread extends Thread {
	private Aquarium aqua;

	// gestion des clients
	private ServerSocket socketserver;
	private static List<Socket> clients;

	// entrées sorties
	private PrintWriter out;
	private BufferedReader in;

	public ServerThread(int p) throws IOException {
		socketserver = new ServerSocket(p);
		aqua = new Aquarium();
		clients = new ArrayList<Socket>();
	}

	public void run() {
		try {
			AquariumWindow animation = new AquariumWindow(aqua);
			animation.displayOnscreen();

			final ScheduledExecutorService myservice = Executors.newScheduledThreadPool(10);
			while (!socketserver.isClosed()) {
				final Socket socket = socketserver.accept(); 
				clients.add(socket);
				
				myservice.execute(new Runnable() {

					void firstContact() {
						long id = Thread.currentThread().getId();
						System.out.println(clients);
						System.out.println("Le client n° " + id	+ " est connecté. ");

						// premier contact

						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						out = new PrintWriter(socket.getOutputStream());

						// envoi d'un message
						out.println(" vous êtes bien dans l'aquarium de " + socket.getLocalAddress() + " au numéro " + id);
						out.flush();

						String tampon;

						// réception des classes du client
						tampon = in.readLine();
						int i = Protocole1.decoder(tampon,id, aqua);
						if(i>0){
							for(int j = 0 ; j < i ; j++){
								tampon = in.readLine();
								int k = Protocole1.decoder(tampon,id, aqua);
							}
						}
						/*tampon = in.readLine();
						int nbr = Integer.parseInt(tampon);
						for (int i = 1; i <= nbr; i++) {
							tampon = in.readLine();
							aqua.addClasses(id, tampon,	"image/polochon.jpg");
						}*/

						// réception des poissons du client
						tampon = in.readLine();
						int nbr = Integer.parseInt(tampon);
						for (int i = 0; i < nbr; i++) {
							tampon = in.readLine();
							String[] contenu = tampon.split("!");
							// tmp gestion image/nom
							aqua.addObj(new Mobiles(id, Integer
									.parseInt(contenu[0]), Integer
									.parseInt(contenu[1]), Integer
									.parseInt(contenu[2]), Integer
									.parseInt(contenu[3]), Integer
									.parseInt(contenu[4]), aqua.getClass(
											aqua.getClasseIndex(contenu[5]))
											.getImages()));
						}
						// envoi des poissons des autres
					}

					@Override
					public void run() {

						long id = Thread.currentThread().getId();

						firstContact();

						// comportement dans le temps
						myservice.scheduleWithFixedDelay(new Runnable() {

							private void send() {
								//envoyer les modification de l'aquarium : position / ajout / suppression
							}

							private void receive(){
								long id = Thread.currentThread().getId();

								String tampon;
								try {
									tampon = in.readLine();
									int nbr = Integer.parseInt(tampon);
									for (int j = 0; j < nbr; j++) {
										tampon = in.readLine();
										String[] contenu = tampon.split("!");
										aqua.modifySingleClientObj(id,
												Integer.parseInt(contenu[0]),
												Integer.parseInt(contenu[1]),
												Integer.parseInt(contenu[2]));
										// out.println(j+"!"+ai.getPosition().x+"!"+ai.getPosition().y);
									}
								} catch (IOException e) {
									// client s'est deconnecte
									myservice.shutdown();
									e.printStackTrace();
								}
							}

							public void run() {
								// réception des positions toutes les secondes
								receive();
								send();
							}
						}, 0, 1, TimeUnit.SECONDS);

						// tant qu'il n'a pas le message qui indique que le
						// client se déconnecte.
						/*
						 * while(Protocole1.decode(tampon) != 0){ tampon =
						 * in.readLine(); synchronized(o){
						 * traiterReceptionPositions(tampon); }
						 * 
						 * }
						 */
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
			socket.close();
		}
	}
}
