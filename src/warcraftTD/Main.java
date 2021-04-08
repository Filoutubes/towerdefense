package warcraftTD;

public class Main {
	
	/*
	 * Le projet �tant �crit sous Windows dans son int�gralit�, 
	 * il est possible que le symbole de s�paration des dossiers
	 *  cause des probl�mes pour l'ex�cution du projet sous d'autres
	 *  syst�mes d'exploitation
	 *  Si vous avez des probl�mes � propos de l'acc�s aux images,
	 *  il est possible qu'en modifiant l'attribut suivant vous arriviez � r�gler ce soucis
	 */
	public static final String symbole_separation_dossiers = "\\";
	
	/* 
	 * n'affiche pas certaines images pour que les configurations
	 *  les plus basiques puissent faire tourner le jeu convenablement
	 *   (l'avancement des monstres peut para�tre saccad� sans �a)
	 */
	public static final boolean version_lite = false;
	
	
	public static void main(String[] args) {
		/* 
		 * la taille minimum est de 1, 
		 * et il vaut mieux s�lectionner une valeur
		 *  comprise entre 1 et 1.5 pour une exp�rience de jeu optimale
		 */
		double taille = 1;
		
		/*
		 * vie de d�part du joueur
		 */
		int life = 3;
		
		/*
		 *  utile seulement dans un cas particulier
		 *  ne pas y pr�ter attention si vous voulez tester le jeu de mani�re classique
		 */
		int startX = 1; // coordonn�e X du spawn
		int startY = 1; //coordonn�e Y du spawn
		int endX = 8; // coordonn�e X du point d'arriv�e
		int endY =8; // coordonn�e Y du point d'arriv�e
		
		World w = new World(taille, startX, startY,endX,endY,life);
		// chargement d'images en avance pour diminuer le temps de chargement
		StdDraw.text(0.5, 0.7, World.general);
		StdDraw.picture(0.5, 0.4, World.plateau+"herbe"+World.choixTheme+".png");
		// Lancement de la boucle principale du jeu
	    w.run();
		
	}
}

