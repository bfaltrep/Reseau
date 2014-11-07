package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import aquarium.gui.Aquarium;
import aquarium.gui.AquariumWindow;
import aquarium.items.AquariumItem;

public class Client extends Thread {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Aquarium a ;
	private List<AquariumItem> others;
	
	public Client(){
		//vide car initialisation dans le run
	}
	
	public void run() {
		try{
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			
			//MESSAGE DE BIENVENUE DANS L AQUARIUM
			// réception > InputStream > ISReader > BufferedReader
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
			
			a = new Aquarium();
			AquariumWindow animation = new AquariumWindow(a);
			animation.displayOnscreen();

			
			//envoi d'un message
			out = new PrintWriter(socket.getOutputStream());
			out.println(" bonjour, merci de me transmettre l'aquarium");
			out.flush();
			
			socket.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}
	
	/*
	public static void main(String[] args) {
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		Aquarium a ;
		
		
		try{
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			
			//MESSAGE DE BIENVENUE DANS L AQUARIUM
			// réception > InputStream > ISReader > BufferedReader
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
			
			a = new Aquarium();
			AquariumWindow animation = new AquariumWindow(a);
			animation.displayOnscreen();

			
			//envoi d'un message
			out = new PrintWriter(socket.getOutputStream());
			out.println(" bonjour, merci de me transmettre l'aquarium");
			out.flush();
			
			socket.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}*/

}
