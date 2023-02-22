package it.betacom.model;



import java.time.LocalDate;
import java.util.Optional;



/**
 * Classe rappresentante un conto deposito.
 * 
 * @author Davide Favaro
 * 
 * @see Conto
 */
public class ContoDeposito extends Conto {

	/**
	 * Crea un <code>ContoDeposito</code> con i dati indicati ed un tasso di interesse di 3%.
	 * 
	 * @param titolare
	 * 		Titolare del conto
	 * @param data_apertura
	 * 		Data di apertura.
	 */
	public ContoDeposito (String titolare, LocalDate data_apertura) {
		super(titolare, data_apertura, 0.03);
	}	


	/**
	 * Preleva una quantità di denaro pari a <code>amount</code> in data <code>data</code>.
	 * La quantità di denaro prelevata deve essere inferiore a 1000.
	 * <p>
	 * <b>N.B.</b>: il prelievo non è possibile a seguito della chiusura di un conto.
	 * 
	 * @param amount
	 * @param data
	 * 
	 * @return
	 * 		<code>Optional&lt;Double&gt;</code> contenente il valore relevato, se l'operazione fallisce viene ritornato un
	 * 		Optional vuoto.
	 * 
	 * @see addOperazione
	 * @see Optional
	 */
	@Override
	public synchronized Optional<Double> preleva(double amount, LocalDate data) {
		return amount > 1000 ? Optional.empty() : super.preleva(amount, data);
	}

}