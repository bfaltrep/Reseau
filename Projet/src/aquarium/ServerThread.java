package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;

public class ServerThread extends Thread {
	private Object o;
	private Aquarium aqua;

	// gestion des clients
	private ServerSocket socketserver;
	private static Hashtable<Socket, Integer> clients;
	private static int valeur ;
	
	// entrées sorties
	private PrintWriter out;
	private BufferedReader in;

	public ServerThread(int p) throws IOException {
		socketserver = new ServerSocket(p);
		aqua = new Aquarium(0);
		clients = new Hashtable<Socket,Integer>();
		valeur = 1;
		o = new Object();
	}

	public void run() {

		AquariumWindow animation = new AquariumWindow(aqua);
		animation.displayOnscreen();
		try{
			final ScheduledExecutorService myservice = Executors.newScheduledThreadPool(10);
			while (!socketserver.isClosed()) {
				final Socket socket = socketserver.accept(); 
				synchronized(o){
					clients.put(socket,valeur);
				}
				valeur++;
				
				myservice.execute(new Runnable() {

					void firstContact(int current) throws Exception {
						System.out.println("Le client n° " + current + " est connecté.");

						// premier contact

						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream());

						// envoi d'un message
						out.println(current);
						out.flush();

						// réception des classes du client
						Protocole1.receptionInit(in, aqua, current,true);

						// réception des poissons du client
						Protocole1.receptionInit(in, aqua, current,true);

						//envoi des classes
						Protocole1.sendMyClasses(out, aqua,0);

						//envoi des poissons
						Protocole1.sendMyFishs(out, aqua,0);
						Protocole1.sendOthersFishes(out, aqua, current);

					}
					
					public void run() {
						try {
							int current;
							synchronized(o){
								current = clients.get(socket);
							}
							firstContact(current);
						} catch (Exception e) {
							e.printStackTrace();
						}

						// comportement dans le temps
						myservice.scheduleWithFixedDelay(new Runnable() {

							private void send(int id) {
								Protocole1.sendMyPositions(out,  aqua,id);
								Protocole1.sendPositionsOthers( out, aqua, id);

								//envoyer les modification de l'aquarium : position / ajout / suppression
							}

							private void receive(int id) throws IOException{
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
								int current;
								synchronized(o){
									current = clients.get(socket);
								}
								// réception des positions toutes les secondes
								try {
									receive(current);
									send(current);
								} catch (IOException e) {
									e.printStackTrace();
									try {
										socket.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}, 0, 1, TimeUnit.SECONDS);

					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();

		}finally{
		}
	}
}
