package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	private Map<Integer, Risultato> mappaRis;
	
	public Model() {
		this.dao= new PremierLeagueDAO();
		this.idMap= new HashMap<>();
		
		this.dao.listAllPlayers(idMap);
		
	}
	
	public void creaGrafo(Match m) {
		
		//creo il grafo
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
				
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(m, idMap));
		
		//aggiungo gli archi
		for(Adiacenza a: dao.getAdiacenze(m, idMap)) {
			if(a.getPeso()>=0) {
				//p1 meglio di p2
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}
			}else {
				//p2 meglio di p1
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (-1)* a.getPeso());
				}
			}
		}
		
	}
	
	public Player giocatoreMigliore() {
		Player migliore= null;
		double max=0;
		
		for(Player p : this.grafo.vertexSet()) {
			double delta = this.deltaComplessivo(p);
			if(delta>max) {
				migliore=p;
				max=delta;
			}
		}
		
		return migliore;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean grafoCreato() {
		if(this.grafo==null)
			return false;
		return true;
	}
	
	public List<Match> getAllMatches(){
		List<Match> result = new ArrayList<>(this.dao.listAllMatches());
		Collections.sort(result);
		return result;
	}
	
	public double deltaComplessivo(Player p) {
		
		Set<DefaultWeightedEdge> entranti = this.grafo.incomingEdgesOf(p);
		double pesoEntranti=0;		
		for(DefaultWeightedEdge e : entranti) {
			pesoEntranti+=this.grafo.getEdgeWeight(e);
		}
		
		Set<DefaultWeightedEdge> uscenti = this.grafo.outgoingEdgesOf(p);
		double pesoUscenti=0;		
		for(DefaultWeightedEdge u : uscenti) {
			pesoUscenti+=this.grafo.getEdgeWeight(u);
		}
		
		return (pesoUscenti-pesoEntranti);	
		
	}
	
	public void simula(int nAzioni, Match m) {
		Simulator sim = new Simulator(this.grafo, m);
		sim.init(nAzioni);
		sim.run();
		this.mappaRis=sim.getMappaRisultati();
	}
	
	public Map<Integer, Risultato> getMappaRis(){
		return this.mappaRis;
	}
	
	
}
