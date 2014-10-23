package aquarium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

public class MonThread extends Thread {
	Socket so;
	Set set;
	
	public MonThread(Socket s,Set t){
		so = s;
		set = t;
	}
	
	public void run(){
		try {
			Iterator it = set.iterator();
			
			//Récupère les flux d'entrée ...
			InputStream is = so.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

			while(true){
				String str = br.readLine();
				if(str == null){
					System.out.println(so.getLocalAddress().getHostName() +" has left");
					
					break;
				}else{
					System.out.println("> "+str);
				}
								
				while(it.hasNext()){
					
					so = (Socket)it.next();
					// ... et de sortie.
					OutputStream os =so.getOutputStream();
					PrintStream ps = new PrintStream(os, false, "utf-8");
					
					ps.println(str);
					ps.flush();
					
				}
			}
			so.close();
			
		} catch (IOException e){
				e.printStackTrace();
			}
	}
}
