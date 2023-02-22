package it.betacom.model;



import java.util.ArrayList;



/**
 * Classe rappresentante un contratto telefonico fisso.
 * 
 * @author Davide Favaro
 */
public class ContrattoFisso extends Contratto {

	private String indirizzo;


	/**
	 * Crea un <code>ContrattoFisso</code> con i dati inseriti.
	 * 
	 * @param nome
	 * 		Nome del titolare.
	 * @param cognome
	 * 		Cognome del titolare.
	 * @param numero
	 * 		Numero di telefono del titolare.
	 * @param indirizzo
	 * 		Indirizzo del titolare.
	 * @param file_bollette
	 * 		Path e nome del file su cui salvare le bollette.
	 */
	public ContrattoFisso ( String nome, String cognome, String numero, String indirizzo, String file_bollette ) {
		super(nome, cognome, numero, file_bollette);
		this.indirizzo = indirizzo;
	}



	public String getIndirizzo() {
		return this.indirizzo;
	} 


	
	public void setIndirizzo( String nuovo_indirizzo ) {
		this.indirizzo = nuovo_indirizzo;
	}



	/**
	 * Ritorna i dati del titolare del contratto come unica stringa.
	 * 
	 * @return
	 * 		Singola istanza di <code>String</code> contenente nome, cognome, indirizzo e numero del titolare di questo contratto. 
	 */
	@Override
	public String getDatiUtente() {
		return super.getDatiUtente() + " " + getIndirizzo(); 
	}



	@Override
	public ArrayList<String> getNomiValori() {
		ArrayList<String> nomi = super.getNomiValori();
		nomi.add( nomi.lastIndexOf("Numero"), "Indirizzo" );
		return nomi;
	}



	@Override
	public void aggiornaBolletta(int numero_secondi) {
		super.aggiornaBolletta(numero_secondi);
		super.getChiamate().add(
			new ChiamataFisso(
				// `super.aggiornaBollette()` aggiunge una `Chiamata`, l'oggetto viene rimosso e sostituito con
				 // con un'istanza di `ChiamataFisso`
				super.getChiamate().remove( getNumeroChiamate() - 1 ),
				getIndirizzo()
			)
		);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}