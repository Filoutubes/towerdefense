
package warcraftTD;

public abstract class Projectile {
	
	final Position depart; // Position p de départ du projectile
	final Position arrivee; // Position p d'arrivée du projectile
	Position courant; // Position courante du projectile
	Position precedent; // Position précédente du projectile
	
	public Projectile(Position depart , Position arrivee) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.courant = new Position(depart.x,depart.y);
		this.precedent = new Position(depart.x,depart.y);
	}
	
	
	
	
	/**
	 * @note méthode move d'un projectile : le projectile se déplace en ligne droite (pas de courbe ou autre)
	 */
	public void move() {
		double dx = arrivee.x - courant.x;
		double dy = arrivee.y - courant.y;
		this.precedent = new Position(courant.x,courant.y);
		if(courant.dist(arrivee)<getVitesse()) {
			courant = arrivee;
		}
		else {
			/* formule :
			 * on calcule le deltaSelonAxe = (position courante - position départ)/N, avec N le nombre de "pas" qu'on veut faire
			 * N correspond dans notre cas, à getVitesse() (le projectile est dirigé par de petits vecteurs de normes
			 * getVitesse() ) : on a bien abs(dx) + abs(dy) = getVitesse())
			 */
			double ratioX =  dx/(Math.abs(dx) + Math.abs(dy));
			double ratioY = dy/(Math.abs(dx) + Math.abs(dy));
			/* on a courant = depart à l'initialisation du projectile : on part bien du point de départ et on y ajoute
			ratioSelonAxe * getVitesse() à intervalles de update réguliers*/
			this.courant.x += ratioX * getVitesse();
			courant.y += ratioY * getVitesse(); 
		
		}
		
	}
	
	public String toString() {
		return "("+depart+" -> " +arrivee+" : "+courant+")";
	}
	public boolean estArrive() {
		return courant.equals(arrivee);
	}
	
	/**
	 * 
	 * @return un entier correspondant aux dégâts que peut causer le projectile à un monstre
	 */
	public abstract int getDegats();
	
	/**
	 * 
	 * @return un double correspondant à la vitesse du projectile
	 */
	public abstract double getVitesse();
	
	/**
	 * @note méthode draw() spécifique à chaque type de projectile : à un projectile, correspond une image
	 */
	public abstract void draw();
	
	/**
	 * 
	 * @param degats, un entier qui correspond à la nouvelle valeur de dégâts que cause le projectile
	 */
	public abstract void setDegats(int degats);

}
