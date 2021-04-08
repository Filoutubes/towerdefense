package warcraftTD;

public class Main {
	
	/*
	 * Le projet étant écrit sous Windows dans son intégralité, 
	 * il est possible que le symbole de séparation des dossiers
	 *  cause des problèmes pour l'exécution du projet sous d'autres
	 *  systèmes d'exploitation
	 *  Si vous avez des problèmes à propos de l'accès aux images,
	 *  il est possible qu'en modifiant l'attribut suivant vous arriviez à régler ce soucis
	 */
	public static final String symbole_separation_dossiers = "\\";
	
	/* 
	 * n'affiche pas certaines images pour que les configurations
	 *  les plus basiques puissent faire tourner le jeu convenablement
	 *   (l'avancement des monstres peut paraître saccadé sans ça)
	 */
	public static final boolean version_lite = false;
	
	
	public static void main(String[] args) {
		/* 
		 * la taille minimum est de 1, 
		 * et il vaut mieux sélectionner une valeur
		 *  comprise entre 1 et 1.5 pour une expérience de jeu optimale
		 */
		double taille = 1;
		
		/*
		 * vie de départ du joueur
		 */
		int life = 3;
		
		/*
		 *  utile seulement dans un cas particulier
		 *  ne pas y préter attention si vous voulez tester le jeu de manière classique
		 */
		int startX = 1; // coordonnée X du spawn
		int startY = 1; //coordonnée Y du spawn
		int endX = 8; // coordonnée X du point d'arrivée
		int endY =8; // coordonnée Y du point d'arrivée
		
		World w = new World(taille, startX, startY,endX,endY,life);
		// chargement d'images en avance pour diminuer le temps de chargement
		StdDraw.text(0.5, 0.7, World.general);
		StdDraw.picture(0.5, 0.4, World.plateau+"herbe"+World.choixTheme+".png");
		// Lancement de la boucle principale du jeu
	    w.run();
		
	}
}

