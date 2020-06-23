package it.polito.tdp.extflightdelays.model;

public class AeroportoAdiacente implements Comparable<AeroportoAdiacente>{
	private Airport a1;
	private Double p;
	
	/**
	 * @param a1
	 * @param p
	 */
	public AeroportoAdiacente(Airport a1, Double p) {
		super();
		this.a1 = a1;
		this.p = p;
	}
	

	public Airport getA1() {
		return a1;
	}


	public void setA1(Airport a1) {
		this.a1 = a1;
	}


	public Double getP() {
		return p;
	}


	public void setP(Double p) {
		this.p = p;
	}


	@Override
	public int compareTo(AeroportoAdiacente o) {
		return -this.p.compareTo(o.getP());
	}


	@Override
	public String toString() {
		return "AeroportoAdiacente: " + a1 + ", distanza=" + p ;
	}
	
	

}
