package it.betacom.model;



import java.time.LocalDate;



/**
 * Classe rappresentante un conto corrente.
 * 
 * @author Davide Favaro
 *
 * @see Conto
 */
public class ContoCorrente extends Conto {

	/**
	 * Crea un <code>ContoCorrente</code> con i dati indicati ed un tasso di interesse di 1%.
	 * 
	 * @param titolare
	 * 		Titolare del conto
	 * @param data_apertura
	 * 		Data di apertura.
	 */
	public ContoCorrente (String titolare, LocalDate data_apertura) {
		super(titolare, data_apertura, 0.01);
	}

}