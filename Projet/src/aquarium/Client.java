package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		Socket socket;
		BufferedReader in;
		PrintWriter out;
		try{
			socket = new Socket (InetAddress.getLocalHost(),8888);
			System.out.println("demande de connexion ");
			// réception > InputStream > ISReader > BufferedReader
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
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

}
