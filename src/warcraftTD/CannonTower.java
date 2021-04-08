package warcraftTD;

public class CannonTower extends Tower{
	
	public static int vitesseATK = ((5/3)*ArrowTower.vitesseATK);
	
	public static final int cout = 60;
	
	public static double porteeInit = ArrowTower.porteeInit -0.1;

	public CannonTower(Position p) {
		super(p);
	}

	@Override
	public int getCout() {
		return cout;
	}

	@Override
	public int getVitesseATK() {
		return ((int) (vitesseATK +(vitesseATK*0.25*(this.getLevel()-1))));
	}

	public double getPorteeInit() {
		return porteeInit;
	}

	@Override
	public Projectile creationProjectile(Position pmonstre) {
		Boulet boulet = new Boulet(p, pmonstre);
		boulet.setDegats(boulet.getDegats()+(this.getLevel()-1)/2);
		return boulet;
	}
	
	@Override
	public String getName() {
		return("cannontower");
	}

}
