package warcraftTD;

public class Boulet extends Projectile{
	
	public static int degats = 4*Fleche.degats;
	public static double vitesseDeBase = Fleche.vitesseDeBase/2;
	
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
	public Boulet(Position depart, Position arrivee) {
		super(depart, arrivee);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		if(Main.version_lite) {
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledEllipse(courant.x, courant.y, 0.015/World.getTaille(), 0.027/World.getTaille());
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledRectangle(courant.x+0.004/World.getTaille(), courant.y+0.004/World.getTaille(), 0.004/World.getTaille()/2, 0.01/World.getTaille()/2);
		}
		else StdDraw.picture(this.courant.x, this.courant.y, "src\\images\\boulet.png");
	}

	@Override
	public double getVitesse() {
		return vitesseDeBase;
	}

	

}
