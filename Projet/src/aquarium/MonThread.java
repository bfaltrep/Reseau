package aquarium;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import aquarium.gui.Aquarium;
import aquarium.items.AquariumItem;
import aquarium.items.StableFish;

public class MonThread extends Thread {
	Socket so;
	Set set;
	Aquarium aqua;
	
	
	public MonThread(Socket s,Set t, Aquarium a){
		so = s;
		set = t;
		aqua = a;
	}
	
	public void run(){
		try {
			Iterator it = set.iterator();
			
			//Récupère les flux d'entrée ...
			
			InputStream is = so.getInputStream();
					
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
/*
			while(true){
				String str = br.readLine();
				
				while(it.hasNext()){
					
					so = (Socket)it.next();
					
					
					
					// ... et de sortie.
					OutputStream os =so.getOutputStream();

					
					//
					//os.write();
					
					PrintStream ps = new PrintStream(os, false, "utf-8");
					
					//récupérer l'image du premier poisson de la liste.
					int i = 0;
					AquariumItem ai = aqua.getAquariumItem(i);
					while(!(ai instanceof StableFish)){
						ai = aqua.getAquariumItem(i);
						i++;
					}
					FileInputStream fis = new FileInputStream(((StableFish) ai).getImage());
					
					
					
					
					ps.flush();
					
				}
			}*/
			so.close();
			
		} catch (IOException e){
				e.printStackTrace();
			}
	}
}
