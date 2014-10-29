package aquarium;

import java.awt.Image;
import java.awt.Point;
import java.io.UnsupportedEncodingException;

public class Protocole1 {
	//String Class nom, int, int, position Point(int int), Image,
	//String = variable
	//int = 4 byte
	//Image ... je sais pas =='
	
	/*public static byte[] encoder (StableFish f){
	//int
	}*/
	
	//je vois pas comment transferer l'image
	public static String encoderPoisson1Class (Image i, int width, int height, String nom, Point p){
		return "";
	}
	
	//poisson correspond à la position dans le tableau du client
	public static String encoderPosition (int poisson, int x, int y){
		return poisson+"!"+x+"!"+y;
	}
	
	public static String encoderPoissonS (int width, int height, String nom, int x, int y){
		return width+"!"+height+"!"+x+"!"+y+"!"+nom;
	}
	
	public static byte[] encoderPoissonB (int width, int height, String nom, int x, int y){
		String s = width+"!"+height+"!"+x+"!"+y+"!"+nom;
		return s.getBytes();
	}

	public static void decoderPoissonB(byte s[]) throws UnsupportedEncodingException{
		String tmp1 = new String(s, "UTF-8");
		String tmp[] = tmp1.split("!");
		int i = 0;
		while(i < tmp.length){
			System.out.println(tmp[i]);
			i++;
		}
	}
	
	public static void decoderPoissonS(String s){
		String tmp[] = s.split("!");
		int i = 0;
		while(i < tmp.length){
			System.out.println(tmp[i]);
			i++;
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException{
		int a = 4;
		int b = 5;
		int c = 6;
		int d = 7;
		String s = "DorisFish";
		byte tmp[] = encoderPoissonB(a,b,s,c,d);
		decoderPoissonB (tmp);
	}

}
