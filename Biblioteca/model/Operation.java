package it.betacom.ProgettoBiblioteca.model;


// Classe che rappresenta le operazioni svolte sul DAO e, di conseguenza, sul db
public class Operation {

	// Utilizzata per rappresentare in maniera chiara i tipi
	// di operazioni possibili.
	public static enum OpsType { INSERT, DELETE, UPDATE }

	// Utilizzato principalmente per risalire velocemente
	// all'oggetto interessato o alla riga della tabella
	// corrispondente
	public final int id;
	public final OpsType type;


	public Operation ( int id, OpsType type ) {
		this.id = id;
		this.type = type;
	}
}
