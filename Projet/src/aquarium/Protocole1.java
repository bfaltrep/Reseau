package aquarium;

import java.io.UnsupportedEncodingException;

import aquarium.util.Constante;

public class Protocole1 {
	// String Class nom, int, int, position Point(int int), Image,
	// String = variable
	// int = 4 byte
	// Image ... je sais pas =='

	// je vois pas comment transferer l'image

	/*
	 * 0: Déconnexion
	 * 1: Connexion
	 * 2: Créer poisson
	 * 3: Tuer poisson
	 * 4: Bouger poisson
	 * 5: Nouvelle classe
	 */

	
	//TODO: Se référer à Client.java pour savoir comment récupérer les variables de l'aquarium
	public static byte[] encodeFishByte(int type, Aquarium aquarium) {
		String result;
		switch(type) {
			case 0:
				result = type + "!";
				return result.getBytes();
			case 1:
				result = type + "!";
				return result.getBytes();
			case 2:
				result = type + "!" + fishId + "!" + width + "!" + height + "!" + x + "!" + y;
				return result.getBytes();
			case 3:
				result = type + "!" + fishId;
				return result.getBytes();
			case 4:
				result = type + "!" + fishId + "!" + x + "!" + y;
				return result.getBytes();
			case 5:
				result = type + "!";
				return result.getBytes();
			default:
				return "".getBytes();
		}
	}
	// !! \\
	public static String encodeFishString(int id, int width, int height, int x,
			int y, String name) {
		return id + "!" + width + "!" + height + "!" + x + "!" + y + "!" + name;
	}

	/**
	 * @param input
	 * @throws UnsupportedEncodingException
	 */
	public static void decodeFishByte(byte input[])
			throws UnsupportedEncodingException {
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
		System.out.println(width + height + xPos + yPos + fishName); // retirer
																		// les
																		// warnings

		// byte tmp[] = encodeFishByte(width, height, fishName, xPos, yPos);
		// decodeFishByte(tmp);
	}
}
