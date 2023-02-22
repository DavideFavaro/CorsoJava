package it.betacom.model;



import java.time.LocalDate;


/**
 * Classe che rappresenta un'operazione bancaria.
 * <p>
 * Le operazioni possibili sono:
 * <ul>
 * <li> Apertura;
 * <li> Deposito;
 * <li> Prelievo;
 * <li> Riscossione;
 * <li> Chiusura;
 * </ul>
 * @author Davide Favaro
 */
public class Operazione {
	
	/**
	 * Enum rappresentante il tipo di operazione eseguitasu un conto.
	 * 
	 * @see Operazione
	 */
	public enum OpType { APERTURA, DEPOSITO, PRELIEVO, RISCOSSIONE, CHIUSURA };
	
	public final double amount, saldoSucc;
	public final LocalDate data;
	public final OpType tipo;
	
	
	
	/**
	 * Crea una nuova operazione bancaria a partire dai valori indicati.
	 * 
	 * @param amount
	 * 		Ammontare dell'operazione bancaria.
	 * @param saldoSucc
	 * 		Saldo totale successivo all'operazione.
	 * @param data
	 * 		Data di esecuzione dell'operazione.
	 * @param tipo
	 * 		Tipo di operazione eseguita.
	 * 
	 * @see OpType		
	 */
	public Operazione ( double amount, double saldoSucc, LocalDate data, OpType tipo ) {
		this.amount = amount;
		this.saldoSucc = saldoSucc;
		this.data = data;
		this.tipo = tipo;
	}
}
