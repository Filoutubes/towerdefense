
package warcraftTD;

import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;

public class World {

	// gestion des arborescences à parcourir pour sélectionner les images
	public static final String s = Main.symbole_separation_dossiers;
	// concerne l'export du projet sous forme de .jar
	public static String jarPath = "\\castleprotection\\images\\";
	public static final String accesImages = "src"+s+"images"+s;
	public static final String general = /*jarPath*/  accesImages;
	public static final String logos = general+"logos"+s;
	public static final String plateau = general + "plateau"+s;
	public static String choixTheme = "classique";// classique // plage //hiver
	
	



	// l'ensemble des monstres présents sur le plateau
	List<Monster> monsters = new ArrayList<Monster>();
	// l'ensemble des monstres morts pour gérer leur animation
	List<Monster> deadMonsters = new ArrayList<Monster>();
	// ensemble des vagues qui auront lieu
	List<Vague>vagues;

	// L'ensemble des tours pour gérer leurs positions sur le plateau

	//Map<Integer,TourGrillee> tours = new TreeMap<Integer,TourGrillee>();
	List<TourGrillee> tours = new ArrayList<TourGrillee>();

	// Position par laquelle les monstres vont venir
	Position spawn;

	//Position vers laquelle les monstres doivent arriver
	Position arrival;

	/* 
	 * Informations sur les mensurations du plateau de jeu : 
	 */
	private final int width;
	private final int height;
	private final int nbSquareX;
	private final int nbSquareY;
	private final double squareWidth;
	private final double squareHeight;
	//les autres classes n'ont pas le droit de modifier taille mais doivent pouvoir y avoir accès
	private  static double taille;
	public static final double getTaille() {
		return taille;
	}

	// Nombre de points de vie du joueur
	final int vieDeDepart;
	int life;
	private int cheatLife;

	// Nombre de pièces du joueur
	final int argentDeDepart;
	private int money;
	private  int cheatMoney;
	// Commande sur laquelle le joueur appuie (sur le clavier)
	char key;
	// Position sur laquelle le joueur a cliqué
	Position clicCourant;
	// Position de la souris du joueur
	Position sourisCourante;

	// Condition pour terminer la partie
	boolean end;
	// condition pour commencer la partie
	boolean commence;

	// indique que l'on a modifié les paramètres de la partie
	boolean presenceCheatCode = false;

	// Ensemble de cases adjacentes partant du spawn pour aller à l'objectif
	Chemin parcours;

	// Chrono du temps de partie
	Chrono tempsPartie;
	// Chrono contenant les infos liées à l'ajout de nouvelles tours
	Chrono infosTours;
	// Chrono contenant les infos liées au changement de la valeur money
	Chrono infosMoney;
	// Chrono contentant les infos liés aux vagues
	Chrono orgaVagues;
	
	 int compteur_drawFinalWave; 



	// renvoie true si la somme peut être payee et modifie ainsi la monnaie du joueur
	// si c'est impossible, le prélèvement n'est pas effectué
	public boolean payer(int somme) {
		// on ne paye pas si la partie n'a pas commencé
		if(!infosMoney.hasStarted())return false;
		if(somme<=money && somme>=0) {
			money= money - somme;
			//System.out.println("monnaie restante :"+money);
			infosMoney.setColor(StdDraw.RED);
			infosMoney.setInfos( "-"+somme+"pièces");
			return true;
		}
		return false;
	}

	/**
	 * le type CelluleActive permet d'avoir une vision des éléments statiques importants pour le gameplay (chemin, tours) sur le plateau de jeu,
	 *  afin de se représenter le jeu comme une grille, 
	 *  mais qui  ne soit pas gourmand en énergie pour autant : aucune des cases inactives (herbe) n'est représentée 
	 *  l'intérêt de la cellule active est de pouvoir convertir une ou un ensemble de Positions en un objet, et de pouvoir convertir cet objet facilement
	 *  On a donc 3 manières de voir une CelluleActive :
	 *  - c'est une Position particulière, centrée par rapport à la grille de jeu
	 *  - c'est une cellule située dans la grille de jeu avec des coordonnées  équivalentes à celles renseignées par l'utilisateur lors de la création du monde
	 *  		( numeroCaseX=0 et numeroCaseY=0 => en bas à gauche) et (numeroCaseX=nbSquareX-1 et numeroCaseY= nbSquareX-1 =>en hautà droite) 
	 *  - c'est une cellule située dans la grille de jeu, avec un nombre correspondant à une coordonnée. 
	 *  	Ce nombre est situé entre 0 (en haut à gauche) et nbSquareX*nbSquareY -1 (en bas à droite)
	 *  une CelluleActive permet alors de manipuler des chemins ou des tours avec + de liberté que la simple utilisation de Positions
	 *  
	 */
	private  abstract class CelluleActive {



		private Position pCentree; // correspond à la situation de l'élément dans la cellule
		private int numeroCaseX;
		private int numeroCaseY;


		public void setpCentree(Position p) {
			this.numeroCaseX = (int) (p.x / (squareWidth));
			this.numeroCaseY = (int) (p.y / squareHeight);
			double centreX = (numeroCaseX+0.5) / nbSquareX;
			double centreY = (numeroCaseY + 0.5) / nbSquareY;
			this.pCentree = new Position(centreX, centreY);
		}
		private void actualizepCentree() {
			double centreX = (numeroCaseX+0.5) / nbSquareX;
			double centreY = (numeroCaseY + 0.5) / nbSquareY;
			this.pCentree = new Position(centreX, centreY);
		}

		public CelluleActive(Position p) {
			setpCentree(p);
		}
		public CelluleActive(int numeroCaseX, int numeroCaseY) {
			this.numeroCaseX = numeroCaseX;
			this.numeroCaseY = numeroCaseY;
			actualizepCentree();
		}

		public CelluleActive(int numCellule) {
			numeroCaseX = numCellule % nbSquareX;
			numeroCaseY = (nbSquareY-1) - ((int) numCellule/nbSquareX);
			actualizepCentree();
		}

		public int getNumeroCellule() {
			return  (nbSquareY-1-numeroCaseY)*nbSquareX+numeroCaseX;
		}

		public Position getPosition() {
			return pCentree;
		}

		public int getNumeroCaseX() {
			return numeroCaseX;
		}

		public void setNumeroCaseX(int numeroCaseX) {
			this.numeroCaseX = numeroCaseX;
			actualizepCentree();
		}

		// décale la position de la case d'un coup vers la droite
		public void aDroite() {
			if(numeroCaseX!=nbSquareX-1)setNumeroCaseX(numeroCaseX+1);
		}
		// décale la position de la case d'un coup vers la gauche
		public void aGauche() {
			if(numeroCaseX!=0)setNumeroCaseX(numeroCaseX-1);
		}

		public int getNumeroCaseY() {
			return numeroCaseY;
		}

		public void enHaut() {
			if(numeroCaseY!=nbSquareY-1)setNumeroCaseY(numeroCaseY+1);
		}

		public void enBas() {
			if(numeroCaseY!=0)setNumeroCaseY(numeroCaseY-1);
		}

		public void setNumeroCaseY(int numeroCaseY) {
			this.numeroCaseY = numeroCaseY;
			actualizepCentree();
		}


		// renvoie une liste des numeros des cases adjacentes
		public List<Integer> casesAdjacentes(){
			List<Integer> cases = new ArrayList<Integer>();
			int num = getNumeroCellule();
			// si l'on ne se trouve pas à la première ligne, ajouter la case au dessus
			if(numeroCaseY!=0)cases.add(num-nbSquareX); 
			// si l'on ne se trouve pas à la première colonne, ajouter la case à gauche
			if(numeroCaseX!=0)cases.add(num-1); 
			// si l'on ne se trouve pas à la dernière ligne, ajouter la case en dessous 
			if(numeroCaseY!=nbSquareY-1)cases.add(num+nbSquareX);
			// si l'on ne se trouve pas à la dernière colonne, ajouter la case à droite
			if(numeroCaseX!=nbSquareX-1)cases.add(num+1); 
			return cases;
		}

		// renvoie une liste des numeros des cases diagonales
		public List<Integer> casesDiagonales(){
			List<Integer> cases = new ArrayList<Integer>();
			int num = getNumeroCellule();
			// si la colonne n'est pas la première et si la ligne n'est pas la première
			if(numeroCaseX!=0 && numeroCaseY !=0)cases.add(num-nbSquareX-1); // case au dessus à gauche 
			// si la colonne n'est pas la première et si la ligne n'est pas la dernière
			if(numeroCaseX!=0 && numeroCaseY !=nbSquareY-1)	cases.add(num+nbSquareX-1); // case en dessous à gauche
			// si la colonne n'est pas la dernière et si la ligne n'est pas la première
			if(numeroCaseX!=nbSquareX-1 && numeroCaseY!=0)cases.add(num-nbSquareX+1); // case au dessus à droite
			// si la colonne n'est pas la dernière et si la ligne n'est pas la dernière
			if(numeroCaseX!=nbSquareX-1 && numeroCaseY!=nbSquareY-1)cases.add(num+nbSquareX+1); // case en dessous à droite 
			return cases;
		}
		// renvoie la cellule sous forme de CouplePosition
		public CouplePositions recouvrement() {
			double pXmin = numeroCaseX * squareWidth;
			double pXmax = (numeroCaseX+1) * squareWidth;
			double pYmin = numeroCaseY * squareHeight;
			double pYmax = (numeroCaseY+1) * squareHeight;
			Position pHautGauche = new Position(pXmin, pYmax);
			Position pBasDroite = new Position(pXmax, pYmin);
			return new CouplePositions(pBasDroite,pHautGauche);
		}

		public String toString() {
			return getNumeroCellule()+"";
		}

		public abstract void draw();
	}
	// celluleActive correspondant à une cellule entière dédiée au chemin
	private class BoutDeChemin extends CelluleActive {


		public BoutDeChemin(Position p) {
			super(p);
		}
		public BoutDeChemin(int numeroCaseX, int numeroCaseY) {
			super(numeroCaseX,numeroCaseY);
		}
		public BoutDeChemin(int numCellule) {
			super(numCellule);
		}
		public void draw() {

			Position p = super.getPosition();
			if(p.equals(spawn)) {
				//traitement case spawn
				StdDraw.picture(spawn.x, spawn.y, plateau+"portail.png",squareWidth,squareHeight);
			}
			else if(p.equals(arrival)){
				//traitement case arrivee
				double ratio = (double) life / vieDeDepart ;
				if(presenceCheatCode)ratio = (double) life / cheatLife;
				int etat =1;
				if(ratio<=0.67 && ratio>0.34)etat=2;
				else if(ratio<=0.34 && ratio>0)etat=3;
				else if(ratio==0)etat=4;
				String url = plateau +"castle"+etat+".png";
				StdDraw.picture(arrival.x,arrival.y, url, squareWidth*1.5*1.5,squareWidth*2*1.5);
			}
			else {
				if(Main.version_lite) {
					StdDraw.setPenColor(StdDraw.ORANGE);
					StdDraw.filledRectangle(p.x, p.y, squareWidth / 2, squareHeight / 2);
				}
				else StdDraw.picture(p.x, p.y, plateau+"chemin"+choixTheme+".png", squareWidth, squareHeight);
				
			}
		}
	}

	// gère la position de la tour dans la grille, la rectifie si besoin
	private class TourGrillee extends CelluleActive {
		Tower t;
		public TourGrillee(Tower t) {
			super(t.p); 
			this.t = t;

		}
		public void draw() {
			t.draw();
		}
	}

	// renvoie false si l'ajout de la tour sur le plateau n'a pas pu se faire
	// des animations sont peut - être à ajouter dans cette méthode
	public boolean ajouteTour(TourGrillee tg) {
		// !! intervention du temps : mettre la condition suivente en commentaires s'il ne faut pas faire attention au temps et  à l'organisation de la partie
		if(!infosTours.hasStarted())return false;
		// on vérifie que la position de la tour n'est pas prise : soit par une autre tour, soit parce que l'on se trouve sur le chemin
		int num = tg.getNumeroCellule();
		// cherche dans les tours posées
		for(TourGrillee t :tours) {
			if(t.getNumeroCellule()==num)return false;
		}
		// cherche dans parcours
		if(parcours.estPresent(num)) {
			infosTours.setColor(StdDraw.RED);
			infosTours.setInfos("vous ne pouvez pas poser de tour sur un chemin !");
			return false;
		}
		// on verifie si l'utilisateur a assez d'or, si oui on paye et on pose la tour
		if(!payer(tg.t.cout)) {
			infosTours.setColor(StdDraw.RED);
			infosTours.setInfos("vous n'avez pas assez d'argent pour construire la tour!");
			return false;
		}
		infosTours.setColor(StdDraw.ORANGE);
		tg.t.compteur= tg.t.getVitesseATK();
		tours.add( tg);
		return true;
	}

	public class Chemin{
		private List<BoutDeChemin>chemin; // ensemble des bouts de chemins formant le chemin que les Monstres doivent parcourir

		public Chemin(BoutDeChemin debut,BoutDeChemin fin) {
			chemin = new ArrayList<BoutDeChemin>();
			chemin.add(debut);

			// chemin rapide pour arriver au chateau
			BoutDeChemin courant = new BoutDeChemin(spawn);
			while(courant.getNumeroCellule() != fin.getNumeroCellule()) {
				while(courant.getNumeroCaseX()!= fin.getNumeroCaseX()) {
					if(courant.getNumeroCaseX()<fin.getNumeroCaseX())courant.aDroite();
					if(courant.getNumeroCaseX()>fin.getNumeroCaseX())courant.aGauche();
					BoutDeChemin tmp = new BoutDeChemin(courant.getNumeroCellule());
					ajoute(tmp);
				}
				while(courant.getNumeroCaseY()!=fin.getNumeroCaseY()) {
					if(courant.getNumeroCaseY()<fin.getNumeroCaseY())courant.enHaut();
					if(courant.getNumeroCaseY()>fin.getNumeroCaseY())courant.enBas();
					BoutDeChemin tmp = new BoutDeChemin(courant.getNumeroCellule());
					ajoute(tmp);
				}
			}
			chemin.add(fin);

		}

		/*
		 * Construit un chemin prédéfini
		 */
		public Chemin(int selection) {
			chemin = new ArrayList<BoutDeChemin>();
			List<Position>pointsDinteret = new ArrayList<Position>();
			if(selection == 1) {
				spawn = new BoutDeChemin(new Position(0.15,0.45)).getPosition();
				Position p1 = new Position(0.25,0.45);
				Position p2 = new Position(0.25,0.15);
				Position p3 = new Position(0.45,0.15);
				Position p4 = new Position(0.45,0.65);
				Position p5 = new Position(0.15,0.65);
				Position p6 = new Position(0.15,0.85);
				Position p7 = new Position(0.65,0.85);
				Position p8 = new Position(0.65,0.45);
				Position p9 = new Position(0.75,0.45);
				Position p10 = new Position(0.75,0.25);
				arrival = new BoutDeChemin(new Position(0.95,0.25)).getPosition();
				pointsDinteret.add(new Position(spawn));
				pointsDinteret.add(p1);
				pointsDinteret.add(p2);
				pointsDinteret.add(p3);
				pointsDinteret.add(p4);
				pointsDinteret.add(p5);
				pointsDinteret.add(p6);
				pointsDinteret.add(p7);
				pointsDinteret.add(p8);
				pointsDinteret.add(p9);
				pointsDinteret.add(p10);
				pointsDinteret.add(new Position(arrival));
			}
			if(selection == 2) {
				spawn = new BoutDeChemin(new Position(0.85,0.85)).getPosition();
				Position p1 = new Position(0.55,0.85);
				Position p2 = new Position(0.55,0.55);
				Position p3 = new Position(0.85,0.55);
				Position p4 = new Position(0.85,0.35);
				Position p5 = new Position(0.35,0.35);
				Position p6 = new Position(0.35,0.85);
				Position p7 = new Position(0.15,0.85);
				Position p8 = new Position(0.15,0.15);
				arrival = new BoutDeChemin(new Position(0.85,0.15)).getPosition();
				pointsDinteret.add(new Position(spawn));
				pointsDinteret.add(p1);
				pointsDinteret.add(p2);
				pointsDinteret.add(p3);
				pointsDinteret.add(p4);
				pointsDinteret.add(p5);
				pointsDinteret.add(p6);
				pointsDinteret.add(p7);
				pointsDinteret.add(p8);
				pointsDinteret.add(new Position(arrival));
				
			}
			if(selection == 3) {
				spawn = new BoutDeChemin(new Position(0.15 , 0.85)).getPosition();
				Position p0 = new Position(0.25 , 0.85);
				Position p1 = new Position(0.85 , 0.85);
				Position p2 = new Position(0.85 , 0.65);
				Position p3 = new Position(0.35 , 0.65);
				Position p4 = new Position(0.35 , 0.45);
				Position p5 = new Position(0.15 , 0.45);
				Position p6 = new Position(0.15 , 0.25);
				Position p7 = new Position(0.55 , 0.25);
				Position p8 = new Position(0.55 , 0.45);
				Position p9 = new Position(0.75 , 0.45);
				Position p10 = new Position(0.75 , 0.15);
				Position p11 = new Position(0.85 , 0.15);
				arrival = new BoutDeChemin(new Position(0.95 , 0.15)).getPosition();
				pointsDinteret.add(new Position(spawn));
				pointsDinteret.add(p0);
				pointsDinteret.add(p1);
				pointsDinteret.add(p2);
				pointsDinteret.add(p3);
				pointsDinteret.add(p4);
				pointsDinteret.add(p5);
				pointsDinteret.add(p6);
				pointsDinteret.add(p7);
				pointsDinteret.add(p8);
				pointsDinteret.add(p9);
				pointsDinteret.add(p10);
				pointsDinteret.add(p11);
				pointsDinteret.add(new Position(arrival));
			}
			constructionChemin(pointsDinteret);

		}


		/*
		 *  construit un chemin à partir d'une liste de positions qui seront reliées dans l'ordre
		 *  chaque position doit : soit être sur la même ligne, soit être sur la même colonne
		 *  que la position précédente
		 */
		private void constructionChemin(List<Position> pointsDinteret) {
			for(int i = 0;i<pointsDinteret.size()-1;i++) {
				BoutDeChemin depart = new BoutDeChemin(pointsDinteret.get(i));
				BoutDeChemin arrivee = new BoutDeChemin(pointsDinteret.get(i+1));
				if(i==0) {
					chemin.add(depart);
				}
				ajoute(depart);
				creationLigne(depart,arrivee);
				ajoute(arrivee);
			}
		}

		// construit une ligne entre un point de départ (présent sur le chemin) et une arrivée (exclue)
		private void creationLigne(BoutDeChemin depart, BoutDeChemin arrivee) {
			BoutDeChemin courant = new BoutDeChemin(depart.getNumeroCellule());
			// on va chercher la direction et agir en conséquence
			boolean gauche = arrivee.getNumeroCaseX()<depart.getNumeroCaseX();
			boolean droite = arrivee.getNumeroCaseX()>depart.getNumeroCaseX();
			boolean haut = arrivee.getNumeroCaseY()>depart.getNumeroCaseY();
			boolean bas = arrivee.getNumeroCaseY()<depart.getNumeroCaseY();
			while(courant.getNumeroCellule() != arrivee.getNumeroCellule()) {
				if(gauche)courant.aGauche();
				else if(droite)courant.aDroite();
				else if(haut)courant.enHaut();
				else if(bas) courant.enBas();
				if(courant.getNumeroCellule()!=arrivee.getNumeroCellule()) {
					BoutDeChemin tmp = new BoutDeChemin(courant.getNumeroCellule());
					ajoute(tmp);
				}
			}
		}



		// vérifie que si l'on rajoute un BoutDeChemin eu chemin cela forme un "bloc" de 4 cellules accollées,
		private boolean agregat(BoutDeChemin bdc) {
			// on vérifie si au moins 2 bouts de chemins adjacents à bdc sont présents sur le chemin
			List<Integer>adj = bdc.casesAdjacentes();
			List<Integer>adjpresentes = numCasePresents(adj);
			if(adjpresentes.size()<2)return false;
			// on regarde alors du côté des cases diagonales
			List<Integer>diago = bdc.casesDiagonales();
			List<Integer>diagopresents = numCasePresents(diago);
			if(diagopresents.size()<1) return false;
			// on peut alors vérifier s'il n'y a pas la formation d'un "angle droit" d'un côté de bdc présent sur le chemin
			return angleDroit(adjpresentes,diagopresents);
		}
		// renvoie les numéros de cases de la liste présents sur le chemin
		public List<Integer>numCasePresents(List<Integer>numCases){
			List<Integer>casesPresentes = new ArrayList<Integer>();
			for(Integer num : numCases) {
				if(estPresent(num))casesPresentes.add(num);
			}
			return casesPresentes;
		}
		/*
		 *  vérifie s'il y a la présence d'un chemin en diagonale de bdc d'un côté 
		 *   et la présence  d'un chemin adjacent de ce même côté 
		 *   et la présence d'un chemin adjacent à la même hauteur que cette diagonale 
		 */
		private boolean angleDroit( List<Integer>adjpresentes,List<Integer>diagopresents) {
			// pour chaque chemin en diagonale,
			for(Integer cheminDiago : diagopresents) {
				// on vérifie s'il y a la présence d'un cote adjacent ayant la même "hauteur" et un autre ayant le même côté
				BoutDeChemin diago = new BoutDeChemin(cheminDiago);
				boolean hauteurOK = false;
				for(Integer adjacent : adjpresentes) {
					BoutDeChemin adj = new BoutDeChemin(adjacent);
					if(diago.getNumeroCaseY()==adj.getNumeroCaseY())hauteurOK=true;
				}

				if(hauteurOK) {
					for(Integer adjacent : adjpresentes) {
						BoutDeChemin adj = new BoutDeChemin(adjacent);
						if(diago.getNumeroCaseX()==adj.getNumeroCaseX())return true;
					}
				}
			}
			return false;
		}

		// ajoute un BoutDeChemin bdc si il est adjacent à un BoutDeChemin present dans le chemin
		public boolean ajoute(BoutDeChemin bdc) {
			if(estPresent(bdc.getNumeroCellule())) 	return false;
			// recherche si les numeros de bouts de chemins adjacents existent
			List<Integer>casesPossibles = bdc.casesAdjacentes();
			boolean existe=false;
			for(Integer cp : casesPossibles) {
				if(estPresent(cp))existe = true;
			}
			if(!existe || agregat(bdc)) {
				return false;
			}
			chemin.add(bdc);
			return true;
		}

		/**
		 * 
		 * @param numero
		 * @return true si numero est un numero de cellule présent dans le chemin
		 */
		public boolean estPresent(int numero) {
			//return chemin.containsKey(numero);
			for(BoutDeChemin bdc : chemin) {
				if(bdc.getNumeroCellule()==numero)return true;
			}
			return false;
		}

		// renvoie toutes les positions recouvertes par le chemin
		public PositionsAutorisees positionsRecouvertes(){
			List<CouplePositions> lcp = new ArrayList<CouplePositions>();
			List<BoutDeChemin> lbdc = getBouts();
			for(BoutDeChemin bdc : lbdc) {
				lcp.add(bdc.recouvrement());
			}
			PositionsAutorisees pas = new PositionsAutorisees(lcp);
			return pas;
		}

		public int getTaille() {
			return chemin.size();
		}

		/**
		 * @return une liste de tous les bouts de chemins présents dans le chemin
		 */
		public List<BoutDeChemin> getBouts(){
			return chemin;
		}

		public String toString() {
			return getBouts().toString();
		}

		// on indique un numero de case. S'il correspond à une case, alors on renvoie la case qui suit
		public BoutDeChemin prochaineCase(int numero) {
			boolean trouve=false;
			for(BoutDeChemin bdc : chemin) {
				if(trouve)return bdc;
				if(bdc.getNumeroCellule()==numero) trouve = true;
			}
			return new BoutDeChemin(numero);
		}


	}

	/* 
	 * *************************************************************************************************************************************************************************************************
	 * *****************************************************************************CONSTRUCTEUR********************************************************************************************************
	 * *************************************************************************************************************************************************************************************************
	 */

	/**
	 * Initialisation du monde en fonction de la largeur, la hauteur et le nombre de cases données
	 * @param taille 
	 * @param startSquareX
	 * @param startSquareY
	 * @param endSquareX
	 * @param endSquareY
	 * @param int life
	 */
	public World(double taille, int startSquareX, int startSquareY, int endSquareX, int endSquareY, int life) {
		// délimitation du plateau
		if(Main.version_lite)taille=1;
		if(taille<1)taille=1;
		World.taille = taille;
		this.width =(int) (1000*taille); // largeur du plateau
		this.height =(int) (600*taille); // hauteur du plateau
		// le nombre de divisions est relatif à la taille
		this.nbSquareX = (int) (10*taille);
		this.nbSquareY = (int) (10*taille);
		this.compteur_drawFinalWave = 0; 
		squareWidth = ((double) 1 / nbSquareX);
		squareHeight = (double) 1 / nbSquareY;

		// intégration du chemin
		parcours = new Chemin(1);

		// sauvegarde des paramètres de partie et initialisation des chronomètres
		vieDeDepart = this.life = life;
		//nbMonstresTotal = 2*life;
		argentDeDepart = money = 100;
		StdDraw.clear(StdDraw.DARK_GRAY);
		StdDraw.setPenColor(StdDraw.YELLOW);
		StdDraw.text(0.5, 0.5, "Chargement...");
		
		demarrage();
	}



	//*********************************************Création et déroulement de la partie************************************************

	public void demarrage() {
		//printCommands();
		this.end = false;
		tempsPartie = new Chrono();
		infosTours=new Chrono();
		infosMoney=new Chrono();
		orgaVagues = new Chrono();
		constructionVagues();
		double randm = (Math.random()*3); // il y a 3 chemins disponibles
		int selection = (int) randm+1;
		parcours = new Chemin(selection);
		this.navigationMenu = true;
		navigationCommandes=false;
		navigationPause = false;
		navigationCode = false;
		life =vieDeDepart;
	}

	// numero de la vague en cours (de 0 à vagues.size() -1)
	int numVagueCourante;
	// numero de la selection de vague
	int typeVague;

	public void constructionVagues() {
		double randm = (Math.random()*3); // il y a 2 types de parties disponibles
		int selection = (int) randm+1;
		typeVague = selection;
		constructionVagues(selection);
	}
	public void constructionVagues(int selection) {
		vagues = new LinkedList<Vague>();
		if(selection == 1) {
			for(int i=1;i<=6;i++) {
				Vague vi = new Vague(1,2*i,i,i/2,i/3);
				vi.frequence = 2;
				vagues.add(vi);
			}
		}
		if(selection ==2) {
			for(int i=1;i<=15;i++) {
				Vague vi = new Vague(1,i,i/2,i/3,i/4);
				vi.frequence = 3;
				vagues.add(vi);
			}
		}
		if(selection == 3) {
			Vague v1= new Vague(1,3,0,0,0);
			v1.frequence = 3;
			Vague v2 = new Vague(1,3,1,0,0);
			v2.frequence = 2;
			Vague v3 = new Vague(1,5,2,1,0);
			v3.frequence =2;
			Vague v4 = new Vague(1,10,5,2,1);
			v4.frequence = 1.5;
			Vague v5 = new Vague(1,20,10,4,4);
			v5.frequence =1;
			vagues.add(v1);
			vagues.add(v2);
			vagues.add(v3);
			vagues.add(v4);
			vagues.add(v5);
			
		}
	}

	/*
	 * garde la même carte (même chemin, mêmes types de monstres, etc)
	 * mais reprend l'état de départ, avec l'or de départ, la vie de départ et l'argent de départ
	 */
	private void restart() {
		tempsPartie = new Chrono();
		infosTours=new Chrono();
		infosMoney=new Chrono();
		orgaVagues = new Chrono();
		if(!this.presenceCheatCode)life = vieDeDepart;
		else life = cheatLife;
		if(!this.presenceCheatCode)money = argentDeDepart;
		else money = cheatMoney;
		monsters = new ArrayList<Monster>();
		tours = new ArrayList<TourGrillee>();
		projectiles = new ArrayList<Projectile>();
		tempsPartie.start(); // lance la partie
		timeToExplode = new Chrono();
		end = false;
		numVagueCourante=0;
		constructionVagues(typeVague);
		orgaVagues.start();
		this.navigationMenu = false;
		this.navigationCode = false;
		this.navigationPause = false;
		this.navigationCommandes = false;
		this.navigationNiveaux = false;
	}
	
	private boolean clicProlonge = false;
	/**
	 * Récupère la touche entrée au clavier ainsi que la position de la souris et met à  jour le plateau en fonction de ces interractions
	 */
	public void run() {
		StdDraw.setCanvasSize(width, height);
		StdDraw.enableDoubleBuffering();
		//printCommands();
		//boolean pause = false;
		while(true) {
			StdDraw.clear(StdDraw.DARK_GRAY);;
			if (StdDraw.hasNextKeyTyped()) {
				key = StdDraw.nextKeyTyped();
				keyPress(key);
				if(key=='s') {
					restart();

				}
				if(key=='q') {
					demarrage();
				}
				if(key=='p' && tempsPartie.hasStarted()) {
					navigationPause=!navigationPause;
					if(!navigationPause) {
						this.navigationCommandes=false;
						this.navigationCode=false;
					}
				}
			}

			if (StdDraw.isMousePressed()) {
				mouseClick(StdDraw.mouseX(), StdDraw.mouseY());
				StdDraw.pause(50);
				clicProlonge = true;
			}
			else clicProlonge = false;
			if(end && tempsPartie.getTimeRecorded()==0) {
				tempsPartie.recordTime();
			}

			if(end && tempsPartie.getTimeRecorded()+3<tempsPartie.tempsDoubleEcoule()) {
				demarrage();
			}
			if(!navigationPause || !tempsPartie.hasStarted()) {
				tempsPartie.reprendre();
				infosMoney.reprendre();
				infosTours.reprendre();
				orgaVagues.reprendre();
				update();
			}
			else {
				// il faut stopper temps Partie !
				if(!tempsPartie.enPause) {
					tempsPartie.pause();
					infosMoney.pause();
					infosTours.pause();
					orgaVagues.pause();
				}
				drawPause();
			}
			StdDraw.show();
			StdDraw.pause(20);

		}
	}
	/**
	 * Met à  jour toutes les informations du plateau de jeu ainsi que les déplacements des monstres et les attaques des tours.
	 * @return les points de vie restants du joueur
	 */
	public int update() {
		updateEnd();

		if(tempsPartie.hasStarted() && !infosTours.hasStarted() && !infosMoney.hasStarted() && !orgaVagues.hasStarted()) {
			infosTours.start();
			infosMoney.start();
			orgaVagues.start();
		}
		drawBackground();
		drawPath();
		drawMenu();

		if(tempsPartie.hasStarted()) {
			drawTowers();
			drawInfos();
		}
		if(tempsPartie.hasStarted() &&!end) {
			if(numVagueCourante == vagues.size() - 1 && compteur_drawFinalWave < 100) {
				drawFinalWave();
				compteur_drawFinalWave++;
			}
			updateMonsters();
		}
		// condition de victoire
		if(end && tempsPartie.hasStarted()) {
			compteur_drawFinalWave = 0;
			if(victoire()) drawVictory();
			else drawDefeat();
		}
		drawMouse();

		return life;
	}

	/*
	 * renvoie les conditions de victoire
	 */
	public boolean victoire() {
		return life!=0 && end;
	}

	// renvoie le numero de cellule d'une position p
	public int numeroCellule(Position p) {
		BoutDeChemin bdc = new BoutDeChemin(p);
		int numero = bdc.getNumeroCellule();
		bdc = null;
		return numero;
	}

	public Position centreCellule(int numero) {
		BoutDeChemin bdc = new BoutDeChemin(numero);
		Position p = bdc.getPosition();
		bdc = null;
		return p;
	}

	public Position centrePosition(Position p) {
		BoutDeChemin bdc = new BoutDeChemin(p);
		Position centre = bdc.getPosition();
		bdc = null;
		return centre;
	}


	// ajoute un nombre nb de BaseMonsters à la liste monsters, et les place au niveau du spawn
	public void ajoutMonstresDeBase(int nb) {
		// calcul des positions autorisées liées au spawn
		// recherche de la case du spawn dans le parcours et transformation de ce BoutDeChemin en 
		// PositionsAutorisees pas = parcours.positionsRecouvertes();
		for(int i=0;i<nb;i++) {
			Monster m = new BaseMonster(spawn);
			monsters.add(m);
		}
	}
	// ajoute un nombre nb de monstres volants à la liste monsters, et les place au niveau du spawn
	public void ajoutMonstreVolant(int nb) {
		for(int i=0;i<nb;i++) {
			Monster m = new FlyingMonster(spawn);
			monsters.add(m);
		}
	}

	public void ajoutDino(int nb) {
		for(int i=0;i<nb;i++) {
			Monster m = new Dino(spawn);
			monsters.add(m);
		}
	}
	
	public void ajoutPtero(int nb) {
		for(int i=0;i<nb;i++) {
			Monster m = new Pterodactyle(spawn);
			monsters.add(m);
		}
	}
	
	
	public boolean proche(Position p1 , Position p2) {
		BoutDeChemin bdc_p2 = new BoutDeChemin(p2);
		List<Integer> cases_adj = bdc_p2.casesAdjacentes();
		if(!p1.equals(spawn)) {
			for(int i = 0 ; i < cases_adj.size() ; i++) {
				if(cases_adj.get(i) != null) {
					if(cases_adj.get(i) == numeroCellule(p1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Pour chaque monstre de la liste de monstres de la vague, utilise la fonction update() qui appelle les fonctions run() et draw() de Monster.
	 * Modifie la position du monstre au cours du temps 
	 */
	public void updateMonsters() {
		List<Monster>monToRemove = new LinkedList<Monster>();
		// ajout de monstres suivant la vague
		ajoutMonstres();
		for(Monster m : monsters) {
			Position p = calculeNextPosition(m);
			// quand le monstre vient d'arriver au chateau
			if(!m.aDetruire() && numeroCellule(p)==numeroCellule(arrival)) {
				timeToExplode.start();
				life-=1;
				//if(m instanceof Boss)life--; // on perd -2 de vie quand un boss explose
				monToRemove.add(m);
			}
			// dans le cas où le monstre est conservé
			if(numeroCellule(p)!=numeroCellule(arrival) && !m.aDetruire()) {
				if(!m.stop_move) {
					m.setPosition(p);
				}
				m.update();
			}
			if(!m.aDetruire() && proche(p , arrival) && m instanceof Boss) {
				m.stop_move = true;
				if(m.compteur >= m.vitesseATK) {
					projectiles.add(m.attaque(arrival));
				}
				else {
					m.updateCompteur();
				}
			}
			
			
			// gestion de l'impact des projectiles sur sur les monstres
			// les tours prédisent l'avancement du monstre
			for(TourGrillee tg : tours) {
				// reinitialise le temps de chargement des tours si on entre dans une nouvelle vague
				if(orgaVagues.nouveaute)tg.t.compteur=tg.t.getVitesseATK();
				Position pCible = calculCible(m.copie(),tg.t.creationProjectile(p),tg.t);
				if(tg.t.isInPortee(pCible) && tg.t.droitDenvoyer() && !m.estMort() && !(m instanceof FlyingMonster && tg.t instanceof CannonTower)
						&& !(m instanceof Pterodactyle && tg.t instanceof CannonTower)) {
					projectiles.add(tg.t.envoiProjectile(pCible));
				}
				if (tg.t.peutLevelUp()) {
					tg.t.a_ameliorer = true;
				}

			}
			List<Projectile>projToRemove = new LinkedList<Projectile>();
			for(Projectile projec : projectiles) {
				if(!m.estMort() && m.isInHitbox(projec.courant) && !(projec instanceof BouleDeFeu)) {
					if((m instanceof Pterodactyle) || m instanceof FlyingMonster ) {
						if(!(projec instanceof Boulet)) m.takeDammage(projec.getDegats());
					}
					else m.takeDammage(projec.getDegats());
					
					if(m.estMort()) {
						// on regarde d'ou vient le projectile
						// la tour à l'origine du tir mortel augmente son compteur de monstres tués
						for(TourGrillee tg : tours) {
							if(projec.depart == tg.t.p)tg.t.compteur_monsterkilled++;
						}
					}
					projToRemove.add(projec);
				}
				if(projec instanceof BouleDeFeu) {
					if(projec.courant.dist(arrival) < projec.getVitesse()) {
						timeToExplode.start();
						life-=1;
						projToRemove.add(projec);
					}
				}
			}
			// dès qu'un projectile a touché un monstre il ne doit plus faire effet
			for(Projectile supp : projToRemove) {
				projectiles.remove(supp);
			}
			// gestion des monstres qu'il faut enlever du plateau
			if(m.aDetruire()) {
				infosMoney.setColor(StdDraw.GREEN);
				infosMoney.setInfos("+"+m.getRecompense());
				money+=m.getRecompense();
				infosMoney.position = new Position(m.getPosition());
				monToRemove.add(m);
			}
		}
		for(TourGrillee tg : tours) {
			tg.t.updateCompteur();
			tg.t.compteur_tempspasse++;
		}
		for(Monster supp : monToRemove) {
			monsters.remove(supp);
		}
		if(timeToExplode.hasStarted() ) {
			// gestion de l'animation de l'explosion (pour ne pas que l'explosion ne dure durant qu'1 seule update)
			if( timeToExplode.getNano()<(0.5*Math.pow(10, 9)))monsterExplode();
			else timeToExplode.reinitTime();
		}
		ArrayList<Projectile>toKeep2 = new ArrayList<Projectile>();
		for(Projectile projec : projectiles) {
			projec.move();
			projec.draw();
			if(!projec.estArrive())toKeep2.add(projec);
		}
		projectiles = toKeep2;
	}
	
	/**
	 * 
	 * @param p une position
	 * @return un monstre m si m.p = p, null sinon
	 */
	private Monster monsterIsPresentSearch(Position p) {
		for(Monster m : monsters) {
			if(m.getPosition().equals(p)) {
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param p une position
	 * @return un booléen indiquant si un monstre est présent ou non à la position p
	 */
	public boolean monsterIsPresent(Position p) {
		if(monsterIsPresentSearch(p) != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return une tour si tour.p = position(x,y), null sinon
	 */
	private Tower towerIsPresentSearch(double x , double y) {
		Position courant = new Position(x,y);
		for(TourGrillee tg : tours) {
			if(tg.recouvrement().appartient(courant)) {
				return tg.t;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return un booléen indiquant si une tour est présente ou non au point de coordonnées (x,y)
	 */
	public boolean towerIsPresent(double x , double y) {
		if(towerIsPresentSearch(x,y) != null) {
			return true;
		}
		return false;
	}
	
	
	private Position calculCible(Monster m, Projectile projec, Tower tour) {
		// distance que le projectile parcours en 1 update
		double distProjec = projec.getVitesse();
		// position courante du monstre
		Position pcourante = m.getPosition();
		// position que le monstre atteindra la prochaine update

		// on regarde l'endroit où se trouve la position future
		// on regarde parmi chaque position future du monstre 
		//quel est le nombre d'updates qu'il faut pour que le monstre arrive à cette position
		//et on le compare au nb d'updates nécessaires pour que  le projectile arrive
		// si le projectile peut arriver avant le monstre, alors cela signifie qu'il ne sera pas en retard

		int nbUpdatesMonstre =0;
		while(tour.isInPortee(pcourante)) {
			int nbUpdatesTour = (int) (tour.p.dist(pcourante) /distProjec);
			if(nbUpdatesTour<=nbUpdatesMonstre)return pcourante;
			nbUpdatesMonstre++;
			m.setPosition(calculeNextPosition(m));
			pcourante = m.getPosition();
		}
		// si on ne trouve pas, on renvoie la position du monstre, par défaut
		return m.getPosition();
	}




	/*
	 * gestion de l'ajout de monstres et des vagues
	 */
	public void ajoutMonstres() {
		// on attend 20 secondes avant de faire qqch
		int tempsPreparation = 1;
		if(tempsPartie.tempsDoubleEcoule()>tempsPreparation) {
			Vague vcourante = vagues.get(numVagueCourante);
			if(numVagueCourante ==0 && orgaVagues.getInfos().startsWith("Préparation"))orgaVagues = new Chrono();
			if(vcourante.terminee() && numVagueCourante != vagues.size()-1) {
				if(!infosMoney.nouveaute) {
					infosMoney.position=null;
					money+= 20; // bonus d'or pour pouvoir récompenser le joueur d'avoir réussi la vague
					infosMoney.setColor(StdDraw.GREEN);
					infosMoney.setInfos("+"+20);
					infosMoney.nouveaute = true;
				}
				String message = "prochaine vague dans : ";
				if(!orgaVagues.getInfos().startsWith(message)) {
					orgaVagues = new Chrono();
					orgaVagues.start();
				}
				double tempsRestant = 5-orgaVagues.tempsDoubleEcoule();
				if(tempsRestant>=0)orgaVagues.setInfos(message+(int)tempsRestant);
				else {
					this.numVagueCourante++;
					orgaVagues = new Chrono();
				}
			}
			if(!vcourante.hasStarted()) {
				vcourante.start();
				orgaVagues.setInfos("Vague "+(numVagueCourante+1));
				infosMoney.nouveaute = false;
			}
			else if(vcourante.hasStarted()) {
				orgaVagues.nouveaute=false;
				vcourante.update();
			}
		}
		// on s'ocuppe du temps de préparation
		else {
			orgaVagues.setInfos("Préparation : "+(tempsPreparation - orgaVagues.tempsEcoule())+"s restantes avant l'arrivée des monstres");
		}
	}
	// une vague correspond à un ensemble de monstres que l'on ajoutera avec parcimonie pendant le temps imparti
	public class Vague{
		// type de vague c-à-d manière dont les monstres rentreront sur le plateau
		int type;
		// nombre de monstres de base qui apparaîteront durant la vague
		int nbMdeBase;
		// nombre de monstres volants qui apparaîteront durant la vague
		int nbMvolant;
		// nombre de Dinosaures
		int nbDino;
		//nombre de Ptérodactyles
		int nbPtero;

		// utile si le type de vague est 1;
		double frequence; 

		/**
		 * @param type le type de vague que l'on souhaite
		 * @param nbMdeBase le nombre de monstres de bases qui seront présents
		 */
		public Vague(int type, int nbMdeBase, int nbMvolant, int nbPtero, int nbDino) {
			this.type = type;
			this.nbMdeBase=nbMdeBase;
			this.nbMvolant = nbMvolant;
			this.nbDino = nbDino;
			this.nbPtero = nbPtero;
			frequence =2;
		}

		public boolean terminee() {
			return orgaVagues.hasStarted() && nbMonstresTotal()==0 && monsters.size()==0 ;
		}

		public int nbMonstresTotal() {
			return nbMdeBase + nbMvolant +nbDino + nbPtero;
		}
		public void start() {
			orgaVagues.start();
			ajouteUnMonstre();
		}

		public boolean hasStarted() {
			return orgaVagues.hasStarted();
		}

		public void update() {
			if(tempsPartie.enPause)orgaVagues.pause();
			if(hasStarted() && !orgaVagues.enPause && !terminee()) {
				switch(type) {
				// 1 monstre toutes les n secondes
				case 1 : {
					if(orgaVagues.tempsDoubleEcoule() - orgaVagues.getTimeRecorded() >frequence) {
						ajouteUnMonstre();
					}
				}break;
				}
			}
		}

		private void ajouteUnMonstre() {
			if(nbMdeBase!=0) {
				ajoutMonstresDeBase(1);
				nbMdeBase--;
			}
			else if(nbMvolant != 0) {
				ajoutMonstreVolant(1);
				nbMvolant--;
			}
			else if(nbDino != 0) {
				ajoutDino(1);
				nbDino--;
			}
			else if (nbPtero != 0) {
				ajoutPtero(1);
				nbPtero--;
			}
			orgaVagues.recordTime();
		}
	}

	public void updateEnd() {
		// c'est la fin de partie si la partie a commencé et qu'il n'y a plus de vie, ou qu'on l'a indiqué précédement
		if ((tempsPartie.hasStarted() && life==0) ||
				(vagues.get(vagues.size()-1).terminee()) && monsters.size()==0) end =true ;
	}

	// gestion des projectiles sur la carte
	List<Projectile> projectiles = new ArrayList<Projectile>();

	private Chrono timeToExplode;
	
	/**
	 * 
	 * @param m un monstre
	 * @return la future position du monstre, après un appel à la méthode update()
	 */
	public Position calculeNextPosition(Monster m) {
		if(m.estMort())return m.getPosition();
		double distMax = m.getSpeed();
		Position p = m.getPosition();
		// on va regarder sur quelle case se situe le monstre
		int numCase = numeroCellule(p);
		if(p.equals(this.centreCellule(numCase)))m.reached=true;
		// avant d'arriver à la case suivante on regardesi on est passé par le centre
		Position pvoulue; 
		if(m.reached) pvoulue= parcours.prochaineCase(numCase).getPosition();
		else {
			pvoulue = centreCellule(numCase);
			if(pvoulue.dist(p)<distMax) {
				distMax-= pvoulue.dist(p);
				p = centreCellule(numCase);
				m.reached = true;
				pvoulue = parcours.prochaineCase(numCase).getPosition();
			}
		}
		Position pfutur = p;
		double dist = p.dist(pvoulue);
		if(dist>distMax) {
			dist=distMax;
			/*
			 *  A cause du ratio hauteur / largeur, 
			 *  la même distance parcourue sur l'axe des X est + rapide que celle parcourue sur l'axe des Y
			 */
			if(pvoulue.estAdroite(p) || pvoulue.estAgauche(p)) {
				dist*=(double) 11/15; 
				/*
				 *  acquis experimentalement, 
				 *  devrait théoriquement être 0.6 = 600/1000,
				 *  mais dans la pratique 0.6 ralentit trop les déplacement sur l'axe des X
				 */
			}
		}
		dist*=taille;
		if(pvoulue.estAdroite(p)) 
			pfutur = new Position(p.x+dist,p.y);

		else if(pvoulue.estAgauche(p)) 
			pfutur= new Position(p.x-dist,p.y);

		else if(pvoulue.estEnBas(p)) 
			pfutur = new Position(p.x,p.y-dist);

		else if(pvoulue.estEnHaut(p)) 
			pfutur = new Position(p.x,p.y+dist);

		if(pfutur.equals(m.getPrecedente()))return this.centreCellule(numCase);
		if(numCase != this.numeroCellule(pfutur))m.reached=false;
		return (pfutur);
	}





	/**
	 * Récupère la touche appuyée par l'utilisateur et affiche les informations pour la touche sélectionnée
	 * @param key la touche utilisée par le joueur
	 */
	public void keyPress(char key) {
		key = Character.toLowerCase(key);
		this.key = key;
		switch (key) {
		case 'a':
			System.out.println("Tour d'archers sélectionnée (coût = 50 pièces)");
			break;
		case 'b':
			System.out.println("Tour à canons sélectionnée (coût = 60 pièces )");
			break;
		case 'c':
			System.out.println("Tour laser sélectionnée (coût = 80 pièces )");
			break;
		case 'e':
			System.out.println("Voici les informations sur les améliorations :"+ "\n" +
					"- Les améliorations coûtent toutes 40 pièces" + "\n" +
					"- Si vous souhaitez améliorer une tour, cliquez simplement dessus"+ "\n" +
					"- L'amélioration d'une tour d'archers vous octroie ceci : réduction de 33% du temps de rechargement, "
					+ "augmentation de la portée de 33% et une augmentation de 50% des dégâts"  + "\n" +
					"- L'amélioration d'une tour de canon vous octroie ceci : réduction de 25% du temps de rechargement, "
					+ "augmentation de la portée de de 50% et une augmentation de de 25% des dégâts" + "\n" +
					"- L'amélioration d'une tour laser vous octroie ceci : réduction de 33% du temps de rechargement, "
					+ "augmentation de la portée de 25% et une augmentation de 50% des dégâts");
			break;
		case 'f':
			System.out.println("Retirez une tour en cliquant dessus !");
			break;
		case 's':
			System.out.println("La partie commence !");
			break;
		case 'q':
			if(tempsPartie.hasStarted())System.out.println("Fin de la partie.");
			System.out.println("Retour au menu principal");
			break;
		}
	}

	/**
	 * Vérifie lorsque l'utilisateur clique sur sa souris qu'il peut: 
	 * 		- Ajouter une tour Ã  la position indiquÃ(c)e par la souris.
	 * 		- Améliorer une tour existante.
	 * Puis l'ajouter Ã  la liste des tours
	 * @param x
	 * @param y
	 */
	public void mouseClick(double x, double y) {
		// mise à jour du clic courant
		this.clicCourant = new Position(x, y);
		// gestion de la création des tours
		if(tempsPartie.hasStarted()) {
			Position p;
			double normalizedX = (int)(x / squareWidth) * squareWidth + squareWidth / 2;
			double normalizedY = (int)(y / squareHeight) * squareHeight + squareHeight / 2;
			p = new Position(normalizedX, normalizedY);
			switch (key) {
			case 'a':
				Tower at = new ArrowTower(p);
				TourGrillee tgat = new TourGrillee(at);
				if(ajouteTour(tgat)) infosTours.setInfos("nouvelle tour d'archers construite");
				break;
			case 'b':
				Tower ct = new CannonTower(p);
				TourGrillee tgct = new TourGrillee(ct);
				if(ajouteTour(tgct))infosTours.setInfos("nouvelle tour de canon construite");
				break;
			case 'c':
				Tower lt = new LaserTower(p);
				TourGrillee tglt = new TourGrillee(lt);
				if(ajouteTour(tglt))infosTours.setInfos("nouvelle tour laser construite");
				break;
			case 'e':
				if(towerIsPresent(x,y) && towerIsPresentSearch(x,y).a_ameliorer == true && payer(40)) {
					System.out.println("Ici il est possible de faire évoluer une des tours");
					towerIsPresentSearch(x,y).a_ameliorer = false;
					towerIsPresentSearch(x,y).updateLevel();
					payer(40);
					infosTours.setColor(StdDraw.LIGHT_GREEN);
					infosTours.setInfos("amélioration réussie !");
				}
				else {
					if(towerIsPresent(x,y) && !towerIsPresentSearch(x,y).a_ameliorer) {
						infosTours.setColor(StdDraw.RED);
						infosTours.setInfos("Vous n'avez pas assez de pièces pour améliorer cette tour");
						infosTours.setInfos("amélioration impossible" /*: la tour ne remplit pas les pré-requis ou bien elle a atteint le niveau maximum"*/);
					}
				}
				break;
			case 'f':
				if(towerIsPresent(x,y)) {
					Tower temp = towerIsPresentSearch(x,y);
					System.out.println("Ici il est possible de retirer une des tours");
					for(int i = 0 ; i < tours.size() ; i++) {
						if(tours.get(i).t.p.dist(temp.p) == 0) {
							money+=(temp.getCout()/2);
							tours.remove(tours.get(i));
							infosTours.setInfos("tour retirée avec succès !");
						}
					}
				}
				else {
					infosTours.setInfos("veuillez cliquer sur une tour");
				}
				break;
			}
			/*
			 *  on peut cliquer sur un boss arrivé en position pour le tuer
			 *  on n'a pas le droit de maintenir la souris appuyée pour prolonger les dégats (1 clic = 1 dégat)
			 */
			for(Monster m : monsters) {
				if(m instanceof Boss && !clicProlonge) {
					if(m.stop_move && m.isInHitbox(p)) {
						m.takeDammage(1);
					}
				}
			}
		}
	}
	
	

	/**
	 * Comme son nom l'indique, cette fonction permet d'afficher dans le terminal les diffÃ(c)rentes possibilitÃ(c)s 
	 * offertes au joueur pour intÃ(c)ragir avec le clavier
	 */
	public void printCommands() {
		List<String>commandes = getCommands();
		for(String str : commandes) {
			System.out.println(str);
		}
	}

	public List<String> getCommands() {
		List<String>commandes = new LinkedList<String>();
		commandes.add("Commandes");
		commandes.add("Appuyer sur 'A' pour sélectionner une tour d'archers (coûte "+ new ArrowTower(new Position(0,0)).getCout() +" pièces).");
		commandes.add("Appuyer sur 'B' pour sélectionner une tour de canons  (coûte "+ new CannonTower(new Position(0,0)).getCout() +"pièces.)");
		commandes.add("Appuyer sur 'C' pour sélectionner une tour laser (coûte "+ new LaserTower(new Position(0,0)).getCout() +" pièces).");
		commandes.add("Appuyer sur 'E' pour faire une amélioration (coûte " + 40+ " pièces).");
		commandes.add("Appuyer sur 'F' pour retirer une tour : vous recevez en échange un bonus d'or équivalent à la moitié de la valeur de la tour.");
		commandes.add("Cliquer sur l'herbe pour construire un bâtiment.");
		//	commandes.add("Appuyer sur 'S' pour commencer une partie"); 
		return commandes;
	}




	//****************************************************************Méthodes DRAW********************************************************


	public void drawPause() {
		navigationPause = true;
		drawBackground();
		drawPath();
		drawTowers();

		for(Monster m : monsters) {
			m.stopAvancement=true;
			m.draw();
			m.stopAvancement=false;
		}
		for(Projectile p : projectiles) {
			p.draw();
		}
		StdDraw.picture(0.5, 0.35, general+"nuage.png", this.squareHeight*nbSquareX, this.squareHeight*nbSquareY*2);
		drawMouse();
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(0.2, 0.7, "Temps écoulé : "+tempsPartie.tempsEcoule()+"''");
		StdDraw.text(0.2, 0.6, "Vague "+(this.numVagueCourante+1));

		CouplePositions aireReprendre = new CouplePositions(new Position(0.65,0.61),new Position(0.4,0.70));
		// survol 
		if(aireReprendre.appartient(sourisCourante))
			StdDraw.picture(0.52, 0.65, logos+"reprendre.png", 0.25*1.3, 0.1*1.3);
		else StdDraw.picture(0.52, 0.65, logos+"reprendre.png", 0.25, 0.1);
		// clic
		if(aireReprendre.appartient(clicCourant)) {
			this.navigationPause=false;
			this.clicCourant = null;
		}
		else {
			CouplePositions aireQuitter = new CouplePositions(new Position(0.58,0.43), new Position(0.44,0.5));
			if(aireQuitter.appartient(sourisCourante)) {
				StdDraw.picture(0.52, 0.45, logos+"quitter.png", 0.15*1.3, 0.1*1.3);
			}
			else StdDraw.picture(0.52, 0.45, logos+"quitter.png", 0.15, 0.1);
			if(aireQuitter.appartient(clicCourant)) {
				this.navigationPause = false;
				this.clicCourant = null;
				this.navigationMenu=true;
				demarrage();
			}
			else {
				CouplePositions aireRedemarrer = new CouplePositions(new Position(0.27,0.45), new Position(0.13,0.5));
				if(aireRedemarrer.appartient(sourisCourante))StdDraw.picture(0.205, 0.47, logos+"reessayer.png",0.2,0.0725);
				else StdDraw.picture(0.205, 0.47, logos+"reessayer.png",0.2/1.3,0.0725/1.3);
				if(aireRedemarrer.appartient(clicCourant)) {
					this.navigationPause = false;
					this.clicCourant = null;
					restart();
				}
				else {
					CouplePositions aireCommandes =  new CouplePositions(new Position(0.84,0.49),new Position(0.680,0.57));
					if(aireCommandes.appartient(sourisCourante))StdDraw.picture(0.76, 0.54, logos+"logocommandes.png", 0.2/1.2, 0.1/1.2);
					else 			StdDraw.picture(0.76, 0.54, logos+"logocommandes.png", 0.2/1.3, 0.1/1.3);
					if(aireCommandes.appartient(this.clicCourant) || navigationCommandes) {
						this.navigationCommandes = true;
						drawCommandes();
						this.clicCourant = null;
					}
					else {
						CouplePositions aireCode = new CouplePositions(new Position(0.835,0.32),new Position(0.676,0.41));
						if(aireCode.appartient(sourisCourante)) {
							StdDraw.picture(0.75,0.37, logos+"rentrercode.png", 0.3/1.5, 0.2/1.5);
						}
						else StdDraw.picture(0.75, 0.37, logos+"rentrercode.png", 0.3/1.7, 0.2/1.7);
						if(aireCode.appartient(clicCourant)||navigationCode) {
							this.navigationCode = true;
							this.drawRentrerCode();
							this.clicCourant = null;
						}
					}
				}
			}

		}

	}

	public void drawCommandes() {
		StdDraw.clear(StdDraw.DARK_GRAY);;
		drawBackground();
		StdDraw.picture(0.48, 0.35, general+"bignuage.png",1.1,1.5);
		StdDraw.setPenColor(StdDraw.BLUE);
		// affiche les commandes sous forme de texte
		/*List<String>commandes = getCommands();
		for(int i=0;i<commandes.size();i++) {
			StdDraw.setPenRadius(taille*0.002);
			StdDraw.text(0.48, (0.35+0.35)-(0.05*i), commandes.get(i));
		}*/
		drawImagesCommandes();
		drawMouse();
		CouplePositions aireRetour =  new CouplePositions(new Position(0.37,0.78), new Position(0.24,0.87));
		if(aireRetour.appartient(sourisCourante)) {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.25/1.5, 0.20/1.5);
		}
		else {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.3/2.5, 0.20/2.5);
		}
		if(aireRetour.appartient(this.clicCourant)) {
			navigationCommandes = false;
		}
	}

	// affcihe les commandes sous forme de logos
	private void drawImagesCommandes() {
		double x1 =0.28;
		Font f1 = new Font("SansSerif",20,15);
		StdDraw.setFont(f1);
		StdDraw.setPenColor(StdDraw.BLACK);
		for(int i=0;i<7;i++) {
			double y = 0.72-(0.08*i);
			double x2 = x1+0.14;
			double x3 = x1+0.27;
			double x4 = x1+0.31;
			double y2 = y-0.02;
			StdDraw.picture(0.2, y, logos+"Touche.png",0.5/5,0.25/5);

			switch(i) {
			case 0 : {
				StdDraw.picture(x1, y, logos+"touchea.png",0.05,0.05);
				StdDraw.picture(x2, y, logos+"selectionne.png",0.35/1.9,0.1/1.7);
				StdDraw.picture(x3, y, general+"arrowtower"+s+"level1.png",0.5/5,0.7/5);
				StdDraw.picture(x4, y2, general+"money_blank.png", 0.25, 0.25);
				StdDraw.text(x4, y, ""+ArrowTower.cout);

			}break;
			case 1 : {
				StdDraw.picture(x1, y, logos+"toucheb.png",0.05,0.05);
				StdDraw.picture(x2, y, logos+"selectionne.png",0.35/1.9,0.1/1.7);
				StdDraw.picture(x3, y, general+"cannontower"+s+"level1.png",0.5/5,0.7/5);
				StdDraw.picture(x4, y2, general+"money_blank.png", 0.25, 0.25);
				StdDraw.text(x4, y, ""+CannonTower.cout);
			}break;
			case 2 : {
				StdDraw.picture(x1, y, logos+"touchec.png",0.05,0.05);
				StdDraw.picture(x2, y, logos+"selectionne.png",0.35/1.9,0.1/1.7);
				StdDraw.picture(x3, y, general+"lasertower"+s+"level1.png",0.5/5,0.7/5);
				StdDraw.picture(x4, y2, general+"money_blank.png", 0.25, 0.25);
				StdDraw.text(x4, y, ""+LaserTower.cout);
			}break;
			case 3 : {
				StdDraw.picture(0.28, y, logos+"touchee.png",0.05/1.3,0.05/1.3);
				StdDraw.picture(0.5, y, logos+"modeamelioration.png",0.4,0.05);
			}break;
			case 4 : {
				StdDraw.picture(0.28, y, logos+"touchep.png",0.05,0.05);
				StdDraw.picture(0.45, y, logos+"active.png",0.25,0.054);
			}break;
			case 5 : {
				StdDraw.picture(0.28, y, logos+"touches.png",0.05,0.05);
				StdDraw.picture(0.51, y, logos+"demarrepartie.png",0.4,0.1/1.7);
			}break;
			case 6 :{
				StdDraw.picture(0.51, y, logos+"consequenceQ.png",0.5,0.05);
			}
			}
		}
		x1 = 0.85;
		double y = 0.35;
		StdDraw.picture(x1, y, logos+"ameliorationclic.png",0.3,0.1);
		y-=0.06;
		x1+=0.13;
		StdDraw.picture(x1, y, general+"money_blank.png", 0.25, 0.25);
		StdDraw.text(x1, y+0.02, ""+40);
		StdDraw.picture(0.8, 0.65, logos+"cliquezherbe.png",0.3,0.15);
	}

	private boolean navigationCommandes=false;
	private boolean navigationMenu = true;
	private boolean navigationPause = false;
	private boolean navigationCode = false;
	private boolean navigationNiveaux = false;


	/*
	 * Ecran de démarrage du jeu, afin de commencer une partie ou être au fait de certains éléments de jeu
	 */
	public void drawMenu() {
		if(navigationMenu && navigationCommandes)drawCommandes();
		else if(navigationMenu && navigationCode)drawRentrerCode();
		else if(navigationMenu && navigationNiveaux)drawSelectionNiveaux();
		else if(navigationMenu){
			double xNuage =0.5;
			double yNuage =0.55;
			StdDraw.picture(xNuage, yNuage, general+"nuage.png",1.3,1.9);
			StdDraw.setPenColor(StdDraw.BLUE);
			CouplePositions credits = new CouplePositions(0.6,0.75,0.39,0.83);
			if(credits.appartient(sourisCourante)) {
				StdDraw.setFont(new Font(StdDraw.getFont().getFontName(),StdDraw.getFont().getStyle(),((int)(12*taille))));
				StdDraw.text(xNuage, yNuage+0.25, "Crédits : Léo FILOCHE & Bahdon BARKHAD   (info2)");
			}
			else {
				StdDraw.picture(xNuage, yNuage+0.25, logos+"logo.png",0.45,0.25);
			}
			StdDraw.picture(xNuage, yNuage+0.1, logos+"start.png",0.5/1.5,0.06/1.5);
			CouplePositions aireCommandes =  new CouplePositions(new Position(0.2,0.83),new Position(0.008,0.91));
			if(aireCommandes.appartient(sourisCourante))StdDraw.picture(0.1, 0.87, logos+"logocommandes.png", 0.2, 0.1);
			else StdDraw.picture(0.1, 0.87, logos+"logocommandes.png", 0.2/1.1, 0.1/1.1);
			if(aireCommandes.appartient(clicCourant)) {
				this.navigationCommandes = true;
				drawCommandes();
				this.clicCourant=null;
			}
			else {
				CouplePositions aireNiveaux = new CouplePositions(new Position(0.917,0.64), new Position(0.76,0.775));
				if(aireNiveaux.appartient(sourisCourante))StdDraw.picture(0.84, 0.7, logos+"selectionniveaux.png", 0.2/1.2, 0.2/1.2);
				else StdDraw.picture(0.84, 0.7, logos+"selectionniveaux.png", 0.2/1.4, 0.2/1.4);
				if(aireNiveaux.appartient(clicCourant)) {
					this.navigationNiveaux = true;
					drawSelectionNiveaux();
					this.clicCourant = null;
				}
				else {
					CouplePositions aireCode = new CouplePositions(new Position(0.212,0.6),new Position(0.05,0.72));
					if(aireCode.appartient(sourisCourante)) {
						StdDraw.picture(0.13, 0.65, logos+"rentrercode.png", 0.3/1.5, 0.2/1.5);
					}
					else StdDraw.picture(0.13, 0.65, logos+"rentrercode.png", 0.3/1.7, 0.2/1.7);
					if(aireCode.appartient(clicCourant)) {
						this.navigationCode = true;
						drawRentrerCode();
						this.clicCourant = null;
					}
				}
			}
		}
	}
	
	private String herbe = plateau+"herbe"+choixTheme+".png";

	public void drawSelectionNiveaux() {
		StdDraw.clear(StdDraw.DARK_GRAY);;
		drawBackground();
		drawPath();
		StdDraw.picture(0.48, 0.35, general+"bignuage.png",1.1,1.5);
		CouplePositions aireRetour =  new CouplePositions(new Position(0.37,0.78), new Position(0.24,0.87));
		if(aireRetour.appartient(sourisCourante)) {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.25/1.5, 0.20/1.5);
		}
		else {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.3/2.5, 0.20/2.5);
		}
		if(aireRetour.appartient(this.clicCourant)) {
			navigationNiveaux = false;
		}
		
		// On part du principe qu'il y a 3 niveaux
		String url =plateau+"selectionchemin";
		CouplePositions aireClassique = new CouplePositions(0.35,0.45,0.15,0.65);
		if(aireClassique.appartient(sourisCourante)) {
			StdDraw.picture(0.25, 0.55, url+"classique.png",0.2,0.3);
		}else StdDraw.picture(0.25, 0.55,url+"classique.png",0.2/1.5,0.3/1.5);
		if(aireClassique.appartient(clicCourant)) {
			this.clicCourant = null;
			choixTheme = "classique";
		}
		CouplePositions airePlage = new CouplePositions(0.6,0.45,0.4,0.65);
		if(airePlage.appartient(sourisCourante)) {
			StdDraw.picture(0.5, 0.55, url+"plage.png",0.2,0.3);
		}else StdDraw.picture(0.5, 0.55, url+"plage.png",0.2/1.5,0.3/1.5);
		if(airePlage.appartient(clicCourant)) {
			this.clicCourant = null;
			choixTheme = "plage";
		}
		CouplePositions aireNeige = new CouplePositions(0.825,0.45,0.68,0.66);
		if(aireNeige.appartient(sourisCourante))
			StdDraw.picture(0.75, 0.55, url+"neige.png", 0.2, 0.3);
		else 
			StdDraw.picture(0.75, 0.55, url+"neige.png", 0.2/1.5, 0.3/1.5);
		if(aireNeige.appartient(clicCourant)) {
			this.clicCourant = null;
			choixTheme = "neige";
		}
		herbe = plateau+"herbe"+choixTheme+".png";
	}


	public void drawRentrerCode() {
		StdDraw.clear(StdDraw.DARK_GRAY);;
		drawBackground();
		drawPath();
		drawTowers();
		StdDraw.picture(0.48, 0.35, general+"bignuage.png",1.1,1.5);
		CouplePositions aireRetour =  new CouplePositions(new Position(0.37,0.78), new Position(0.24,0.87));
		if(aireRetour.appartient(sourisCourante)) {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.25/1.5, 0.20/1.5);
		}
		else {
			StdDraw.picture(0.3, 0.82, logos+"retour.png", 0.3/2.5, 0.20/2.5);
		}
		if(aireRetour.appartient(this.clicCourant)) {
			navigationCode = false;
		}
		StdDraw.setPenColor(StdDraw.BLUE);
		// bouton valider les modifications
		CouplePositions reinitialiser = new CouplePositions(new Position(0.74,0.78), new Position(0.56,0.87));
		if(reinitialiser.appartient(sourisCourante)) {
			StdDraw.picture(0.65, 0.8, logos+"reinitialiser.png",0.2*1.5,0.058*1.5);
		}
		else StdDraw.picture(0.65, 0.8, logos+"reinitialiser.png",0.2,0.058);
		if(reinitialiser.appartient(clicCourant)) {
			this.money = this.argentDeDepart;
			this.life = this.vieDeDepart;
			BaseMonster.setVitesseDeBase(BaseMonster.vitesseInit);
			BaseMonster.vieDeBase = BaseMonster.healthInit;
			ArrowTower.vitesseATK = ArrowTower.tempsDeRechargement;
			ArrowTower.porteeInit = ArrowTower.rayonInit;
			Fleche.vitesseDeBase = Fleche.vitesseInit;
			Fleche.degats = Fleche.degatsInit;
			this.presenceCheatCode = false;
		}

		//double proportionsBoutons = BoutonsModificateurs.prop;
		// Modification du nombre de pièces du joueur
		BoutonsModificateurs bMoney = new BoutonsModificateurs(0.3+(0.05*(proportionsBoutons-1)), 0.65, general+"money.png",0.06,0.1,money);
		bMoney.draw(sourisCourante);
		cheatMoney = money = bMoney.valeurVar( clicCourant);
		// Modification du nombre de vies du joueur
		BoutonsModificateurs bLife = new BoutonsModificateurs(0.2,0.65, general+"heart_v2.png",0.06/1.2,0.1/1.2,life);
		bLife.draw(sourisCourante);
		cheatLife = life = bLife.valeurVar(clicCourant);

		/*
		 * Modification des paramètres des monstres
		 */
		// Modification de la vitesse des monstres
		BoutonsModificateurs bSpeedMonsters = new BoutonsModificateurs(0.65,0.65,general+"speedMonsters.png",0.06,0.1,BaseMonster.getGraduation());
		bSpeedMonsters.draw(sourisCourante);
		int vAffichee = bSpeedMonsters.valeurVar(clicCourant);
		BaseMonster.setVitesseDeBase(vAffichee);
		// Modifie la vie des monstres (leur résistance aux dégats)
		BoutonsModificateurs bLifeInitMonsters = new BoutonsModificateurs(0.75+(0.05*(proportionsBoutons-1)),0.65,general+"monsterLife.png",0.06,0.1,BaseMonster.vieDeBase);
		bLifeInitMonsters.draw(sourisCourante);
		BaseMonster.vieDeBase = bLifeInitMonsters.valeurVar(clicCourant);

		/*
		 *  Modification des paramètres des tours
		 */
		// modification du temps de rechargement
		BoutonsModificateurs bRechargement = new BoutonsModificateurs(0.2,0.3,general+"rechargementtours.png",0.07/1.4,0.1/1.4,ArrowTower.vitesseATK);
		bRechargement.draw(sourisCourante);
		ArrowTower.vitesseATK = bRechargement.valeurVar(clicCourant);

		// modification de la portée
		BoutonsModificateurs bPortee = new BoutonsModificateurs(0.3+(0.05*(proportionsBoutons-1)),0.3,general+"porteetours.png",0.07/1.4,0.1/1.4, ArrowTower.getPorteeAffichage());
		bPortee.draw(sourisCourante);
		ArrowTower.setPorteeAffichage(bPortee.valeurVar(clicCourant));

		/*
		 * Modification des paramètres de projectiles
		 */
		// modification de la vitesse des projectiles
		BoutonsModificateurs bVitesseProj = new BoutonsModificateurs(0.65,0.3,general+"projectiles_speed.png",0.07/1.5,0.15/1.5,Fleche.getVitesseAffichage());
		bVitesseProj.draw(sourisCourante);
		Fleche.setVitesseAffichage(bVitesseProj.valeurVar(clicCourant));

		// modification des degats
		BoutonsModificateurs bDegatsProj = new BoutonsModificateurs(0.75+(0.05*(proportionsBoutons-1)),0.3,general+"projectiles_degats.png",0.07/1.5,0.15/1.5,Fleche.degats);
		bDegatsProj.draw(sourisCourante);
		Fleche.degats = bDegatsProj.valeurVar(clicCourant);
	}
	
	private double proportionsBoutons = 1.7;

	private  class BoutonsModificateurs{

		public /*static*/ double prop = proportionsBoutons;//1.7;

		private Position pPlus;
		private CouplePositions airePlus;
		private Position pMoins;
		private CouplePositions aireMoins;

		private String lienImage;
		private Position image;
		private int valeur;
		private Position norme;

		public BoutonsModificateurs(Position image,String lienImage, Position norme, int valeur) {
			this.lienImage = lienImage;
			this.image = new Position(image.x-0.005,image.y);
			this.valeur = valeur;
			this.norme = norme;


			this.pMoins = new Position(image.x-0.005,image.y-0.04*prop);
			this.pPlus = new Position(image.x,image.y+0.04*prop);
			calculAires();
		}

		private void calculAires() {
			double espacement = prop* 0.015;
			airePlus = new CouplePositions(new Position(pPlus.x+espacement,pPlus.y-espacement),
					new Position(pPlus.x-espacement,pPlus.y+espacement));
			aireMoins =  new CouplePositions(new Position(pMoins.x+espacement,pMoins.y-espacement),
					new Position(pMoins.x-espacement,pMoins.y+espacement));
		}

		public BoutonsModificateurs(double xImage, double yImage, String lienImage, double xNorme, double yNorme, int valeur) {
			this(new Position(xImage,yImage),lienImage,new Position(xNorme,yNorme), valeur);
		}

		public void draw(Position curseur) {
			if(lienImage.contains("heart.png"))	StdDraw.picture(image.x+0.006,image.y,lienImage,norme.x*prop,norme.y*prop);
			else StdDraw.picture(this.image.x, this.image.y, lienImage,norme.x*prop,norme.y*prop);
			if(airePlus.appartient(curseur)) {
				StdDraw.picture(pPlus.x, pPlus.y, general+"plus.png", 0.05*2*prop, 0.05*2*prop);
			}
			else StdDraw.picture(pPlus.x, pPlus.y, general+"plus.png", 0.025*2*prop, 0.025*2*prop);
			if(aireMoins.appartient(curseur)) {
				StdDraw.picture(pMoins.x, pMoins.y, general+"moins.png", 0.05*2*prop, 0.05*2*prop);
			}
			else StdDraw.picture(pMoins.x, pMoins.y, general+"moins.png", 0.025*2*prop, 0.025*2*prop);
			Font f1 = new Font("SansSerif",StdDraw.getFont().getStyle(),(int)(20*taille*prop/1.4));
			StdDraw.setFont(f1);
			StdDraw.text(image.x+0.05+(0.01*(prop-1)),image.y,valeur+"");
		}

		boolean modif = false;

		public int valeurVar(Position pClic) {
			if(this.simpleMoinsActive(clicCourant) && valeur>0) {
				if(valeur>9)valeur-= Math.pow(10, (valeur+"").length()-2);
				else valeur--;
				modif= true;
			}
			if(this.simplePlusActive(clicCourant)) {
				if(valeur>9)valeur+= Math.pow(10, (valeur+"").length()-2);
				else valeur++;
				modif= true;
			}
			if(modif) {
				clicCourant = null;
				presenceCheatCode = true;
			}
			return valeur;
		}


		public boolean simpleMoinsActive(Position pClic) {
			return aireMoins.appartient(pClic);
		}

		public boolean simplePlusActive(Position pClic) {
			return airePlus.appartient(pClic);
		}


	}

	/**
	 * Définit le décors du plateau de jeu.
	 */
	public void drawBackground() {	
		StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
		for (int i = 0; i < nbSquareX; i++) {
			for (int j = 0; j < nbSquareY; j++) {
				if(Main.version_lite) {
					StdDraw.setPenColor(new Color(0,128,0));
					if(choixTheme.equals("neige"))StdDraw.setPenColor(new Color(176,224,230));
					if(choixTheme.equals("plage"))StdDraw.setPenColor(new Color(0,191,255));
					 StdDraw.filledRectangle(i * squareWidth + squareWidth / 2, j * squareHeight + squareHeight / 2, squareWidth , squareHeight);
				}
				
				else StdDraw.picture(i * squareWidth + squareWidth / 2, j * squareHeight + squareHeight / 2, herbe, squareWidth, squareHeight);

			}}
		}


	/**
	 * Initialise le chemin sur la position du point de dÃ(c)part des monstres. Cette fonction permet d'afficher une route qui sera diffÃ(c)rente du dÃ(c)cors.
	 */
	public void drawPath() {
		List<BoutDeChemin> bouts = parcours.getBouts();
		for(int i=0;i<bouts.size();i++) {
			BoutDeChemin bdc = bouts.get(i);
			bdc.draw();
		}


	}

	public void drawTowers() {
		for(TourGrillee tg : tours) {
			tg.draw();
		}
	}
	
	private void drawMessage(double x, double y,String str,Color c) {
		
		StdDraw.setPenColor(StdDraw.DARK_GRAY);
		StdDraw.filledRectangle(x, y, str.length()*0.005, 0.02);
		StdDraw.setPenColor(c);
		StdDraw.text(x, y, str);
	}
	
	/**
	 * Affiche certaines informations sur l'écran telles que les points de vie du joueur ou son or
	 */
	public void drawInfos() {
		// infos Vie
		StdDraw.setPenColor(StdDraw.BLACK);
		Font f1 = new Font("SansSerif",StdDraw.getFont().getStyle(),(int)(17*taille));
		StdDraw.setFont(f1);
		double xCoeur = 0.05;
		double yCoeur = 0.9;
		StdDraw.text(xCoeur+0.03, yCoeur, life+"");
		StdDraw.picture(xCoeur, yCoeur, general+"heart.png",0.06,0.1);
		// infos Money
		StdDraw.text(0.08, 0.8, money+"");
		StdDraw.picture(0.05, 0.8, general+"money.png",0.1/2,0.15/2);
		// infos liées à la construction de tours
		if(infosTours.possedeInfos())drawMessage(0.5, 0.8, infosTours.getInfos(),infosTours.couleurInfos());

		// infos liées à la perte ou au gain d'argent
		StdDraw.setPenColor(infosMoney.couleurInfos());
		StdDraw.text(0.1, 0.75, infosMoney.getInfos());
		if(infosMoney.position != null) {
			StdDraw.picture(infosMoney.position.x, infosMoney.position.y, general+"money_blank.png", 0.25, 0.25);
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setFont(new Font(f1.getFamily(),f1.getStyle(),(int) (20*taille)));
			StdDraw.text(infosMoney.position.x, infosMoney.position.y+0.02, infosMoney.getInfos());
		}
		

		// infos liés aux vagues
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(0.5, 0.95, orgaVagues.getInfos());

		// gérer le temps !
		if( tempsPartie.tempsEcoule()==0) {
			infosMoney.start();
			infosTours.start();
		}
		if(infosMoney.tempsEcoule()>3 )infosMoney.start();
		if(infosTours.tempsEcoule()>3 )infosTours.start();
		
		CouplePositions airePause = new CouplePositions(0.99,0.93,0.90,99);
		if(airePause.appartient(sourisCourante)) {
			StdDraw.picture(0.92, 0.95, logos+"pause.png",0.2/1.5,0.15/1.5);
		}
		else StdDraw.picture(0.95, 0.95, logos+"pause.png",0.2/2.2,0.15/2.2);
		if(airePause.appartient(clicCourant)) {
			this.navigationPause = true;
			clicCourant = null;
		}
	}

	public void drawVictory() {
		StdDraw.picture(0.5, 0.5, general+"victory_pin.png");
	}
	 public void drawFinalWave() {
         StdDraw.picture(0.5, 0.5, general+"final_wave.png");
     } 

	public void drawDefeat() {
		StdDraw.picture(0.5, 0.5, general+"defeat.png");
	}
	public void monsterExplode() {
		StdDraw.picture(arrival.x, arrival.y, general+"explosion.png",squareWidth,squareHeight*2);
	}

	/**
	 * Fonction qui récupère le positionnement de la souris et permet d'afficher une image de tour en temps rÃ(c)Ã(c)l
	 *	lorsque le joueur appuie sur une des touches permettant la construction d'une tour.
	 * N'est pas effective lorsque la partie n'a pas commencé
	 */
	public void drawMouse() {
		this.sourisCourante = new Position(StdDraw.mouseX(),StdDraw.mouseY());
		if(tempsPartie.hasStarted()) {
			//Position p = centrePosition(new Position(StdDraw.mouseX(),StdDraw.mouseY()));
			double normalizedX = (int)(StdDraw.mouseX() / squareWidth) * squareWidth + squareWidth / 2;
			double normalizedY =  (int)(StdDraw.mouseY() / squareHeight) * squareHeight + squareHeight / 2;
			switch (key) {
			case 'a' : 
				ArrowTower at = new ArrowTower(new Position(normalizedX,normalizedY));
				at.draw();
				break;
			case 'b' :
				CannonTower ct = new CannonTower(new Position(normalizedX,normalizedY));
				ct.draw();
				break;
			case 'c' :
				LaserTower lt = new LaserTower(new Position(normalizedX,normalizedY));
				lt.draw();
				break;
			}
		}

	}
}
