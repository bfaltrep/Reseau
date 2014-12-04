package aquarium;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Starting point of the Aquarium application
 */
public class Main  {
	
	/**
	 * 
	 * @param args : ip et port => si ip est localhost : est le serveur, ouvert au port port. Ouvre un client sur soi-même
	 * 							=> si ip est autre : est un client et indique à quelle ip se connecter, via quel port
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException  {
		try{
			if(args.length  == 2){
				if(args[0].matches("localhost")){ // || args[0].matches(InetAddress.getLocalHost().toString())){
					//serveur
					System.out.println("Vous êtes un serveur "+InetAddress.getLocalHost()+" "+args[1]);
	
					Thread t = new ServerThread(Integer.parseInt(args[1]));
					t.start();
	
				}else{
					//client
					System.out.println("vous êtes un client "+args[0]+" "+args[1]);
					
					Client c = new Client(Integer.parseInt(args[1]));
					c.start();
				}
			}else{
				System.out.println("2 arguments expected : Address IP and Port.");
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
