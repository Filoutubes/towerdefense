package warcraftTD;

public class Dino extends Boss {
	
	public Dino(Position p) {
		super(p);
		this.hitbox.modifProportions(2);
		this.hitbox.decallage = new Position(0,0.2*echelle.y);
		this.decallage = new Position(0,0.5*echelle.y);
	}
	
	@Override
	public void draw() {
		Position copieEch = this.echelle;
		this.echelle = new Position(echelle.x*6,echelle.y*6);
		super.draw();
		this.echelle = copieEch;
	}
	
	@Override
	public int getNbEtats() {
		return 4;
	}

	@Override
	public String getName() {
		return super.getName()+"dino";
	}

	@Override
	public int getHealthInit() {
		return 20*BaseMonster.healthInit;
	}

	@Override
	public double getSpeed() {
		return BaseMonster.getVitesseDeBase()/3; // c'est un boss -> bcp de vie mais vitesse assez lente
	}

	@Override
	public int getRecompense() {
		return 100;
	}

	@Override
	public Monster copie() {
		return new Dino(getPosition());
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
	public double getDecalage() {
		// TODO Auto-generated method stub
		return 0.2;
	}

}