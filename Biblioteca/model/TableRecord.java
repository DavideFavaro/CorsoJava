package it.betacom.ProgettoBiblioteca.model;


// Interfaccia usata per individuare solo gli oggetti per cui
// ha effettivamente un senso tilizzare la classe DAO.
 // Al momento ha come solo metodo "getID",
 // potrebbero esserci altri metodi sensati da aggiungere.
public interface TableRecord {
	
	public int getId();

}
