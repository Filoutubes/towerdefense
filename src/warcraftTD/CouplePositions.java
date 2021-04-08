package warcraftTD;


public class CouplePositions {
	private Position pBasDroite;
	private Position pHautGauche;
	
	/**
	 * 
	 * @param pBasDroite
	 * @param pHautGauche
	 */
	public CouplePositions(Position pBasDroite, Position pHautGauche) {
		this.pBasDroite = new Position(pBasDroite);
		this.pHautGauche = new Position(pHautGauche);
	}
	
	public CouplePositions(double x1, double y1, double x2, double y2) {
		this.pBasDroite = new Position(x1,y1);
		this.pHautGauche = new Position(x2,y2);
	}
	
	/**
	 * 
	 * @param p une position
	 * @return un booléen indiquant si oui ou non p appartient au rectangle délimité par le couple de positions courant
	 */
	public boolean appartient(Position p) {
		if(p == null)return false;
		boolean bonX = p.x<= pBasDroite.x && (p.x >= pHautGauche.x);
		boolean bonY = p.y <= pHautGauche.y && p.y>= pBasDroite.y;
		return bonX && bonY;
	}
	
	/**
	 * 
	 * @param p une position
	 * @return si p est présent sur le segment construit par les 2 positions
	 */
	public boolean appartientSegment(Position p) {
		CouplePositions inverse = new CouplePositions(new Position(pHautGauche),new Position(pBasDroite));
		return appartient(p) || inverse.appartient(p);
	}
	
	public double get_dx() {
		return(Math.abs(this.pBasDroite.x - this.pHautGauche.x));
	}
	
	public double get_dy() {
		return(Math.abs(this.pHautGauche.y - this.pBasDroite.y));
	}
	
	/**
	 * 
	 * @return la longueur entre les deux positions du couple de positions courant
	 */
	public double longueur() {
		return(pBasDroite.dist(pHautGauche)); //Pythagore
	}
	
	/**
	 * 
	 * @return une position dont les composantes selon x et y correspondent aux coordonées du vecteur représenté par le couple de positions
	 */
	public Position coordonneesVecteur() {
		// segment allant du point pBasDroite au point pHautGauche
		double coordSelonX = this.pHautGauche.x - this.pBasDroite.x;
		double coordSelonY = this.pHautGauche.y - this.pBasDroite.y;
		return (new Position(coordSelonX , coordSelonY));
	}
	
	/**
	 * 
	 * @param Segment correspondant à un couple de positions
	 * @return un booléen indiquant si le vecteur courant (représenté par un couple de positions) est colinéaire au vecteur mis en paramètre
	 */
	public boolean estColineaire(CouplePositions Segment) {
		Position Vector = (new CouplePositions(pBasDroite , pHautGauche)).coordonneesVecteur();
		Position Vector2 = Segment.coordonneesVecteur();
		return (Vector.x * Vector2.y == Vector.y * Vector2.x);
		
	}
	
	public String toString() {
		return "("+pBasDroite+","+pHautGauche+")";
	}
}
