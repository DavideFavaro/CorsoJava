package it.betacom.ProgettoBiblioteca.model;



import java.util.Optional;



public class Autori implements TableRecord {

	public static enum Sesso { 
		M("maschio"),
		F("femmina");
	
		public final String rep;
		
		private Sesso (String rep) {
			this.rep = rep;
		}
	}


	private int codice;
	private String nome, nazione;
	private short nascita;
	private Optional<Short> morte;
	private Sesso sesso;



	public Autori ( int codice, String nome, String nazione, short nascita, Optional<Short> morte, Sesso sesso ) {
		this.codice = codice;
		this.nome = nome;
		this.nazione = nazione;
		this.nascita = nascita;
		this.morte = morte;
		this.sesso = sesso;
	}



	
	public static String getTableName() {
		return "Autori";
	}


	@Override
	public int getId() {
		return codice;
	}



	public void setId(int codice) {
		this.codice = codice;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getNazione() {
		return nazione;
	}



	public void setNazione(String nazione) {
		this.nazione = nazione;
	}



	public short getNascita() {
		return nascita;
	}



	public void setNascita(short nascita) {
		this.nascita = nascita;
	}



	public Optional<Short> getMorte() {
		return morte;
	}



	public void setMorte(Optional<Short> morte) {
		this.morte = morte;
	}



	public String getSesso() {
		return sesso.rep;
	}



	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}



	@Override
	public String toString() {
		return "Autori [ codice = " + codice
				+ ", nome = " + nome
				+ ", nazione = " + nazione
				+ ", nascita = " + nascita
				+ ", morte = " + morte
				+ ", sesso = " + sesso + " ]";
	}

}
