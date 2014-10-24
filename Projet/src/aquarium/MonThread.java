package aquarium;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.items.StableFish;

public class MonThread extends Thread {
	Socket so;
	Set set;
	Aquarium aqua;
	
	
	public MonThread(Socket s,Set t, Aquarium a) throws IOException{
		so = s;
		set = t;
		aqua = a;
	}
	
	public void run(){
		try {
			byte inputTab[] = new byte [1024];
			byte outputTab[] = new byte [1024];
			
			String welcomeMessage = "Bonjour Client !";
			//réception poissons du nouveau client
			//envoyer les poissons supplémentaire
			

			OutputStream os = so.getOutputStream();
			os.write(welcomeMessage.getBytes(Charset.forName("UTF-8")));


			while(true){
				InputStream is = so.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while(true){
					String str = br.readLine();
					System.out.println(str);
				}
			}
			//so.close();
			
		} catch (IOException e){
				e.printStackTrace();
		}
	}
		
}
