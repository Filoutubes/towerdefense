
package warcraftTD;


public abstract class Tower {
	
	public static final String level = Main.symbole_separation_dossiers+"level";
	
	Position p; // position p de la tour
	int cout; // co�t de construction de la tour
	double vitesseATK; // vitesse d'attaque = temps de rechargement = nombre d'updates
	double portee; // port�e de la tour
	Projectile projectile; // projectile associ� � la tour
	int compteur; // compteur permettant d'avoir le nb de updates qu'une tour a pass� sur le plateau de jeu apr�s avoir tir� (remis � jour r�guli�rement)
	Position echelle;
	
	private int level_actuel; // niveau actuel de la tour (niveaux possibles : 1 (celui de base), 2 et 3)
	
	boolean a_ameliorer; // boolean indiquer si une tour est am�liorable ou non (elle respecte un certain nombre de conditions)
	
	int compteur_monsterkilled; // compteur qui associe � une tour le nombre de monstres qu'elle a pu tuer ("last hit" en g�n�ral)
	final int MONSTRE_A_TUER_MAX = 20; // condition num�ro 1 pour atteindre le level_max
	
	int compteur_tempspasse; // compteur qui associe � une tour le temps qu'elle passe sur le plateau
	final int TEMPS_A_PASSER_MAX = 1000; // condition num�ro 2 pour atteindre le level_max (correspond � 1000 updates)

	public Tower(Position p) {
		this.p = p;
		this.cout = getCout();
		this.vitesseATK = getVitesseATK();
		this.portee = getPortee()/World.getTaille();
		this.compteur = getVitesseATK();
		this.echelle = new Position(0.06/World.getTaille(),0.1/World.getTaille());
		this.a_ameliorer = false;
		this.compteur_monsterkilled = 0;
		this.compteur_tempspasse = 0;
		this.level_actuel = 1;
	}
	
	/**
	 * 
	 * @param p_monster, correspondant � la position du monstre
	 * @param p_tour, correpondant � la position de la tour
	 * @return true si le monstre est dans la port�e de la tour, false sinon
	 */
	private boolean isInPortee(Position p_monster , Position p_tour) {
		double portee= getPortee();
		boolean bonX = p_monster.x <= p_tour.x + portee && p_monster.x >= p_tour.x -portee;
		boolean bonY = p_monster.y <= p_tour.y + portee && p_monster.y >= p_tour.y - portee;
		return(bonX && bonY);
	}
	
	/**
	 * 
	 * @param pmonster, correspondant � la position du monstre
	 * @return true si le monstre est dans la port�e de la tour
	 */
	public boolean isInPortee(Position pmonster) {
		return isInPortee(pmonster,p);
	}

	
	/**
	 * 
	 * @param position, correspondant � la position d'arriv�e du projectile en question (la position du monstre)
	 * @return un projectile (de type associ� � la tour en question)
	 */
	public Projectile envoiProjectile(Position position) {
		this.projectile = creationProjectile(position);
		compteur =0;
		return projectile;
	}

	
	/**
	 * 
	 * @param monstre qui correspond � la position du monstre
	 * @return un projectile (dont le type est associ� au type de la tour en question), qui aura pour position d'arriv�e, p monstre
	 */
	public abstract  Projectile creationProjectile(Position monstre);

	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	public boolean droitDenvoyer(){ 
		return (compteur >=getVitesseATK() );
	}

	public  void updateCompteur() {
		this.compteur++;
	}
	
	/**
	 * 
	 * @return true si la tour peut level up
	 * @note une tour peut level up � chaque fois, si elle a tu� un certain nombre de monstres ET si elle a pass� un certain temps sur le plateau
	 */
	public boolean peutLevelUp() {
		boolean ret = false;
		switch(level_actuel) {
		case 1 : //level_actuel = 1, la tour va passer au level 2
			ret = compteur_monsterkilled >= MONSTRE_A_TUER_MAX/10 && compteur_tempspasse >= TEMPS_A_PASSER_MAX/10;
			break;
		case 2 : //level_actuel = 2, la tour va passer au level 3 
			ret = compteur_monsterkilled >= MONSTRE_A_TUER_MAX && compteur_tempspasse >= TEMPS_A_PASSER_MAX;
		}
		return ret;
	}
	
	/**
	 * @note m�thode draw des tours : selon que la tour en question soit am�lior�e/am�liorable ou non, on fait appel � une image diff�rente
	 */
	public void draw() {
		String arborescence = World.general+getName()+level;
		if(!a_ameliorer) {
			StdDraw.picture(p.x, p.y, arborescence +this.getLevel()+".png" , this.echelle.x*2,this.echelle.y*2);
		}
		else {
			StdDraw.picture(p.x, p.y, arborescence +this.getLevel()+"_a_ameliorer.png" , this.echelle.x*2,this.echelle.y*2);
		}
	}
	
	public String toString() {
		return this.getName() +" : " +this.p +" : niveau "+this.getLevel() + " : tempsDeRechargement : "+this.getVitesseATK() +" :  portee :" +this.getPortee(); 
	}
	
	/**
	 * 
	 * @return un entier correspondant au niveau actuel de la tour
	 */
	public int getLevel() {
		return level_actuel;
	}
	
	/**
	 * @note m�thode qui permet � la tour de level up
	 */
	public void updateLevel() {
		this.level_actuel++;
	}
	
	/**
	 * 
	 * @return un double correspondant � la port�e de la tour, selon le niveau de la tour
	 */
	public double getPortee() {
		return getPorteeInit() + 0.05*(level_actuel-1);
	}
	
	/**
	 * 
	 * @return le co�t de construction de la tour
	 */
	public abstract int getCout();
	
	/**
	 * 
	 * @return un entier correspondant au temps de rechargement de la tour , en nombre de update()
	 */
	public abstract int getVitesseATK();
	
	/**
	 * 
	 * @return un double correspondant � la port�e initiale de la tour
	 */
	public abstract double getPorteeInit() ;
	
	/*
	 * @return le nom du dossier dans lequel se trouve l'image de la tour
	 */
	public abstract String getName();
}
