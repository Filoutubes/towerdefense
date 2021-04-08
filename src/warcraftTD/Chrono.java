package warcraftTD;

import java.awt.Color;


//gestion du temps dans la partie
public class Chrono{
	private double depart;
	private double current;
	private String msg;
	private Color colormsg;
	private boolean hasStarted;
	public boolean nouveaute=true;
	
	public Position position = null;
	
	
	private double departPause;
	public boolean enPause;
	private double finPause;
	private double tpsTotalPause;
	
	public Chrono() {
		//start();
		this.depart=0;
		this.current = 0;
		this.colormsg=StdDraw.BLACK;
		this.msg="";
		this.position = null;
		this.hasStarted = false;
		this.enPause = false;
	}

	public void start () {
		reinitTime();
		this.depart = System.nanoTime();
		this.colormsg=StdDraw.BLACK;
		this.msg="";
		this.position = null;
		hasStarted= true;
		timeRecorded = 0;
	}
	
	public boolean hasStarted() {
		return hasStarted;
	}
	
	public void reinitTime() {
		this.depart=0;
		this.current=0;
		this.departPause = 0;
		this.finPause =0;
		this.enPause = false;
		hasStarted=false;
		timeRecorded=0;
	}
	
	// renvoie un int correspondant au nombre de secondes écoulées
	public int tempsEcoule() {
		return (int) tempsDoubleEcoule();
	}
	
	public double tempsDoubleEcoule() {
		return enSecondes(getNano());
	}
	
	private double timeRecorded;
	public void recordTime() {
		timeRecorded = enSecondes(getNano());
	}
	/**
	 * @return le moment qui a été enregistré sous forme de secondesDoubles
	 * 
	 */
	public double getTimeRecorded() {
		return timeRecorded;
	}
	
	public void setTimeRecorded(double time) {
		timeRecorded = time;
	}
	
	public void reinitTimeRecorded() {
		timeRecorded =0;
	}
	
	public double getNano() {
		if(!hasStarted) return 0;
		this.current= System.nanoTime();
		return (current -depart)-dureeNanoPause();
	}
	
	public boolean enPause() {
		return enPause;
	}
	
	private double enSecondes(double nano) {
		return Math.pow(10, -9)*nano;
	}

	public void pause() {
		enPause = true;
		departPause= System.nanoTime();
	}
	
	public void reprendre() {
		tpsTotalPause = dureeNanoPause();
		enPause = false;
		finPause = 0;
		departPause = 0;
		
	}
	
	private double dureeNanoPause() {
		if(enPause)finPause = System.nanoTime();
		return finPause - departPause + tpsTotalPause;
	}
	
	public void setInfos(String msg) {
		this.msg=msg;
	}
	public void setInfos(String msg, Color colormsg) {
		setInfos(msg);
		setColor(colormsg);
	}
	public String getInfos() {
		return msg;
	}

	public boolean possedeInfos() {
		return msg.length()!=0;
	}
	public void setColor(Color colormsg) {
		this.colormsg = colormsg;
	}
	public Color couleurInfos() {
		return colormsg;
	}
}

