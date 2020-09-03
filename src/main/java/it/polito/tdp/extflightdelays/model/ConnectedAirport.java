package it.polito.tdp.extflightdelays.model;

public class ConnectedAirport implements Comparable<ConnectedAirport>{
	private Airport a1;
	private Double peso;
	/**
	 * @param a1
	 * @param peso
	 */
	public ConnectedAirport(Airport a1, Double peso) {
		super();
		this.a1 = a1;
		this.peso = peso;
	}
	public Airport getA1() {
		return a1;
	}
	public void setA1(Airport a1) {
		this.a1 = a1;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "ConnectedAirport [a1=" + a1 + ", peso=" + peso + "]";
	}
	@Override
	public int compareTo(ConnectedAirport o) {
		// TODO Auto-generated method stub
		return -this.getPeso().compareTo(o.getPeso());
	}
	
	

}
