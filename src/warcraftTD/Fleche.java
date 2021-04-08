package warcraftTD;

public class Fleche extends Projectile{
	public static final int degatsInit =2;
	public static int degats = degatsInit;
	public static final double vitesseInit = BaseMonster.vitesseInit*4;
	public static double vitesseDeBase = vitesseInit;
	
	
	public final int degatsLocauxInit=degats;
	private int degatsLocaux = degats;
	
	

	@Override
	public void setDegats(int degats) {
		this.degatsLocaux = degats;
	}

	@Override
	public int getDegats() {
		// TODO Auto-generated method stub
		return degatsLocaux;
	}
	
	public static int getVitesseAffichage() {
		  return (int) Math.round(vitesseDeBase*100);
	}
	public static void setVitesseAffichage(int curseur) {
		double retour  = ((double) curseur/100);
		vitesseDeBase = retour;
	}
	
	public Fleche(Position depart , Position arrivee) {
		super(depart , arrivee);
	}
	
	
	
	
	
	public void draw() {
		if(Main.version_lite) {
			StdDraw.setPenColor(StdDraw.DARK_GREEN);
			Fleche  f = new Fleche(new Position(courant.x,courant.y), new Position(arrivee));
			if(f.courant.dist(arrivee)>=0.05)while(f.courant.dist(courant)<0.005)f.move();
			 StdDraw.line(courant.x, courant.y, f.courant.x, f.courant.y);
		}
		else StdDraw.picture(this.courant.x, this.courant.y, "src\\images\\arrow.png",0.0125/World.getTaille(),0.05/World.getTaille(),getAngle());
		 
	}
	
	/*
	 * l'angle est étrangement invalide, 
	 * nous ne comprenons pas pourquoi
	 * il a l'air de fonctionner seulement sur 1/4 des angles
	 */
	public double getAngle() {
		CouplePositions Vector = new CouplePositions(depart,arrivee);
		double cote_adjacent = Vector.get_dx();
		double hypothenuse = Vector.longueur();
		double angleRad = Math.acos(cote_adjacent/hypothenuse);
		double angleDeg =  Math.toDegrees(angleRad)+90;
		return angleDeg;
	}


	@Override
	public double getVitesse() {
		return vitesseDeBase;
	}

	
	
}

