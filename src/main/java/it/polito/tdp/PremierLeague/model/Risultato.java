package it.polito.tdp.PremierLeague.model;

public class Risultato {
	
	private int squadraId;	
	private int goal;
	private int espulsioni;
	
	public Risultato(int squadraId) {
		super();
		this.squadraId = squadraId;
		this.goal=0;
		this.espulsioni=0;
	}

	public int getSquadraId() {
		return squadraId;
	}

	
	public int getGoal() {
		return goal;
	}
	
	public void incrementaGoal() {
		this.goal++;
	}


	public int getEspulsioni() {
		return espulsioni;
	}
	
	public void incrementaEspulsioni() {
		this.espulsioni++;
	}
	
	public int giocatoriInCampo() {
		return 11-espulsioni;
	}

	@Override
	public String toString() {
		return " goal: " + goal + ", espulsioni: " + espulsioni;
	}
	
	
}
