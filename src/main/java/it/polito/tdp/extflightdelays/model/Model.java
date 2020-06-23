package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private ExtFlightDelaysDAO dao ;
	private Graph <Airport, DefaultWeightedEdge> grafo;
	private Map <Integer, Airport> idMap; 
	private List<Airport> bestPercorso;
	private List<Airport> successivi; 
	private Double maxMiglia;
	
	
	public Model () {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	public void creaGrafo(int distance) {
		this.idMap= new HashMap<>();
		this.grafo = new SimpleWeightedGraph <>(DefaultWeightedEdge.class);
		
		dao.loadAllAirports(idMap);
		
		for (Rotta r : dao.getEdges(distance, idMap)) {
			this.grafo.addVertex(r.getA1());
			this.grafo.addVertex(r.getA2());
			
			Graphs.addEdgeWithVertices(this.grafo, r.getA1(), r.getA2(), r.getPeso());
		}
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Airport> getAirports(){
		List <Airport> aTemp = this.dao.loadAllAirportsTendina();
		List<Airport> result = new ArrayList<>();
		
		for (Airport a : aTemp) {
			if (this.grafo.vertexSet().contains(a)){
				result.add(a);
			}
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public List <AeroportoAdiacente> direttamenteConnessi (Airport a1){
		List<Airport> vicini = Graphs.neighborListOf(this.grafo, a1);
		List<AeroportoAdiacente> result = new ArrayList<>();
		
		for (Airport v : vicini) {
			AeroportoAdiacente aTemp = new AeroportoAdiacente (v, this.grafo.getEdgeWeight(this.grafo.getEdge(a1, v)));
			result.add(aTemp);
		}
		
		Collections.sort(result);
		return result;
	}
	
	public List<Airport> trovaPercorso (Double migliaDisponibili, Airport a1){
		this.bestPercorso = new ArrayList<> ();
		this.maxMiglia=0.0;
		List<Airport> parziale = new ArrayList<>();
		Double migliaParziale = 0.0;
		parziale.add(a1);
		
		cerca(migliaDisponibili, migliaParziale, parziale, a1);
		
		return bestPercorso;
	}

	private void cerca(Double migliaDisponibili, Double migliaParziale, List<Airport> parziale, Airport a1) {
		this.successivi = Graphs.neighborListOf(this.grafo, a1);
		
		if (parziale.size()>bestPercorso.size()) {
			if (migliaParziale> maxMiglia && migliaParziale<=migliaDisponibili) {
				this.maxMiglia = migliaParziale;
				this.bestPercorso= new ArrayList<>(parziale);
			}
		}
		
		for (Airport a : this.successivi) {
			if (!parziale.contains(a)) {
				if (this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a))<=migliaDisponibili) {
					parziale.add(a);
					migliaParziale = this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a));
					this.cerca(migliaDisponibili, migliaParziale, parziale, a);
					parziale.remove(parziale.size()-1);
					migliaParziale = migliaParziale - this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a));
				}
			}
		}
	}
	
	public Double getMiglia() {
		return this.maxMiglia;
	}
	
	

}
