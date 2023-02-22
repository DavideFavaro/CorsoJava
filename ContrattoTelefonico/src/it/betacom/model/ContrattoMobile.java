package it.betacom.model;



/**
 * Classe rappresentante un contratto telefonico fisso.
 * 
 * @author Davide Favaro
 */
public class ContrattoMobile extends Contratto {
	
	public final float COSTO_ALLA_RISPOSTA = 1.5f;


	/**
	 * Crea un <code>ContrattoMobile</code> con i dati inseriti.
	 * 
	 * @param nome
	 * 		Nome del titolare.
	 * @param cognome
	 * 		Cognome del titolare.
	 * @param numero
	 * 		Numero di telefono del titolare.
	 * @param file_bollette
	 * 		Path e nome del file su cui salvare le bollette.
	 */
	public ContrattoMobile ( String nome, String cognome, String numero, String file_bollette ) {
		super(nome, cognome, numero, file_bollette);
	}
	


	@Override
	public float costoChiamata(int numero_secondi) {
		return super.costoChiamata(numero_secondi) + this.COSTO_ALLA_RISPOSTA;
	}



	@Override
	public void aggiornaBolletta(int numero_secondi) {
		super.aggiornaBolletta(numero_secondi);
		this.bolletta += this.COSTO_ALLA_RISPOSTA;
		getChiamate().get(getNumeroChiamate() - 1).addToCostoTotale(this.COSTO_ALLA_RISPOSTA);
	}

}