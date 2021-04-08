
package warcraftTD;


public abstract class Boss extends Monster {

	Projectile projectile;

	public static int vitesseATK = 50;

	public Boss(Position p) {
		super(p);
	}

	public String getName() {
		return "boss"+World.s;
	}
	
	
	public Projectile creationProjectile(Position arrival) {
		BouleDeFeu bdf = new BouleDeFeu(getPosition() , arrival);
		return bdf;
	}

	@Override
	public int getVitesseATK() {
		return vitesseATK;
	}

	@Override
	public Projectile attaque(Position arrival) {
		this.projectile = creationProjectile(arrival);
		compteur = 0;
		return projectile;
	}

	@Override
	public void updateCompteur() {
		this.compteur++;
	}

}

