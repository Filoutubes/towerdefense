
package warcraftTD;

public abstract class Projectile {
	
	final Position depart; // Position p de d�part du projectile
	final Position arrivee; // Position p d'arriv�e du projectile
	Position courant; // Position courante du projectile
	Position precedent; // Position pr�c�dente du projectile
	
	public Projectile(Position depart , Position arrivee) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.courant = new Position(depart.x,depart.y);
		this.precedent = new Position(depart.x,depart.y);
	}
	
	
	
	
	/**
	 * @note m�thode move d'un projectile : le projectile se d�place en ligne droite (pas de courbe ou autre)
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
			 * on calcule le deltaSelonAxe = (position courante - position d�part)/N, avec N le nombre de "pas" qu'on veut faire
			 * N correspond dans notre cas, � getVitesse() (le projectile est dirig� par de petits vecteurs de normes
			 * getVitesse() ) : on a bien abs(dx) + abs(dy) = getVitesse())
			 */
			double ratioX =  dx/(Math.abs(dx) + Math.abs(dy));
			double ratioY = dy/(Math.abs(dx) + Math.abs(dy));
			/* on a courant = depart � l'initialisation du projectile : on part bien du point de d�part et on y ajoute
			ratioSelonAxe * getVitesse() � intervalles de update r�guliers*/
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
	 * @return un entier correspondant aux d�g�ts que peut causer le projectile � un monstre
	 */
	public abstract int getDegats();
	
	/**
	 * 
	 * @return un double correspondant � la vitesse du projectile
	 */
	public abstract double getVitesse();
	
	/**
	 * @note m�thode draw() sp�cifique � chaque type de projectile : � un projectile, correspond une image
	 */
	public abstract void draw();
	
	/**
	 * 
	 * @param degats, un entier qui correspond � la nouvelle valeur de d�g�ts que cause le projectile
	 */
	public abstract void setDegats(int degats);

}
