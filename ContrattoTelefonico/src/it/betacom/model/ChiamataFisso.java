package it.betacom.model;



import java.time.LocalDate;
import java.time.LocalTime;



/**
 * Classe rappresentante una chiamata da telefono fisso.
 * 
 * @author Davide Favaro
 * 
 * @see Chiamata
 */
public class ChiamataFisso extends Chiamata {

	public final String indirizzo;



	/**
	 * Crea una nuova <code>ChiamataFisso</code>
	 * 
	 * @param data
	 * 		Data della chiamata.
	 * @param ora
	 * 		Ora della chiamata.
	 * @param nome
	 * 		Nome del chiamante.
	 * @param cognome
	 * 		Cognome del chiamante.
	 * @param numero
	 * 		Numero di telefono del chiamante.
	 * @param indirizzo
	 * 		Indirizzo del chiamante.
	 * @param numeroChiamata
	 * 		Numero della chiamata, ovvero numero delle chiamate ricevute fino a questa, compresa. 
	 * @param durata
	 * 		Durata della chiamata.
	 * @param costo
	 * 		Costo di questa singola chiamata.
	 * @param costoTotale
	 * 		Costo complessivo di questa chiamata e di quelle precedenti.
	 */
	public ChiamataFisso( LocalDate data, LocalTime ora, String nome, String cognome,
			String numero, String indirizzo, int numeroChiamata, float durata,
			float costo, float costoTotale ) {
		super(data, ora, nome, cognome, numero, numeroChiamata, durata, costo, costoTotale);
		this.indirizzo = indirizzo;
	}

	/**
	 * Crea una nuova <code>ChiamataFisso</code> a partire da un'oggetto <code>Chiamata</code>.
	 * L'oggetto creato conterrà esattamente gli stessi dati di <code>chiamata</code> con l'aggiunta di <code>indirizzo</code>.
	 * 
	 * @param chiamata
	 * @param indirizzo
	 */
	public ChiamataFisso( Chiamata chiamata, String indirizzo ) {
		super( chiamata.data, chiamata.ora, chiamata.nome, chiamata.cognome,
			chiamata.numero, chiamata.numeroChiamata, chiamata.durata,
			chiamata.costo, chiamata.getCostoTotale() );
		this.indirizzo = indirizzo;
	}



	@Override
	public String[] toStringsArray() {
		return new String[] {
			""+data, ""+ora, nome, cognome, indirizzo,
			numero, ""+numeroChiamata, ""+durata,
			""+costo, ""+getCostoTotale() 
		};
	}

}
