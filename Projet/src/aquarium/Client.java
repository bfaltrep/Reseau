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
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			
			out = new PrintWriter(socket.getOutputStream());
			
			//MESSAGE DE BIENVENUE DANS L AQUARIUM
			// réception > InputStream > ISReader > BufferedReader
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
			
			//création de l'aquarium
			a = new Aquarium();
			AquariumWindow animation = new AquariumWindow(a);
			animation.displayOnscreen();
			
			//envoi des classes
			int i= 0;
			int total = a.ClassesNbPourClient(0);
			out.print(total+"!");
			while(i < total){
				out.println(a.getClasse(i).getNom()); //envoi d'une classe, MODIF après gestion de l'image
				out.flush();
				i++;
			}
			
			//envoi des poissons
			List<String> mobils =  a.StringMobileItems();
			out.print(mobils.size()+"!");
			for(int j = 0;i<mobils.size();i++){
				out.println(mobils.get(j)); //envoi d'une classe, MODIF après gestion de l'image
				out.flush();
			}
			
			
			//reception des poissons des autres
			
			
			
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
