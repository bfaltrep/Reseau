package aquarium;

import java.awt.Image;
import java.awt.Point;
import java.io.UnsupportedEncodingException;

public class Protocole1 {
	//String Class nom, int, int, position Point(int int), Image,
	//String = variable
	//int = 4 byte
	//Image ... je sais pas =='

	//je vois pas comment transferer l'image

	public static byte[] encodeFishByte(int type, intint width, int height, String name, int x, int y) {
			if (type == 0)
			{

			}
			else if (type == 1)
			{

			}
			else if (type == 2)
			{

			}
			else if (type == 3)
			{
				
			}
			else if (type == 4)
			{
				
			}
		String result = width + "!" + height + "!" + x + "!" + y + "!" + name;
		return result.getBytes();
	}
	
	public static String encodeFishString(int width, int height, String name, int x, int y) {
		return width + "!" + height + "!" + x + "!" + y + "!" + name;
	}

	public static void decodeFishByte(byte input[]) throws UnsupportedEncodingException {
		String rawInput = new String(input, "UTF-8");
		String processedInput[] = rawInput.split("!");
		int i = 0;
		
		while (i < processedInput.length) {
			System.out.println(processedInput[i]);
			i++;
		}
	}

	public static void decodeFishString(String input) {
		String processedInput[] = input.split("!");
		int i = 0;
		
		while (i < processedInput.length) {
			System.out.println(processedInput[i]);
			i++;
		}
	}

	public static int decode(String input) {
		return 0;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		int width = 4;
		int height = 5;
		int xPos = 6;
		int yPos = 7;
		String fishName = "DorisFish";
		
		//byte tmp[] = encodeFishByte(width, height, fishName, xPos, yPos);

		
		decodeFishByte(tmp);
	}
}
