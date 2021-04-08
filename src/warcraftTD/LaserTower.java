package warcraftTD;

public class LaserTower extends Tower{
	
	public static int vitesseATK = 2*ArrowTower.vitesseATK;
	
	public static final int cout=80;
	
	public static double porteeInit = 0.1+ArrowTower.porteeInit;
	
	public LaserTower(Position p) {
		super(p);
	}

	@Override
	public int getCout() {
		return cout;
	}
	
	@Override
	public int getVitesseATK() {
		return ((int) (vitesseATK +(vitesseATK*0.33*(this.getLevel()-1))));
	}
	
	public double getPorteeInit() {
		return porteeInit;
	}
	
	public Projectile creationProjectile(Position pmonstre) {
		Laser laser = new Laser(p, pmonstre);
		laser.setDegats(laser.getDegats()+this.getLevel()-1);
		return laser;
	}
	
	@Override
	public String getName() {
		return("lasertower");
	}
}

