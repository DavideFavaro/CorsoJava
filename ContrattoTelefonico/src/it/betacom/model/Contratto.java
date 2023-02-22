package it.betacom.model;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;



/**
 * Classe rappresentante un contratto telefonico.
 * 
 * @author Davide Favaro
 */
public class Contratto {

	public final float COSTO_AL_SECONDO = 0.1f; 

	private String numero, nome, cognome;
	private int numero_chiamate;
	protected float bolletta;
	// Path del file dove verranno inserite le bollette
	//  il file dovrebbe essere inserito senza estensione, quest'ultima verrà inserita in base al file da stampare 
	private String file_bollette;
	// ArrayList contenente array di stringhe con i dati delle singole chiamate
	//  Le chiamate vengono rappresentate come array di stringhe, invece che con oggetti di una classe apposita,
	//   in quanto i metodi di stampa, sia che si usino direttamente le librerie importate,
	//	 sia per la classe FileWriter da noi definita, richiedono come input delle stringhe
	//   
	private ArrayList<Chiamata> chiamate;



	/**
	 * Crea un nuovo <code>Contratto</code>.
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
	public Contratto ( String nome, String cognome, String numero, String file_bollette ) {
		this.nome = nome;
		this.cognome = cognome;
		this.numero = numero;
		this.numero_chiamate = 0;
		this.bolletta = 0;
		this.file_bollette = file_bollette;
		this.chiamate = new ArrayList<>();
	}



	public String getNome() {
		return nome;
	}



	public String getCognome() {
		return cognome;
	}



	public String getNumero() {
		return numero;
	}



	public int getNumeroChiamate() {
		return this.numero_chiamate;
	}



	public float getBolletta() {
		return this.bolletta;
	}



	public String getFileBollette() {
		return this.file_bollette;
	}


	/**
	 * Ritorna i dati del titolare del contratto come unica stringa.
	 * 
	 * @return
	 * 		Singola istanza di <code>String</code> contenente nome, cognome e numero del titolare di questo contratto. 
	 */
	public String getDatiUtente() {
		return getNome() + " " + getCognome() + " " + getNumero();
	}



	public ArrayList<Chiamata> getChiamate() {
		return this.chiamate;
	}


	/**
	 * Ritorna l'ultima telefonata registrata.
	 * 
	 * @return
	 * 		Oggetto <code>Chiamata</code> rappresntante l'ultima telefonata registrata.
	 * 
	 * @see Chiamata
	 */
	public Chiamata getLastChiamata() {
		return getChiamate().get(getNumeroChiamate() - 1);
	}



	/**
	 * Ritorna i nomi delle colonne per file in formato tabellare.
	 * 
	 * @return
	 * 		<code>ArrayList</code> con i nomi delle colonne da inserire in un file in formato tabellare.
	 */
	public ArrayList<String> getNomiValori() {
		ArrayList<String> nomi = new ArrayList<>();
		nomi.add("Data");
		nomi.add("Ora");
		nomi.add("Nome");
		nomi.add("Cognome");
		nomi.add("Numero");
		nomi.add("Telefonate");
		nomi.add("Durata");
		nomi.add("Costo");
		nomi.add("Totale");
		return nomi;
	}



	/**
	 * Ritorna la singola stringa da inserire nei file per ogni chiamata a partire da <code>dati</code>.
	 * 
	 * @param dati
	 *		Valori da inserire nella stringa rappresentante la bolletta.
	 * 
	 * @return
	 * 		Singola istanza di <code>String</code> contenete una singola bolletta. 
	 */
	public String getEntryString( String[] dati ) {
		return "Bolletta del " + dati[0] +
				"\n   Num.Telefonate: " + dati[5] +
				"   Costo Totale: " + dati[8] +
				" €\nDettaglio Telefonata\n   Data Telefonata: " +
				dati[0] + " " + dati[1] + "   Durata: " + dati[6] +
				" sec\n   Costo: " + dati[7] + " €\n\n";
	}
	
	/**
	 * Ritorna la singola stringa da inserire nei file per ogni chiamata.
	 * 
	 * @param chiamata
	 *		<code>Chiamata</code> di cui inserire i dati.
	 * 
	 * @return
	 * 		Singola istanza di <code>String</code> contenete una singola bolletta. 
	 */
	public String getEntryString( Chiamata chiamata ) {
		return String.format(
			"Bolletta del %6$s\n   Num.Telefonate: %d   Costo Totale: %f €\nDettaglio Telefonata\n   Data Telefonata: %6$s %s   Durata: %f sec\n   Costo: %f €\n\n",
			chiamata.numeroChiamata, chiamata.getCostoTotale(), chiamata.ora.toString(), chiamata.durata, chiamata.costo, chiamata.data.toString()
		);
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}



	public void setNumero(String numero) {
		this.numero = numero;
	}



	/**
	 * Incrementa il contatore delle chiamate ricevute
	 */
	protected void addTelefonata() {
		++this.numero_chiamate;
	}


	/**
	 * Calcola il costo di una chiamata con durata pari a <code>numero_secondi</code>
	 * 
	 * @param numero_secondi
	 * 
	 * @return
	 * 		Costo della chiamata
	 */
	public float costoChiamata(int numero_secondi) {
		return numero_secondi * COSTO_AL_SECONDO;
	}



	/**
	 * Incrementa il costo totale in base a <code>numero_secondi</code> della chiamata e aggiunge una nuova entry alla lista delle chiamate.
	 * 
	 * @param numero_secondi
	 */
	public void aggiornaBolletta(int numero_secondi) {
		float costo = costoChiamata(numero_secondi);
		this.bolletta += costo;
		addTelefonata();
		this.chiamate.add(new Chiamata(
			LocalDate.now(),
			LocalTime.now(),
			getNome(),
			getCognome(),
			getNumero(),
			getNumeroChiamate(),
			numero_secondi,
			costo,
			getBolletta()
		));
	}

}







