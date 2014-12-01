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
						System.out.println("Le client n° " + id	+ " est connecté. ");

						// premier contact

						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream());

						// envoi d'un message
						out.println(id);
						out.flush();

						// réception des classes du client
						Protocole1.receiveClasses(in, aqua, id,true);

						// réception des poissons du client
						Protocole1.receiveFishs(in, aqua, id,true);

						//envoi des classes
						Protocole1.sendMyClasses(out, aqua,0);

						//envoi des poissons
						Protocole1.sendMyFishs(out, aqua);


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
							}

							private void receive() throws IOException{
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
									socket.close();
								}
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
