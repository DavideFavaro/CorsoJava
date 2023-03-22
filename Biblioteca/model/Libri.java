package it.betacom.ProgettoBiblioteca.model;



public class Libri implements TableRecord {

	private int codice;
	private String titolo;
	private int pagine;
	private short pubblicazione;
	private int autore;
	private int genere;
	private int editore;



	public Libri ( int codice, String titolo, int pagine, short pubblicazione, int autore, int genere, int editore ) {
		this.codice = codice;
		this.titolo = titolo;
		this.pagine = pagine;
		this.pubblicazione = pubblicazione;
		this.autore = autore;
		this.genere = genere;
		this.editore = editore;
	}



	@Override
	public int getId() {
		return this.codice;
	}


	public void setId(int id) {
		this.codice = id;
	}



	public String getTitolo() {
		return titolo;
	}


	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}



	public int getPagine() {
		return pagine;
	}


	public void setPagine(int pagine) {
		this.pagine = pagine;
	}



	public short getPubblicazione() {
		return pubblicazione;
	}


	public void setPubblicazione(short pubblicazione) {
		this.pubblicazione = pubblicazione;
	}



	public int getAutore() {
		return autore;
	}


	public void setAutore(int autore) {
		this.autore = autore;
	}



	public int getGenere() {
		return genere;
	}


	public void setGenere(int genere) {
		this.genere = genere;
	}



	public int getEditore() {
		return editore;
	}


	public void setEditore(int editore) {
		this.editore = editore;
	}



	@Override
	public String toString() {
		return "Libri [ codice = " + codice
					+ ", titolo = \"" + titolo
					+ "\", pagine = " + pagine
					+ ", pubblicazione = " + pubblicazione
					+ ", autore = " + autore
					+ ", genere = " + genere
					+ ", editore = " + editore + " ]";
	}

}
