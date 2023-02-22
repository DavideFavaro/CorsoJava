package it.betacom.model;



import java.time.LocalDate;
import java.util.Random;



/**
 * Classe rappresentante un conto investimento.
 * 
 * @author Davide Favaro
 * 
 * @see Conto
 */
public class ContoInvestimento extends Conto {

	private static final Random rand = new Random();



	/**
	 * Crea un <code>ContoInvestimento</code> con i dati indicati ed un tasso di interesse di casuale.
	 * 
	 * @param titolare
	 * 		Titolare del conto
	 * @param data_apertura
	 * 		Data di apertura.
	 */
	public ContoInvestimento ( String titolare, LocalDate data_apertura ) {
		super(titolare, data_apertura, rand.nextDouble());
	}



	@Override
	public synchronized double generaInteressi( LocalDate data ) {
		double res = 0.0;
		if( data.isAfter(getDataApertura()) &&
			data.isBefore(LocalDate.now()) && 
			data.isAfter(getUltimaRiscossione()) ) {
			res = getSaldo() * calcolaInteressi(
				getDataApertura(),
				data,
				getTasso()
			);
			deposita(res, data, true);
			setTasso(rand.nextDouble());
		}
		return res;
	}

}
