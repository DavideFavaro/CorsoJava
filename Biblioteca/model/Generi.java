package it.betacom.ProgettoBiblioteca.model;



public class Generi implements TableRecord {
	
	private int codice;
	private String descrizione;



	public Generi ( int codice, String descrizione ) {
		this.codice = codice;
		this.descrizione = descrizione;
	}



	@Override
	public int getId() {
		return codice;
	}


	public String getDescrizione() {
		return descrizione;
	}



	public void setId( int codice) {
		this.codice = codice;
	}


	public void setDescrizione( String descrizione ) {
		this.descrizione = descrizione;
	}



	@Override
	public String toString() {
		return "Generi [ codice = " + codice + ", descrizione = " + descrizione + " ]";
	}
}