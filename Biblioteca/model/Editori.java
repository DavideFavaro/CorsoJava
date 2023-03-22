package it.betacom.ProgettoBiblioteca.model;



public class Editori implements TableRecord {

	private int codice;
	private String nome;
	private String sede;



	public Editori ( int codice, String nome, String sede ) {
		this.codice = codice;
		this.nome = nome;
		this.sede = sede;
	}



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



	public String getSede() {
		return sede;
	}


	public void setSede(String sede) {
		this.sede = sede;
	}



	@Override
	public String toString() {
		return "Editori [ codice = " + codice + ", nome = " + nome + ", sede = " + sede + " ]";
	}

}
