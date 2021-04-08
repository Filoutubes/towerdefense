package warcraftTD;

import java.util.List;

public class PositionsAutorisees {
	
	private List<CouplePositions> pas;

	public PositionsAutorisees(List<CouplePositions> pas) {
		this.pas = pas;
	}
	
	/**
	 * 
	 * @param p une position
	 * @return un booléen nous indiquant si oui ou n, p est une position autorisée
	 */
	public boolean estAutorisee(Position p) {
		for(int i=0;i<pas.size();i++) {
			if(pas.get(i).appartient(p))return true;
		}
		return false;
	}
	
	public List<CouplePositions> getPas(){
		return pas;
	}
	
}
