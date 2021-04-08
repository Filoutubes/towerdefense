package warcraftTD;

public class ArrowTower extends Tower{
	
	public static final int tempsDeRechargement = 15;
	public static int vitesseATK = tempsDeRechargement;
	
	public static final int cout = 50;
	
	public static final double rayonInit=0.3;
	public static double porteeInit = rayonInit;
	
	public ArrowTower(Position p) {
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
	
	@Override
	public double getPorteeInit() {
		return porteeInit;
	}
	
	
	
	public Projectile creationProjectile(Position pmonstre) {
		Fleche fleche = new Fleche(p, pmonstre);
		fleche.setDegats(fleche.getDegats()+this.getLevel()-1);
		return fleche;
	}
	
	public static int getPorteeAffichage() {
		/*
		 *  la présence de Math.round est dûe au fait que java (ou eclipse)
		 *  fasse un mauvais arrondi avec les doubles: 
		 *  0.29 * 100 = 28.999999999999996 et pas 29 ce qui cause des souçis
		 */
		return (int) Math.round(porteeInit*100);
	}
	
	public static void setPorteeAffichage(int graduation) {
		
		porteeInit = (double) graduation/100;
	}
	
	@Override
	public String getName() {
		return("arrowtower");
	}
	
}
