package warcraftTD;


public class FlyingMonster extends Monster {

	public FlyingMonster(Position p) {
		super(p);
		//this.hitbox.decallage = new Position(0,-0.2*echelle.y);
		this.decallage = new Position(0,0.1*echelle.y);
	}

	@Override
	public double getSpeed() {
		return BaseMonster.getVitesseDeBase()*2;
	}

	@Override
	public int getRecompense() {
		return 8;
	}
	
	/*
	 * renvoie le nom du dossier sont stockées les images du montre
	 * toutes les images doivent être nommées monster_etatX.png
	 * avec X partant de 0
	 * l'état 0 correspond au monstre en vie
	 */
	@Override
	public String getName() {
		return "flyingMonster";
	}

	@Override
	public int getNbEtats() {
		return 2;
	}


	@Override
	public int getHealthInit() {
		return 8;
	}


	@Override
	public Monster copie() {
		return new FlyingMonster(this.getPosition());
	}

	@Override
	public String getDirection() {
		if(this.getPosition().estAgauche(precedente)) {
			return "_versGauche";
		}
		else {
			return "_versDroite";
		}
	}

	@Override
	public Projectile attaque(Position p) {
		return null;
	}

	@Override
	public int getVitesseATK() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateCompteur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getDecalage() {
		// TODO Auto-generated method stub
		return 0;
	}


}