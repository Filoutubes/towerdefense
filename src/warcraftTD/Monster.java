package warcraftTD;


public abstract class Monster {



	// Position du monstre Ã  l'instant t (position courante)
	private Position p;
	// correction de la position dû au format de l'image (le centre n'est parfois pas le bon)
	public Position decallage = new Position(0,0);
	// Points de vie du monstre
	private int health;
	// Vitesse du monstre
	double speed;
	// Position du monstre Ã  l'instant t+1
	Position nextP;
	// position du monstre à l'instant t-1
	Position precedente;
	// Boolean pour savoir si le monstre a atteint une position importante
	boolean reached;
	//Boolean pour arrêter le mouvement du monstre
	boolean stop_move;
	// Compteur correspondant au temps d'attaque entre 2 attaques sur le chateau de la part d'un boss
	int compteur;
	// Vitesse d'attaque des boss
	int vitesseATK;
	// Compteur de dÃ(c)placement pour savoir si le monstre Ã  atteint le chateau du joueur
	int checkpoint = 0;
	// dimensions de la cellule
	Position echelle;

	String direction;

	PositionsAutorisees pa;

	JaugeDeVie jdv; // Objet de type JaugeDeVie correspondant à la barre de vie interactive associée au monstre

	public class SquareHitbox {
		private double cote;

		// gestion des petits décallages qu'il peut y avoir entre le centre du monstre et celui de la hitbox
		public Position decallage = new Position(0,0);

		public SquareHitbox(double cote) {
			this.cote = cote;
		}

		public void modifProportions(double prop) {
			this.cote*=prop;
		}

		//diagonale d'un carré = c * sqrt(2) (pythagore)

		public Position getPositionCoinSupGauche() {
			double x_min = p.x - cote/2;
			double y_max = p.y + cote/2;
			return(new Position(x_min +decallage.x, y_max+decallage.y));
		}

		public Position getPositionCoinInfDroit() {
			double x_max = p.x + cote/2;
			double y_min = p.y - cote/2;
			return(new Position(x_max +decallage.x, y_min +decallage.y));
		}

		public void draw() {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.005);
			double x = (getPositionCoinInfDroit().x+getPositionCoinSupGauche().x)/2;
			double y = (getPositionCoinInfDroit().y+getPositionCoinSupGauche().y)/2;
			StdDraw.rectangle(x, y, cote/3, cote/2);
		}

	}
	public class JaugeDeVie {

		private int nombre_ref;
		private int nb_courant;

		public JaugeDeVie(Position p) {
			this.nombre_ref = getHealthInit();
			this.nb_courant = getHealth();
		}

		public void update() {
			nb_courant = getHealth();
		}

		/*
		 * On souhaite dessiner une jauge de vie en temps réel d'un monstre : pour cela nous avons besoin de 2 rectangles.
		 * Le premier (appelé r1) délimitera la barre de vie (rectangle dessiné avec contours uniquement) tandis que 
		 * le second (r2) sera le rectangle, qui représentera la barre de vie.
		 * 
		 * La bibliothèque StdDraw n'est pas très pratique puisqu'elle nous oblige à définir un rectangle depuis son centre.
		 * 
		 * Pour avoir un rectangle dont la longueur varie, nous savons qu'on aura :
		 * - longueur_r2 = ratio_courant * longueur_r1 avec ratio = vie_courante/vie_init
		 * 
		 * N'oublions pas que le centre du rectangle (selon x) doit être modifié également !! -> Pas de modifs selon y
		 * 
		 * On aura dès lors : new_centre.x_r2 = centre.x_r2 - dx * longueur_r1 * centre.x_r2 avec dx = (vie_init - vie_courante)/2
		 */
		public void draw() {
			update();
			double ratio_courant = Math.abs((nb_courant * 1.0/nombre_ref*1.0));
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(0.005);
			double pointY = hitbox.getPositionCoinInfDroit().y-0.01;
			StdDraw.rectangle(p.x, pointY, (echelle.x)/2, 0.01/2);
			if(ratio_courant >= 0.75) {
				StdDraw.setPenColor(StdDraw.GREEN);
			}
			else if (ratio_courant >= 0.5 && ratio_courant <= 0.75) {
				StdDraw.setPenColor(StdDraw.YELLOW);
			}
			else if (ratio_courant >= 0.25 && ratio_courant <= 0.5) {
				StdDraw.setPenColor(StdDraw.ORANGE);
			}
			else if (ratio_courant >= 0 && ratio_courant <= 0.25) {
				StdDraw.setPenColor(StdDraw.RED);
			}
			double centre = (p.x-echelle.x/2)+ (0.5 *(ratio_courant))*echelle.x;
			StdDraw.filledRectangle(centre, pointY,(ratio_courant * (echelle.x)/2) , (0.005));
		}


	}

	private double LONGUEUR_CARRE;
	public SquareHitbox hitbox;

	public Monster(Position p) {
		this.p = p;
		this.nextP = new Position(p);
		this.precedente = new Position(p);
		this.deadState =0;
		this.echelle = new Position(0.06/World.getTaille(),0.1/World.getTaille());
		this.LONGUEUR_CARRE = echelle.y/1.5;
		this.hitbox = new SquareHitbox(LONGUEUR_CARRE);
		this.health = getHealthInit();
		this.jdv = new JaugeDeVie(p);
		this.stop_move = false;
		this.vitesseATK = getVitesseATK();
		this.compteur = getVitesseATK();
	}

	public void setEchelle(Position p) {
		this.echelle = new Position(p.x/World.getTaille(),p.y/World.getTaille());
		this.LONGUEUR_CARRE = echelle.y/1.5;
		this.hitbox = new SquareHitbox(LONGUEUR_CARRE);
	}

	public abstract Monster copie();
	
	/**
	 * 
	 * @param monster, objet correspondant au monstre lui-même
	 * @param p, correspondant à la position du projectile
	 * @return un booléen indiquant si oui ou non, p est dans la hitbox
	 */
	private boolean isInHitbox(Monster monster , Position p) {
		Position coinSupGauche = monster.hitbox.getPositionCoinSupGauche(); // couple (x_min , y_max)
		Position coinInfDroit = monster.hitbox.getPositionCoinInfDroit(); // couple (x_max , y_min)
		return((coinSupGauche.x <= p.x && p.x <= coinInfDroit.x) && (coinInfDroit.y <= p.y && p.y <= coinSupGauche.y));
	}
	
	/**
	 * 
	 * @param p, correspondant à la position du projectile
	 * @return un booléen indiquant si oui non non, p est dans la hitbox associée au monstre
	 */
	public boolean isInHitbox(Position p) {
		return isInHitbox(this,p);
	}
	
	/**
	 * 
	 * @param nextP, correspondant à la position courante du monstre après un appel à la méthode update()
	 * @param pa, une liste de CouplePositions 
	 * @return un booléen indiquant si oui ou non le monstre est bien sur le chemin
	 */
	public boolean isInChemin(Position nextP, PositionsAutorisees pa) {
		if(pa.estAutorisee(nextP)) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "Type :" + getName()+"  Santé : "+getHealth()+"  Position : "+p;
	}

	public void update() {
		draw();
		//hitbox.draw(); 
		jdv.draw();

	}

	public Position getPosition() {
		return p;
	}

	public void setX(double x) {
		this.precedente = p;
		p.x = x;
	}

	public void setY(double y) {
		this.precedente = p;
		p.y = y;
	}

	public double getX() {
		return p.x;
	}
	public double getY() {
		return p.y;
	}

	public void setPosition(Position p) {
		this.precedente = this.p;
		this.p=p;
	}
	public Position getPrecedente() {
		return precedente;
	}

	public  boolean estMort() {
		return this.getHealth()==0;
		//return true;
	}

	public abstract int getNbEtats();

	private int deadState;
	public  int etatDeMort() {
		return deadState;
	}
	
	/**
	 * 
	 * @param dammage un entier correspondant aux dégâts qu'est censé prendre le monstre
	 */
	public void takeDammage(int dammage) {
		prendDesDegats =true;
		if(dammage>health)health=0;
		else health-=dammage;
	}
	
	protected void avancementEtat() {
		// pour eviter les bugs
		if(aDetruire())return;
		if(deadState<=getNbEtats() && estMort()) {
			if(nbRepetitions==nbRepetitionsMax) {
				deadState++;
				nbRepetitions=0;
			}
			else nbRepetitions++;
		}
		else if(prendDesDegats) {
			if(nbRepetitions==nbRepetitionsMax) {
				nbRepetitions=0;
				prendDesDegats=false;
			}
			else nbRepetitions++;
		}


	}
	
	public boolean aDetruire() {
		return (deadState==getNbEtats() && nbRepetitions==nbRepetitionsMax);
	}

	private final int nbRepetitionsMax = 5;
	private int nbRepetitions;

	private Position getCorrection() {
		return new Position(p.x+decallage.x, p.y+decallage.y);
	}

	/**
	 * @note Affiche un monstre qui change de couleur au cours du temps
	 */
	public void draw() {
		String arborescence = World.general+getName();
		String etat = arborescence +World.s+"etat"+this.etatDeMort()+getDirection()+".png";
		Position tmp = new Position(p);
		p = getCorrection();
		if(p.estAdroite(precedente)) {
			if(this.etatDeMort()==1)StdDraw.picture(p.x, p.y, etat, echelle.x, echelle.y, -45);
			else if(prendDesDegats && !estMort())StdDraw.picture(p.x, p.y-0.1*echelle.y, arborescence+World.s+"dommages"+getDirection()+".png", echelle.x, echelle.y);
			else StdDraw.picture(p.x, p.y-0.1*echelle.y, etat,echelle.x,echelle.y);
			if(!stopAvancement)avancementEtat();
			p= tmp;
		}
		else {
			if(this.etatDeMort()==1)StdDraw.picture(p.x, p.y, etat, echelle.x, echelle.y, 45);
			else if(prendDesDegats && !estMort())StdDraw.picture(p.x, p.y-0.1*echelle.y, arborescence+World.s+"dommages"+getDirection()+".png", echelle.x, echelle.y);
			else StdDraw.picture(p.x, p.y-0.1*echelle.y, etat,echelle.x,echelle.y);
			if(!stopAvancement)avancementEtat();
			p= tmp;
		}
	}

	public boolean stopAvancement=false;

	private boolean prendDesDegats;
	public boolean getPrendDesDegats() {
		return prendDesDegats;
	}

	/**
	 * 
	 * @return le nom du dossier dans lequel se trouvent les images du monstre en question
	 */
	public abstract String getName();
	
	/*
	 * @return la santé initiale du monstre
	 */
	public abstract int getHealthInit();
	
	/*
	 * @return la santé actuelle du monstre
	 */
	public  int getHealth() {
		return this.health;
	}
	
	
	
	/**
	 * 
	 * @return une chaîne de caractères nous indiquant si le monstre va vers la droite ou la gauche 
	 * (pour les monstres dont l'image load depuis la méthode draw(), n'offre pas de symétrie)
	 */
	public abstract String getDirection();
	
	/**
	 * 
	 * @return un double correspondant à la vitesse du monstre
	 */
	public abstract double getSpeed();
	
	/**
	 * 
	 * @return un entier qui correspond au bonus de pièces qu'on obtient, lorsque le monstre est tué
	 */
	public abstract int getRecompense();
	
	/**
	 * 
	 * @param p la position actuelle du monstre
	 * @return un projectile boule de feu
	 * @note cette caractéristique est spécifique aux boss
	 */
	public abstract Projectile attaque(Position p);
	
	/**
	 * 
	 * @return temps d'attente nécessaire entre deux attaques
	 * @note cette caractéristique est spécifique aux boss
	 */
	public abstract int getVitesseATK();
	
	/**
	 * @note cette méthode permet de gérer le temps d'attente entre 2 attaques sur le château : caractéristique spécifique aux boss
	 */
	public abstract void updateCompteur();
	
	/**
	 * @return le décalage par rapport au centre de l'image du monstre
	 */
	public abstract double getDecalage();
}