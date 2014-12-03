package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;

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
		aqua = new Aquarium(0);
		clients = new ArrayList<Socket>();
	}

	public void run() {

		AquariumWindow animation = new AquariumWindow(aqua);
		animation.displayOnscreen();
		try{
			final ScheduledExecutorService myservice = Executors.newScheduledThreadPool(10);
			while (!socketserver.isClosed()) {
				final Socket socket = socketserver.accept(); 
				clients.add(socket);

				myservice.execute(new Runnable() {

					void firstContact() throws Exception {
						long id = Thread.currentThread().getId();
						System.out.println(clients);
						System.out.println("Le client n° " + id	+ " est connecté.");

						// premier contact

						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream());

						// envoi d'un message
						out.println(id);
						out.flush();

						// réception des classes du client
						Protocole1.receptionInit(in, aqua, id,true);

						// réception des poissons du client
						Protocole1.receptionInit(in, aqua, id,true);

						//envoi des classes
						Protocole1.sendMyClasses(out, aqua,0);

						//envoi des poissons
						Protocole1.sendMyFishs(out, aqua);
						Protocole1.sendOthersFishes(out, aqua, id);

					}
					
					public void run() {
						try {
							firstContact();
						} catch (Exception e) {
							e.printStackTrace();
						}

						// comportement dans le temps
						myservice.scheduleWithFixedDelay(new Runnable() {

							private void send() {
								long id = Thread.currentThread().getId();
								Protocole1.sendMyPositions(out,  aqua, id);
								Protocole1.sendPositionsOthers( out, aqua, id);

								//envoyer les modification de l'aquarium : position / ajout / suppression
							}

							private void receive() throws IOException{
								long id = Thread.currentThread().getId();

								try {									
									Protocole1.receivePositions(in, aqua, id,true);
									//recevoir ajout suppression
									
								} catch (Exception e) {
									// client s'est deconnecte
									socket.close();
									myservice.shutdown();
									e.printStackTrace();
								}
							}

							public void run() {
								// réception des positions toutes les secondes
								try {
									System.out.println("run boucle serveur "+Thread.currentThread().getId());
									receive();
									send();
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
						}, 0, 100, TimeUnit.MILLISECONDS);

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

		}finally{
		}
	}
}
