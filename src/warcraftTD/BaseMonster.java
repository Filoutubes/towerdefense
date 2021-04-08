package warcraftTD;


class BaseMonster extends Monster {

	public BaseMonster(Position p) {
		super(p);
		
	}
	
	public static final double vitesseInit = 0.01;
	private static double vitesseDeBase = vitesseInit;
	
	public static final int healthInit = 5;
	public static int vieDeBase = healthInit;
	
	public static double getVitesseDeBase() {
		return vitesseDeBase;
	}
	

	@Override
	public double getSpeed() {
		return vitesseDeBase;
	}

	@Override
	public int getRecompense() {
		return 5;
	}
	
	
	
	public static int getGraduation() {
		return (int) (vitesseDeBase/(0.001));
	}
	
	public static void setVitesseDeBase(int graduation) {
		vitesseDeBase = graduation*0.001;
		
	}
	
	public static void setVitesseDeBase(double vitesse) {
		vitesseDeBase = vitesse;
	}
	
	/*
	 * renvoie le nom du dossier sont stockées les images du montre
	 * toutes les images doivent être nommées monster_etatX.png
	 * avec X partant de 0
	 * l'état 0 correspond au monstre en vie
	 */
	@Override
	public String getName() {
		return "baseMonster";
	}

	@Override
	public int getNbEtats() {
		return 4;
	}


	@Override
	public int getHealthInit() {
		return vieDeBase;
	}
	

	@Override
	public Monster copie() {
		// TODO Auto-generated method stub
		return new BaseMonster(this.getPosition());
	}


	@Override
	public String getDirection() {
		// TODO Auto-generated method stub
		return "";
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
