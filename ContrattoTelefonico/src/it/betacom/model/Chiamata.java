package it.betacom.model;



import java.time.LocalDate;
import java.time.LocalTime;



/**
 * Classe rappresentante una chiamata telefonica 
 * 
 * @author Davide Favaro
 */
public class Chiamata {

	public final LocalDate data;
	public final LocalTime ora;
	public final String nome;
	public final String cognome;
	public final String numero;
	public final int numeroChiamata;
	public final float durata;
	public final float costo;
	private float costoTotale;



	/**
	 * Costruttore preincipale.
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
	 * @param numeroChiamata
	 * 		Numero della chiamata, ovvero numero delle chiamate ricevute fino a questa, compresa. 
	 * @param durata
	 * 		Durata della chiamata.
	 * @param costo
	 * 		Costo di questa singola chiamata.
	 * @param costoTotale
	 * 		Costo complessivo di questa chiamata e di quelle precedenti.
	 */
	public Chiamata( LocalDate data, LocalTime ora, String nome, String cognome, String numero,
			int numeroChiamata, float durata, float costo, float costoTotale ) {
		this.data = data;
		this.ora = ora;
		this.nome = nome;
		this.cognome = cognome;
		this.numero = numero;
		this.numeroChiamata = numeroChiamata;
		this.durata = durata;
		this.costo = costo;
		this.costoTotale = costoTotale;
		
	}



	public float getCostoTotale() {
		return costoTotale;
	}



	/**
	 * Incrementa il costo totale di una chiamata di <code>amount</code>.
	 * 
	 * @param amount
	 * 		Quantità di cui viene aumentato il costo totale della chiamata.
	 */
	public void addToCostoTotale(float amount) {
		this.costoTotale += amount;
	}



	/**
	 * Ritorna un vettore di stringhe contenente i dati presenti nella chiamata.
	 * 
	 * @return
	 * 		Rappresentazione come <code>String[]</code> della chiamata.
	 */
	public String[] toStringsArray() {
		return new String[] {
			""+data, ""+ora, nome, cognome, numero,
			""+numeroChiamata, ""+durata, ""+costo,
			""+costoTotale 
		};
	}

}













