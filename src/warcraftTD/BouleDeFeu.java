package warcraftTD;

public class BouleDeFeu extends Projectile {
	public static final int degatsInit =1;
	public static int degats = degatsInit;
	public static final double vitesseInit = BaseMonster.vitesseInit;
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

	public BouleDeFeu(Position depart , Position arrivee) {
		super(depart , arrivee);
	}





	public void draw() {
		if(Main.version_lite) {
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.filledEllipse(courant.x, courant.y, 0.015/World.getTaille(), 0.027/World.getTaille());
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledRectangle(courant.x+0.004/World.getTaille(), courant.y+0.004/World.getTaille(), 0.004/World.getTaille()/2, 0.01/World.getTaille()/2);
		}
		else StdDraw.picture(this.courant.x, this.courant.y, "src\\images\\Boule_de_Feu.png",0.05/World.getTaille(),0.2/World.getTaille(),getAngle());
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
