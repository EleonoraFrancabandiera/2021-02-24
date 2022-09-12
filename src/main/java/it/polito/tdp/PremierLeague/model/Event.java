package it.polito.tdp.PremierLeague.model;

public class Event implements Comparable<Event> {
	
	public enum EventType{
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private EventType type;
	private Risultato ris;
	
	
	public Event(EventType type, Risultato ris) {
		super();
		this.type = type;
		this.ris = ris;
	}

	public EventType getType() {
		return type;
	}

	public Risultato getRisultato() {
		return ris;
	}

	@Override
	public int compareTo(Event o) {
		return this.ris.getSquadraId()-o.getRisultato().getSquadraId();
	}
	
	
	
}
