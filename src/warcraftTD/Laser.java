
package warcraftTD;

public class Laser extends Projectile {
	
	public static int degats = 2*Fleche.degats;
	public static double vitesseDeBase = Fleche.vitesseDeBase*5;
	
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
	public Laser(Position depart , Position arrivee) {
		super(new Position(depart.x-0.005,depart.y+0.07) , arrivee);
		
	}
	
	public String getTypeProjectile() {
		return "Laser";
	}

	
	
	public void draw() {
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledCircle(this.depart.x , this.depart.y , 0.007);
		StdDraw.setPenRadius(0.005);
		StdDraw.line(this.depart.x  , this.depart.y  ,this.courant.x, this.courant.y);}


	@Override
	public double getVitesse() {
		return vitesseDeBase;
	}



	
}
