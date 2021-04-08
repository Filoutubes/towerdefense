
package warcraftTD;


public abstract class Tower {
	
	public static final String level = Main.symbole_separation_dossiers+"level";
	
	Position p; // position p de la tour
	int cout; // coût de construction de la tour
	double vitesseATK; // vitesse d'attaque = temps de rechargement = nombre d'updates
	double portee; // portée de la tour
	Projectile projectile; // projectile associé à la tour
	int compteur; // compteur permettant d'avoir le nb de updates qu'une tour a passé sur le plateau de jeu après avoir tiré (remis à jour régulièrement)
	Position echelle;
	
	private int level_actuel; // niveau actuel de la tour (niveaux possibles : 1 (celui de base), 2 et 3)
	
	boolean a_ameliorer; // boolean indiquer si une tour est améliorable ou non (elle respecte un certain nombre de conditions)
	
	int compteur_monsterkilled; // compteur qui associe à une tour le nombre de monstres qu'elle a pu tuer ("last hit" en général)
	final int MONSTRE_A_TUER_MAX = 20; // condition numéro 1 pour atteindre le level_max
	
	int compteur_tempspasse; // compteur qui associe à une tour le temps qu'elle passe sur le plateau
	final int TEMPS_A_PASSER_MAX = 1000; // condition numéro 2 pour atteindre le level_max (correspond à 1000 updates)

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
	 * @param p_monster, correspondant à la position du monstre
	 * @param p_tour, correpondant à la position de la tour
	 * @return true si le monstre est dans la portée de la tour, false sinon
	 */
	private boolean isInPortee(Position p_monster , Position p_tour) {
		double portee= getPortee();
		boolean bonX = p_monster.x <= p_tour.x + portee && p_monster.x >= p_tour.x -portee;
		boolean bonY = p_monster.y <= p_tour.y + portee && p_monster.y >= p_tour.y - portee;
		return(bonX && bonY);
	}
	
	/**
	 * 
	 * @param pmonster, correspondant à la position du monstre
	 * @return true si le monstre est dans la portée de la tour
	 */
	public boolean isInPortee(Position pmonster) {
		return isInPortee(pmonster,p);
	}

	
	/**
	 * 
	 * @param position, correspondant à la position d'arrivée du projectile en question (la position du monstre)
	 * @return un projectile (de type associé à la tour en question)
	 */
	public Projectile envoiProjectile(Position position) {
		this.projectile = creationProjectile(position);
		compteur =0;
		return projectile;
	}

	
	/**
	 * 
	 * @param monstre qui correspond à la position du monstre
	 * @return un projectile (dont le type est associé au type de la tour en question), qui aura pour position d'arrivée, p monstre
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
	 * @note une tour peut level up à chaque fois, si elle a tué un certain nombre de monstres ET si elle a passé un certain temps sur le plateau
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
	 * @note méthode draw des tours : selon que la tour en question soit améliorée/améliorable ou non, on fait appel à une image différente
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
	 * @note méthode qui permet à la tour de level up
	 */
	public void updateLevel() {
		this.level_actuel++;
	}
	
	/**
	 * 
	 * @return un double correspondant à la portée de la tour, selon le niveau de la tour
	 */
	public double getPortee() {
		return getPorteeInit() + 0.05*(level_actuel-1);
	}
	
	/**
	 * 
	 * @return le coût de construction de la tour
	 */
	public abstract int getCout();
	
	/**
	 * 
	 * @return un entier correspondant au temps de rechargement de la tour , en nombre de update()
	 */
	public abstract int getVitesseATK();
	
	/**
	 * 
	 * @return un double correspondant à la portée initiale de la tour
	 */
	public abstract double getPorteeInit() ;
	
	/*
	 * @return le nom du dossier dans lequel se trouve l'image de la tour
	 */
	public abstract String getName();
}
