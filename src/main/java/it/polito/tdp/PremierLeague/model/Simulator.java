package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {
	
	//dati in ingresso
	private int nAzioni;
	private int idTeamHome;
	int idTeamAway;
	
	//dati in uscita
	private Map<Integer, Risultato> mappaRisultati;	
	
	//modello del mondo
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	//coda degli eventi
	private PriorityQueue<Event> queue;

	public Simulator(Graph<Player, DefaultWeightedEdge> grafo, Match m) {
		this.grafo = grafo;		
		idTeamHome= m.getTeamHomeID();
		idTeamAway =m.getTeamAwayID();
	}
	
	public void init(int n) {
		this.nAzioni=n;
		
		
		//inizializzare output
		this.mappaRisultati = new HashMap<>();
		this.mappaRisultati.put(idTeamHome, new Risultato(idTeamHome));
		this.mappaRisultati.put(idTeamAway, new Risultato(idTeamAway));
		
		//creo la coda
		this.queue = new PriorityQueue<>();
		
		//inizializzo la coda
		for(int i=0; i<nAzioni; i++) {
			this.sceltaEvento();
		}
	}
	

	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
		
	}
	
	public void processEvent(Event e) {
		switch(e.getType()) {
			
		case GOAL:
			mappaRisultati.get(e.getRisultato().getSquadraId()).incrementaGoal();
			break;
			
		case ESPULSIONE:
			mappaRisultati.get(e.getRisultato().getSquadraId()).incrementaEspulsioni();
			break;
			
		case INFORTUNIO:
			int unita;
			double caso = Math.random();
			
			if(caso<0.5) {
				unita =2;
			}
			else {
				unita = 3;
			}
			
			for(int i=0; i<unita; i++) {
				this.sceltaEvento();
			}
			break;
			
		}
	}
	
	public void sceltaEvento() {
		
		double caso = Math.random();
		
		if(caso<0.5) {
			//goal
			int squadraScelta;
			if(mappaRisultati.get(idTeamHome).giocatoriInCampo()>mappaRisultati.get(idTeamAway).giocatoriInCampo()) {
				squadraScelta = idTeamHome;
			}
			else if(mappaRisultati.get(idTeamHome).giocatoriInCampo()<mappaRisultati.get(idTeamAway).giocatoriInCampo()) {
				squadraScelta=idTeamAway;
			}
			else {
				squadraScelta = this.getSquadraGiocatoreMigliore();
			}
			
			this.queue.add(new Event(EventType.GOAL, mappaRisultati.get(squadraScelta)));
		}
		else if(caso<0.8) {
			//espulsione
			double caso2= Math.random();
			int scelta = this.getSquadraGiocatoreMigliore();
			
			if(caso2<0.6) {
				//l'espulsione coinvolge la squadra con giocatore migliore					
				this.queue.add(new Event(EventType.ESPULSIONE, mappaRisultati.get(scelta)));
				
			}else {
				//l'espulsione coinvolge la squadra senza giocatore migliore
				if(scelta==idTeamHome) { 
					//la squadra col giocatore migliore è TeamHome
					this.queue.add(new Event(EventType.ESPULSIONE, mappaRisultati.get(idTeamAway)));
				}
				else {
					this.queue.add(new Event(EventType.ESPULSIONE, mappaRisultati.get(idTeamHome)));
				}						
			}
			
		}else {
			//infortunio
			this.queue.add(new Event(EventType.INFORTUNIO,mappaRisultati.get(idTeamHome)));
			
		}
	}
	
	
	public int getSquadraGiocatoreMigliore() {
		int idSquadraMigliore = -1;
		double max=0;
		
		for(Player p : this.grafo.vertexSet()) {
			double delta = this.deltaComplessivo(p);
			if(delta>max) {
				idSquadraMigliore = p.getTeamID();
				max=delta;
			}
		}		
		return idSquadraMigliore;
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

	public Map<Integer, Risultato> getMappaRisultati() {
		return mappaRisultati;
	}
	
}
