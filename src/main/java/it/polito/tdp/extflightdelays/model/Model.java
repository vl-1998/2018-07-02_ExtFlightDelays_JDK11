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
	private Graph <Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> idMap;
	private ExtFlightDelaysDAO dao;
	private List<Airport> best;
	private Double migliaTot;
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	public void creaGrafo(Double distanzaMinima) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		
		this.dao.loadAllAirports(idMap);
		
		for(Adiacenza a : this.dao.getEdge(distanzaMinima, idMap)) {
			this.grafo.addVertex(a.getA1());
			this.grafo.addVertex(a.getA2());
			
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<Airport> getVertex(){
		List<Airport> vertici = new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
		
	public List<DefaultWeightedEdge> getEdge(){
		List<DefaultWeightedEdge> archi = new ArrayList<>(this.grafo.edgeSet());
		return archi;
	}
	
	public List<ConnectedAirport> direttamenteConnessi(Airport a){
		List<Airport> vicini = Graphs.neighborListOf(this.grafo, a);
		List<ConnectedAirport> result = new ArrayList <>();
		for (Airport s: vicini) {
			ConnectedAirport pTemp = new ConnectedAirport(s,this.grafo.getEdgeWeight(this.grafo.getEdge(a, s)));
			result.add(pTemp);
		}
		
		Collections.sort(result);
		return result;
	}
	
	public List<Airport> ricorsiva(Double migliaDisponibili, Airport a1){
		this.best = new ArrayList<>();
		this.migliaTot = 0.0;
		
		
		List<Airport> parziale = new ArrayList<>();
		parziale.add(a1);
		Double migliaP = 0.0;

		
		cerca(parziale,migliaDisponibili,migliaP, a1);
		
		return best;
	}

	private void cerca(List<Airport> parziale, Double migliaDisponibili, Double migliaP,Airport a1) {
		List<Airport> vicini = Graphs.neighborListOf(this.grafo, a1);
		
		if(migliaP>migliaTot && migliaP<migliaDisponibili) {
			if(parziale.size()>best.size()) {
				this.best = new ArrayList<>(parziale);
				this.migliaTot = migliaP;
			}
		}
		
		for(Airport a : vicini) {
			if(!parziale.contains(a)) {
				if (this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a))<=migliaDisponibili) {
					parziale.add(a);
					migliaP = migliaP + this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a));
					cerca(parziale, migliaDisponibili, migliaP,a);
					parziale.remove(parziale.size()-1);
					migliaP = migliaP - this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a));
				}
			}
		}
		
	}

	public Double getMiglia() {
		// TODO Auto-generated method stub
		return this.migliaTot;
	}


}
